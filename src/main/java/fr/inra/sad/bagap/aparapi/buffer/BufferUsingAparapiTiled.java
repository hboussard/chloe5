package fr.inra.sad.bagap.aparapi.buffer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.aparapi.Kernel;

public class BufferUsingAparapiTiled {
	
	public static void main(final String[] _args) throws IOException {
		final File file;
		FileWriter fw;
		StringBuffer sb;
		try {
			System.out.println("buffer using aparapi");
			
			short windowSize = 51;
			short mid = (short) (windowSize/2);
			int width = 500;
			int height = 500;
			short dep = 1;
			short tile = 1;
			
			/*
			short[] orderedIndex = getOrderedIndex(windowSize);
			
			Matrix mFriction = ArrayMatrixFactory.get().createWithAsciiGrid("C:/Hugues/temp/friction.asc", true);
			float[] friction = new float[width*height];
			int ipp=0;
			for(Pixel p : mFriction){
				friction[ipp++] = (float) ((double) mFriction.get(p));
				//friction[ipp++] = 1f;
			}
			
			float[][] functionalWindows = new float[width*height][windowSize*windowSize];
			for(float[] fW : functionalWindows){
				for(int i=0; i<fW.length; i++){
					fW[i] = 9999f;
				}
			}
			
			DiffusionAlgorithm da = new DiffusionAlgorithm(windowSize, orderedIndex, 5.0f, width, height);
			da.setExplicit(true);
			da.applyDiffusion(friction, functionalWindows);	
			da.dispose();
			
			da = new DiffusionAlgorithm(windowSize, orderedIndex, 5.0f, width, height);
			da.setExplicit(true);
			da.applyDiffusion(friction, functionalWindows);	
			da.dispose();
			
			
			float[] fWs = functionalWindows[260*width + 396];
			ipp = 0;
			for(float d : fWs){
				if(ipp%windowSize == 0){
					ipp = 0;
					System.out.println();
				}
				System.out.print(d+" ");
				ipp++;
			}
				*/		
			
			file = new File(Buffer.class.getResource("/bretagne.tif").toURI());

			ImageInputStream stream = ImageIO.createImageInputStream(file);

			// File or input stream
			ImageReader reader = ImageIO.getImageReaders(stream).next();
			reader.setInput(stream);

			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle sourceRegion = new Rectangle(10000, 10000, width, height); 
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
			float[] outDatas = new float[inDatas.length/dep];
			
			/*
			short[][] shapeFriction = new short[width*height][windowSize*windowSize];
			float[][] coeffFriction = new float[width*height][windowSize*windowSize];
			
			ipp = 0;
			for(float[] fW : functionalWindows){
				for(short j=0; j<windowSize; j++){
					for(short i=0; i<windowSize; i++){
						// gestion de la forme
						if(fW[(j * windowSize) + i] <= 15){
							shapeFriction[ipp][(j * windowSize) + i] = 1;
						}else{
							shapeFriction[ipp][(j * windowSize) + i] = 0;
							//shapeFriction[ipp][(j * windowSize) + i] = 1;
						}
						
						// gestion des distances pondérées (1 partout)
						// coeffFriction[ipp][(j * windowSize) + i] = 1;
						
						// gestion des distances pondérées (décroissante)
						coeffFriction[ipp][(j * windowSize) + i] = (float) ((double) fW[(j * windowSize) + i] / 15.0);
					}
				}
				ipp++;
			}
			
			CountValueWithFriction cvwf = new CountValueWithFriction(11, windowSize, shapeFriction, coeffFriction, width, height);
			cvwf.setExplicit(true);
			cvwf.applySlidingWindow(datas, outDatas);	
			cvwf.dispose();
			
			cvwf = new CountValueWithFriction(11, windowSize, shapeFriction, coeffFriction, width, height);
			cvwf.setExplicit(true);
			cvwf.applySlidingWindow(datas, outDatas);	
			cvwf.dispose();
			
			FileWriter fw = new FileWriter("C:/Hugues/temp/image_func.txt");
			StringBuffer sb = new StringBuffer();
			for(int j=0; j<height; j++){
				sb.setLength(0);
				for(int i=0; i<width; i++){
					sb.append(outDatas[(j * width) + i]+ " ");
				}
				fw.write(sb.toString());
				fw.append("\n");
			}
			fw.close();
			*/
			
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
			long begin = System.currentTimeMillis();
			for(int j=0; j<height; j+=tile){
				System.out.println(j);
				cv.applySlidingWindow(j, (short) Math.min(tile, (height-j)));
			}
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			cv.dispose();
			
			/*
			CountValue cv;
			long begin = System.currentTimeMillis();
			for(int j=0; j<height; j++){
				System.out.println(j);
				cv = new CountValue(5, windowSize, shape, coeffs, width, height, datas, outDatas);
				cv.setExplicit(true);
				cv.applySlidingWindow(j);	
				cv.dispose();
			}
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			*/
			
			/*
			cv = new CountValue(5, windowSize, shape, coeffs, width, height, dep, datas, outDatas);
			cv.setExplicit(true);
			cv.applySlidingWindow(datas, outDatas);	
			cv.dispose();
			*/
			/*
			cv = new CountValue(5, windowSize, shape, coeffs, width, height, dep, datas, outDatas);
			cv.setExplicit(true);
			begin = System.currentTimeMillis();
			for(int j=0; j<height; j+=tile){
				//System.out.println(j);
				cv.applySlidingWindow(j, (short) Math.min(tile, (height-j)));
			}
			end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			cv.dispose();
			*/
			fw = new FileWriter("C:/Hugues/temp/image_treshold.txt");
			sb = new StringBuffer();
			for(int j=0; j<(height/dep); j++){
				sb.setLength(0);
				for(int i=0; i<(width/dep); i++){
					sb.append(outDatas[(j * width) + i]+ " ");
				}
				fw.write(sb.toString());
				fw.append("\n");
			}
			fw.close();
			
			//((DataBufferFloat) outputImage.getRaster().getDataBuffer()).getData() = outDatas;
			
			//File outputfile = new File("C:/Hugues/temp/image_aparapi1.tif");
			//ImageIO.write(outputImage, "tif", outputfile);
			
			
		
		} catch (URISyntaxException e) {
			throw new IllegalStateException("could not get map", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	final static class CountValueWithFriction extends Kernel {

		private int width, height;

		private int imageIn[];
		
		private float imageOut[];
		
		private short[][] shape;
		
		private float[][] coeff;
		
		private int windowSize;
		
		private int value;
		
		public CountValueWithFriction(int value, int windowSize, short[][] shape, float[][] coeff, int width, int height){
			this.value = value;
			this.windowSize = windowSize;
			this.shape = shape;
			this.coeff = coeff;
			this.width = width;
			this.height = height;
		}

		public void processPixel(int x, int y) {
			float count = 0f;
			int mid = windowSize / 2;
			int ic;
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height)){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width)){
							ic = ((dy+mid) * windowSize) + (dx+mid);
							final short sh = shape[(y * width) + x][ic];
							if(sh == 1){
								final float c = coeff[(y * width) + x][ic];
								final int v = imageIn[((y + dy) * width) + (x + dx)];
								if(v == value){
									count += c;
								}
							}
						}
					}
				}
			}
			
			imageOut[(y * width) + x] = (float) count;
		}

		public void applySlidingWindow(int[] _imageIn, float[] _imageOut) {

			long begin = System.currentTimeMillis();
			imageIn = _imageIn;
			imageOut = _imageOut;
			
			put(imageOut);
			execute(width * height);
			get(imageOut);
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
		}

		@Override
		public void run() {
			final int x = getGlobalId(0) % width;
			final int y = getGlobalId(0) / width;
			processPixel(x, y);
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
		
		private short tile;
		
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
 
		public void processPixel(int x, int y) {
			
			
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
				
				imageOut[((y * width) + x)/dep] = count;
				
				//imageOut[((y * width) + x)/dep] = imageIn[(y * width) + x];
			}
			/*
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
			
			imageOut[(y * width) + x] = count;
			*/
			//imageOut[(y * width) + x] = imageIn[(y * width) + x];
		}

		public void applySlidingWindow(int theY, short tile) {
		//public void applySlidingWindow(int[] _imageIn, float[] _imageOut, int theY) {

			//long begin = System.currentTimeMillis();
			//imageIn = _imageIn;
			//imageOut = _imageOut;
			
			this.theY = theY;
			this.tile = tile;
			
			put(imageOut);
			//execute(width * height);
			execute(width * tile);
			get(imageOut);
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

	final static class DiffusionAlgorithm extends Kernel {

		private int width;
		
		private int height;
		
		private int windowSize;
		
		private float cellSize;
		
		private float[] input;
		
		private short[] orderedIndex;
		
		private float[][] functionalWindows;
		
		public DiffusionAlgorithm(int windowSize, short[] orderedIndex, float cellSize, int width, int height){
			this.windowSize = windowSize;
			this.orderedIndex = orderedIndex;
			this.cellSize = cellSize;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public void run() {
			final int x = getGlobalId(0) % width;
			final int y = getGlobalId(0) / width;
			processPixel(x, y);
			//System.out.println(x+" "+y);
		}
		
		public void processPixel(int x, int y) {
			int mid = windowSize / 2;
			functionalWindows[(y * width) + x][(mid * windowSize) + mid] = 0;
			
			//byte[] ever = new byte[windowSize * windowSize];
			short io;
			int iox, ioy, gx, gy;
			for(int i=0; i<orderedIndex.length; i++){
				io = orderedIndex[i];
			//for(short io : orderedIndex){
				iox = io % windowSize;
				ioy = io / windowSize;
				gx = x + (iox - mid);
				gy = y + (ioy - mid);
				//System.out.println("la1 "+iox+" "+ioy+" "+gx+" "+gy);
				if(gx>=0 && gx<width && gy>=0 && gy<height){
					localDiffusion(x, y, iox, ioy, gx, gy);
				}
			}
		}
		
		private float minF(float f1, float f2){
			if(f1 < f2){
				return f1;
			}
			return f2;
		}
		
		public void localDiffusion(int x, int y, int lx, int ly, int gx, int gy) {
			
			int il = (ly * windowSize) + lx;
			//ever[il] = 1;
			float crcm = functionalWindows[(y * width) + x][il];
			
			int ig = (gy * width) + gx;
			//System.out.println("la2 "+x+" "+y+" "+lx+" "+ly+" "+gx+" "+gy+" "+il+" "+crcm+" "+ig);
			float cv = input[ig];
					
			//System.out.println("la3 "+lx+" "+ly+" "+gx+" "+gy+" "+il+" "+crcm+" "+ig+" "+cv);
			
			int ilb = 1, igb = 1;
			
			//nord
			if(ly != 0 && gy != 0){
				ilb = ((ly-1) * windowSize) + lx;
				igb = ((gy-1) * width) + gx;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.5f*cv + 0.5f*input[igb]) * cellSize);
			}
			
			//ouest
			if(lx != (windowSize-1) && gx != (width-1)){
				ilb = (ly * windowSize) + lx+1;
				igb = (gy * width) + gx+1;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.5f*cv + 0.5f*input[igb]) * cellSize);
			}
			
			//sud
			if(ly != (windowSize-1) && gy != (height-1)){
				ilb = ((ly+1) * windowSize) + lx;
				igb = ((gy+1) * width) + gx;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.5f*cv + 0.5f*input[igb]) * cellSize);
			}
			
			//est
			if(lx != 0 && gx != 0){
				ilb = (ly * windowSize) + lx-1;
				igb = (gy * width) + gx-1;
	
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.5f*cv + 0.5f*input[igb]) * cellSize);
			}
			
			//nord-ouest
			if(ly != 0 && lx != (windowSize-1) && gy != 0 && gx != (width-1)){
				ilb = ((ly-1) * windowSize) + lx+1;
				igb = ((gy-1) * width) + gx+1;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.7071f*cv + 0.7071f*input[igb]) * cellSize);
			}
			
			//sud-ouest
			if(ly != (windowSize-1) && lx != (windowSize-1) && gy != (height-1) && gx != (width-1)){
				ilb = ((ly+1) * windowSize) + lx+1;
				igb = ((gy+1) * width) + gx+1;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.7071f*cv + 0.7071f*input[igb]) * cellSize);
			}
			
			//sud-est
			if(ly != (windowSize-1) && lx != 0 && gy != (height-1) && gx != 0){
				ilb = ((ly+1) * windowSize) + lx-1;
				igb = ((gy+1) * width) + gx-1;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.7071f*cv + 0.7071f*input[igb]) * cellSize);
			}
			
			//nord-est
			if(ly != 0 && lx != 0 && gy != 0 && gx != 0){
				ilb = ((ly-1) * windowSize) + lx-1;
				igb = ((gy-1) * width) + gx-1;
				
				functionalWindows[(y * width) + x][ilb] = minF(functionalWindows[(y * width) + x][ilb], crcm + (0.7071f*cv + 0.7071f*input[igb]) * cellSize);
			}
		}
		
		public void applyDiffusion(float[] input, float[][] functionalWindows) {
			this.input = input;
			this.functionalWindows = functionalWindows;
			long begin = System.currentTimeMillis();
			put(this.functionalWindows);
			execute(width*height);
			get(this.functionalWindows);
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
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
