package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.counting.CoupleCounting;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedCountValueAndCoupleKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.couple.CountCoupleMetric;
import fr.inra.sad.bagap.chloe.metric.couple.HeterogeneityIndex;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.metric.value.ShannonDiversityIndex;
import fr.inra.sad.bagap.chloe.output.AsciiGridOutput;
import fr.inra.sad.bagap.chloe.output.CsvOutput;
import fr.inra.sad.bagap.chloe.output.TextImageOutput;
import fr.inra.sad.bagap.chloe.util.Couple;

public class ValuesAndCouplesSlidingWindowAnalysis {
	
	public static void main(final String[] args) {
		try {
			System.out.println("sliding window");
			
			short windowSize = 51;
			short mid = (short) (windowSize/2);
			//int roiWidth = 12090;
			//int roiHeight = 12494;
			//short roiX = 17000;
			//short roiY = 700;
			int roiWidth = 1000;
			int roiHeight = 1000;
			short roiX = 10000;
			short roiY = 10000;
			short dep = 1;
			short buffer = 500;
			
			buffer = (short) Math.max(dep, buffer);
			
			File file = new File("C:/Users/hboussard/data/bretagne.tif");
			GeoTiffReader reader = new GeoTiffReader(file);
			//System.out.println(reader.getCoordinateReferenceSystem());
			GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			
			int imageWidth = (Integer) coverage.getProperty("image_width");
			int imageHeight = (Integer) coverage.getProperty("image_height");
			double imageMinX = coverage.getEnvelope().getMinimum(0);
			double imageMinY = coverage.getEnvelope().getMinimum(1);
			double imageMaxY = coverage.getEnvelope().getMaximum(1);
			double cellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			System.out.println("cellsize : "+cellSize);
			System.out.println(coverage.getEnvelope());
			System.out.println(coverage.getEnvelope().getMinimum(0)+" "+coverage.getEnvelope().getMaximum(0)+" "+coverage.getEnvelope().getMinimum(1)+" "+coverage.getEnvelope().getMaximum(1));
			System.out.println(coverage.getProperties());
			
			short outWidth = (short) (((roiWidth-1)/dep)+1);
			short outHeight = (short) (((roiHeight-1)/dep)+1);
			double outCellSize = cellSize * dep;
			double outMinX = imageMinX + roiX*cellSize;
			double outMaxX = outMinX + outWidth*cellSize;
			double outMinY = imageMinY + (imageHeight-roiY)*cellSize - outHeight*outCellSize;
			double outMaxY = imageMaxY - roiY*cellSize;
			
			Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
			
			float[] inDatas = new float[roiWidth*roiHeight];
			inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
			//System.out.println(pi.getMinX()+" "+pi.getMaxX()+" "+pi.getMinTileX()+" "+pi.getMaxTileX());
				
			
			Set<Short> inValues = new TreeSet<Short>();
			for(float s : inDatas){
				if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains((short) s)){
					inValues.add((short) s);
				}
			}
			System.out.println(inValues);
			short[] values = new short[inValues.size()];
			int index = 0;
			for(Short s : inValues){
				values[index++] = (short) s;
			}
			
			short nValues = (short) inValues.size();
			float[] couples = new float[(((inValues.size()*inValues.size())-inValues.size())/2) + inValues.size()];
			index = 0;
			for(Short s1 : inValues){
				couples[index++] = Couple.getCouple((short) s1, (short) s1);
			}
			Set<Short> ever = new HashSet<Short>();
			for(Short s1 : inValues){
				ever.add(s1);
				for(Short s2 : inValues){
					if(!ever.contains(s2)) {
						couples[index++] = Couple.getCouple((short) s1, (short) s2);
					}
				}
			}
			
			float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][values.length+2+couples.length+2];
						
			short[] shape = new short[windowSize*windowSize];
			float[] coeffs = new float[windowSize*windowSize];
			
			int theoriticalSize = 0;
			int theoriticalCoupleSize;
			int theoroticalVerticalCoupleSize = 0;
			int theoroticalHorizontalCoupleSize = 0;
			for(short j=0; j<windowSize; j++){
				for(short i=0; i<windowSize; i++){
					
					// gestion en cercle
					if(distance(mid, mid, i, j) <= mid){
						shape[(j * windowSize) + i] = 1;
						theoriticalSize++; 
						
						if(j>0 && (distance(mid, mid, i, j-1) <= mid)) {
							theoroticalVerticalCoupleSize++;
						}
						if(i>0 && (distance(mid, mid, i-1, j) <= mid)) {
							theoroticalHorizontalCoupleSize++;
						}
						
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
			
			theoriticalCoupleSize = theoroticalVerticalCoupleSize + theoroticalHorizontalCoupleSize;
			
			/*
			for(int s=0; s<(windowSize*windowSize); s++){
				shape[s] = 1;
			}
			
			for(int c=0; c<(windowSize*windowSize); c++){
				coeffs[c] = 1;
			}
			*/
			
			DistanceWeightedCountValueAndCoupleKernel cv = new DistanceWeightedCountValueAndCoupleKernel(values, couples, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas);
			/*
			ValueCounting vc = new ValueCounting(values, theoriticalSize); 
			//CoupleCounting cc = new CoupleCounting(nValues, couples, theoriticalCoupleSize);
			
			Metric metric;
			
			metric = new CountValueMetric((short) 5);
			metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_5.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			
			vc.addMetric(metric);
			*/
			CoupleCounting vc = new CoupleCounting(nValues, couples, theoriticalCoupleSize);
			
			Metric metric;
			
			metric = new CountCoupleMetric((short) 5, (short) 5);
			metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_5-5.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			
			vc.addMetric(metric);
			
			/*
			metric = new ShannonDiversityIndex();
			metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_shdi.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			vc.addMetric(metric);
			
			metric = new HeterogeneityIndex();
			//metric.addObserver(new TextImageOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_het.txt", outWidth));
			metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_het.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			cc.addMetric(metric);
			
			for(short v : values){
				metric = new CountValueMetric(v);
				metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_"+v+".asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
				
				vc.addMetric(metric);
			}
			
			for(float c : couples){
				metric = new CountCoupleMetric(c);
				metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_"+c+".asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
				
				cc.addMetric(metric);
			}
			*/
			//CsvOutput csvOut = new CsvOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image.csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, cellSize, (short) Raster.getNoDataValue(), cc.metrics());
			//cc.addObserver(csvOut);
			
			//cc.init();
			vc.init();
			
			int nextJ = 0;
			long begin = System.currentTimeMillis();
			for(int b=0; b<roiHeight; b+=buffer){
				System.out.println(b);
				cv.applySlidingWindow(b, (short) Math.min(buffer, (roiHeight-b)));
				cv.get(outDatas);
				
				index = 0;
				for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight-b); j+=dep){
					//System.out.println(j);
					nextJ += dep;
					for(int i=0; i<roiWidth; i+=dep){
						
						vc.setCounts(Arrays.copyOfRange(outDatas[index], 0, values.length+2));
						vc.calculate();
						vc.export();
						/*
						cc.setCounts(Arrays.copyOfRange(outDatas[index], values.length+2, outDatas[index].length));
						cc.calculate();
						cc.export();
						*/
						index++;
					}
				}	
			}
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
			cv.dispose();
			//cc.close();
			vc.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}

}
