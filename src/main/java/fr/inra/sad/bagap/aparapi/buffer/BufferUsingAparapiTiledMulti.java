package fr.inra.sad.bagap.aparapi.buffer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.aparapi.Kernel;

public class BufferUsingAparapiTiledMulti {
	
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
			short dep = 1;
			short tile = 20;	
			long begin, end;
			
			//file = new File(Buffer.class.getResource("/bretagne.tif").toURI());
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
			Set<Short> inValues = new TreeSet<Short>();
			for(short s : inDatas){
				if(s!=-1 && s!=0 && !inValues.contains(s)){
					inValues.add(s);
				}
			}
			System.out.println(inValues);
			short[] values = new short[inValues.size()];
			int index = 0;
			for(Short s : inValues){
				values[index++] = (short) s;
			}
			
			float[][] outDatas = new float[(((width-1)/dep)+1)*(((height-1)/dep)+1)][values.length+2];
			//float[] outDatas = new float[(((width-1)/dep)+1)*(((height-1)/dep)+1)];
			
			
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
			}
			*/
			CountValue cv = new CountValue(values, windowSize, shape, coeffs, width, height, dep, inDatas, outDatas);
			cv.setExplicit(true);
			begin = System.currentTimeMillis();
			for(int t=0; t<height; t+=tile){
				//System.out.println(j);
				cv.applySlidingWindow(t, (short) Math.min(tile, (height-t)));
				
				cv.get(outDatas);
			}
			//cv.get(outDatas);
			end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			cv.dispose();
			
			
			fw = new FileWriter("C:/Hugues/temp/image_multi.txt");
			sb = new StringBuffer();
			
			index = 0;
			for(int j=0; j<(((height-1)/dep))+1; j++){
				sb.setLength(0);
				for(int i=0; i<(((width-1)/dep))+1; i++){
					sb.append(outDatas[index++][5]+ " ");
					//sb.append("1 ");
				}
				sb.append('\n');
				fw.write(sb.toString());
			}
			
			
			fw.close();
			
		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	final static class CountValue extends Kernel {

		private int width, height;
		
		private int dep;

		private short[] imageIn;
		
		private float[][] imageOut;
		
		private short[] shape;
		
		private float[] coeff;
		
		private int windowSize;
		
		private short[] values;
		
		private int theY;
		
		public CountValue(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, short[] imageIn, float[][] imageOut){
			this.values = values;
			this.windowSize = windowSize;
			this.shape = shape;
			this.coeff = coeff;
			this.width = width;
			this.height = height;
			this.dep = dep;
			this.imageIn = imageIn;
			this.imageOut = imageOut;
		}
 
		public void processPixel(int x, int y) {
			
			if(x%dep == 0 && y%dep == 0){
				//System.out.println(x+" "+y+" "+(((y/dep) * (((width-1)/dep)+1) + (x/dep))));
				
				int ind = ((y/dep) * (((width-1)/dep)+1) + (x/dep));
				
				int mid = windowSize / 2;
				int ic;
				short v;
				
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height)){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width)){
								ic = ((dy+mid) * windowSize) + (dx+mid);
								if(shape[ic] == 1){
									v = imageIn[((y + dy) * width) + (x + dx)];
									
									if(v == -1){
										imageOut[ind][0] = imageOut[ind][0] + coeff[ic];;
									}else{
										if(v == 0){
											imageOut[ind][1] = imageOut[ind][1] + coeff[ic];;
										}else{
											for(int i=0; i<values.length; i++){
												if(v == values[i+2]){
												 imageOut[ind][i+2] = imageOut[ind][i+2] + coeff[ic];
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		public void applySlidingWindow(int theY, short tile) {
		//public void applySlidingWindow(int[] _imageIn, float[] _imageOut, int theY) {

			//long begin = System.currentTimeMillis();
			//imageIn = _imageIn;
			//imageOut = _imageOut;
			
			this.theY = theY;
			
			//put(imageOut);
			//execute(width * height);
			execute(width * tile);
			//get(imageOut);
			//long end = System.currentTimeMillis();
			//System.out.println("time computing : "+(end - begin));
		}

		@Override
		public void run() {
			final int x = getGlobalId(0) % width;
			final int y = getGlobalId(0) / width;
			//final int y = 18;
			processPixel(x, theY + y);
			//System.out.println();
		}
	}

	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}

}
