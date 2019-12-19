package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.aparapi.Kernel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ValuesSlidingWindowAnalysis {

	public static void main(final String[] args) {
		try {
			System.out.println("sliding window");
			
			short windowSize = 51;
			short mid = (short) (windowSize/2);
			//int width = 12090;
			//int height = 12494;
			int width = 1000;
			int height = 1000;
			short dep = 1;
			short buffer = 50;
			
			buffer = (short) Math.max(dep, buffer);
			
			File file = new File("C://Hugues/modelisation/chloe/v5/data/bretagne.tif");
			ImageInputStream stream = ImageIO.createImageInputStream(file);
			ImageReader reader = ImageIO.getImageReaders(stream).next();
			reader.setInput(stream);
			ImageReadParam param = reader.getDefaultReadParam();
			//Rectangle sourceRegion = new Rectangle(17000, 700, width, height); 
			Rectangle sourceRegion = new Rectangle(11000, 11000, width, height); 
			param.setSourceRegion(sourceRegion); // Set region
			BufferedImage inputImage = reader.read(0, param); // Will read only the region specified
			//System.out.println(inputImage);
		

			short[] inDatas = ((DataBufferShort) inputImage.getRaster().getDataBuffer()).getData();
			Set<Short> inValues = new TreeSet<Short>();
			for(short s : inDatas){
				if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains(s)){
					inValues.add(s);
				}
			}
			short[] values = new short[inValues.size()];
			int index = 0;
			for(Short s : inValues){
				values[index++] = (short) s;
			}
			
			float[][] outDatas = new float[((((width-1)/dep)+1)*(((buffer-1)/dep)+1))][values.length+2];
						
			short[] shape = new short[windowSize*windowSize];
			float[] coeffs = new float[windowSize*windowSize];
			
			short theoriticalSize = 0;
			
			for(short j=0; j<windowSize; j++){
				for(short i=0; i<windowSize; i++){
					
					// gestion en cercle
					if(distance(mid, mid, i, j) <= mid){
						shape[(j * windowSize) + i] = 1;
						theoriticalSize++; 
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
			
			CountValueKernel cv = new CountValueKernel(values, windowSize, shape, coeffs, width, height, dep, inDatas, outDatas);
			ValueCounting vc = new ValueCounting(values, theoriticalSize);
			
			Metric metric;
			
			metric = new ShannonDiversityIndex();
			metric.addObserver(new TextImageOutput("C:/Hugues/temp/image_shdi.txt", (short) (((width-1)/dep)+1)));
			vc.addMetric(metric);
			
			for(short v : values){
				metric = new CountValueMetric(v);
				metric.addObserver(new TextImageOutput("C:/Hugues/temp/image_count_"+v+".txt", (short) (((width-1)/dep)+1)));
				vc.addMetric(metric);
			}
			
			int nextJ = 0;
			long begin = System.currentTimeMillis();
			for(int b=0; b<height; b+=buffer){
				System.out.println(b);
				cv.applySlidingWindow(b, (short) Math.min(buffer, (height-b)));
				cv.get(outDatas);
				
				index = 0;
				for(int j=nextJ%buffer; j<Math.min(buffer, height-b); j+=dep){
					nextJ += dep;
					for(int i=0; i<width; i+=dep){
						vc.setCounts(outDatas[index]);
						vc.calculate();
						index++;
					}
				}	
			}
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
			cv.dispose();
			vc.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	final static class CountValueKernel extends Kernel {

		private int width, height;
		
		private int dep;

		private short imageIn[];
		
		private float imageOut[][];
		
		private short[] shape;
		
		private float[] coeff;
		
		private int windowSize;
		
		private short[] values;
		
		private int theY;
		
		public CountValueKernel(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, short[] imageIn, float[][] imageOut){
			this.setExplicit(true);
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
 
		public void processPixel(int x, int y, int localY) {
			if(x%dep == 0 && y%dep == 0){
				
				int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
				
				for(int i=0; i<values.length+2; i++){
					imageOut[ind][i] = 0f;
				}
				
				int mid = windowSize / 2;
				int ic;
				short v;
				/*
				imageOut[ind][0] = imageIn[((y) * width) + (x)];
				imageOut[ind][1] = imageIn[((y) * width) + (x)];
				for(int i=0; i<values.length; i++){
					imageOut[ind][i+2] = imageIn[((y) * width) + (x)];
				}
				*/							
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height)){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width)){
								ic = ((dy+mid) * windowSize) + (dx+mid);
								if(shape[ic] == 1){
									v = imageIn[((y + dy) * width) + (x + dx)];
									
									if(v == -1){
										imageOut[ind][0] = imageOut[ind][0] + coeff[ic];
									}else{
										if(v == 0){
											imageOut[ind][1] = imageOut[ind][1] + coeff[ic];
										}else{
											for(int i=0; i<values.length; i++){
												if(v == values[i]){
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
	
	
}
