package fr.inra.sad.bagap.aparapi.buffer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import com.aparapi.Kernel;

public class BufferUsingAparapiTiledNoOut {
	
	public static void main(final String[] _args) throws IOException {
		final File file;
		FileWriter fw;
		StringBuffer sb;
		try {
			System.out.println("buffer using aparapi");
			
			short windowSize = 51;
			short mid = (short) (windowSize/2);
			int width = 1000;
			int height = 1000;
			short dep = 5;
			short tile = 5;
			
			tile = (short) Math.max(dep, tile);
			
			file = new File("C://Hugues/modelisation/chloe/v5/data/bretagne.tif");

			ImageInputStream stream = ImageIO.createImageInputStream(file);

			// File or input stream
			ImageReader reader = ImageIO.getImageReaders(stream).next();
			reader.setInput(stream);

			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle sourceRegion = new Rectangle(11000, 11000, width, height); 
			param.setSourceRegion(sourceRegion); // Set region

			BufferedImage inputImage = reader.read(0, param); // Will read only the region specified

			System.out.println(inputImage);

			//BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);

			short[] inDatas = ((DataBufferShort) inputImage.getRaster().getDataBuffer()).getData();
			int[] datas = new int[inDatas.length];
			for(int s=0; s<inDatas.length; s++){
				datas[s] = inDatas[s];
			}
			//float[] outDatas = ((DataBufferFloat) outputImage.getRaster().getDataBuffer()).getData();
			System.out.println(((((width-1)/dep)+1)*(((tile-1)/dep)+1)));
			float[] outDatas = new float[((((width-1)/dep)+1)*(((tile-1)/dep)+1))];
			
			
			
			short[] shape = new short[windowSize*windowSize];
			float[] coeffs = new float[windowSize*windowSize];
			
			for(short j=0; j<windowSize; j++){
				for(short i=0; i<windowSize; i++){
					
					// gestion en cercle
					if(distance(mid, mid, i, j) <= mid){
						shape[(j * windowSize) + i] = 1;
						//System.out.print(1+" ");
					}else{
						shape[(j * windowSize) + i] = 0;
						//System.out.print(0+" ");
					}
					
					// gestion des distances pondérées (décroissantes)
					float d = mid - distance(mid, mid, i, j);
					if(d < 0){
						d = 0;
					}
					coeffs[(j * windowSize) + i] = (float) d / mid;
					//System.out.println((float) d / mid);
				}
				//System.out.println();
			}
			
			/*
			for(int s=0; s<(windowSize*windowSize); s++){
				shape[s] = 1;
			}
			
			for(int c=0; c<(windowSize*windowSize); c++){
				coeffs[c] = 1;
			}*/
			
			CountValue cv = new CountValue(5, windowSize, shape, coeffs, width, height, dep, datas, outDatas);
			cv.setExplicit(true);
			fw = new FileWriter("C:/Hugues/temp/image_no_out.txt");
			sb = new StringBuffer();
			long begin = System.currentTimeMillis();
			int index;
			int nextJ = 0;
			for(int t=0; t<height; t+=tile){
				
				cv.applySlidingWindow(t, (short) Math.min(tile, (height-t)));
				cv.get(outDatas);
				
				sb.setLength(0);
				index = 0;
				for(int j=nextJ%tile; j<Math.min(tile, height-t); j+=dep){
					nextJ += dep;
					for(int i=0; i<width; i+=dep){
						sb.append(outDatas[index++]+ " ");
					}
					sb.append('\n');
				}
				
				fw.write(sb.toString());
			}
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			cv.dispose();
			fw.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	final static class CountValue extends Kernel {

		private int width, height;
		
		private int dep;

		private int imageIn[];
		
		private float imageOut[];
		
		private short[] shape;
		
		private float[] coeff;
		
		private int windowSize;
		
		private int value;
		
		private int theY;
		
		public CountValue(int value, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, int[] imageIn, float[] imageOut){
			this.value = value;
			this.windowSize = windowSize;
			this.shape = shape;
			this.coeff = coeff;
			this.width = width;
			this.height = height;
			this.dep = dep;
			this.imageIn = imageIn;
			this.imageOut = imageOut;
		}
 
		public void processPixel(int x, int y, int localY) {
			if(x%dep == 0 && y%dep == 0){
				float count = 0f;
				int mid = windowSize / 2;
				int ic;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height)){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width)){
								ic = ((dy+mid) * windowSize) + (dx+mid);
								if(shape[ic] == 1){
									if(imageIn[((y + dy) * width) + (x + dx)] == value){
										count += coeff[ic];
									}
								}
							}
						}
					}
				}
				
				imageOut[((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)))] = count;
				
			}
		}

		public void applySlidingWindow(int theY, short tile) {
			this.theY = theY;
			execute(width * tile);
		}

		@Override
		public void run() {
			final int x = getGlobalId(0) % width;
			final int y = getGlobalId(0) / width;
			processPixel(x, theY + y, y);
		}
	}
	
	
	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
	final static class DistanceComparator implements Comparator<Integer> {
		Map<Integer, Float> map;
		public DistanceComparator(Map<Integer, Float> map){
			this.map = map;
		}
		@Override
		public int compare(Integer v1, Integer v2) {
			if(map.get(v1) >= map.get(v2)) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	private static short[] getOrderedIndex(int windowSize){
		int mid = windowSize/2;
		
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		Comparator<Integer> dComparator = new DistanceComparator(map);
		Map<Integer, Float> distanceByIndex = new TreeMap<Integer, Float>(dComparator);
		for(short j=0; j<windowSize; j++){
			for(short i=0; i<windowSize; i++){
				map.put((j * windowSize) + i, distance(i, j, mid, mid));
			}
		}
		distanceByIndex.putAll(map);
		
		short[] orderedI = new short[windowSize*windowSize];
		int ind = 0;
		for(Integer i : distanceByIndex.keySet().toArray(new Integer[windowSize*windowSize])){
			orderedI[ind++] = (short) ((int) i);
		}
		
		return orderedI;
	}

}
