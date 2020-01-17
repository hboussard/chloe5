package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.counting.CoupleCounting;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.GaussianWeightedCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedCountCoupleKernelBis;
import fr.inra.sad.bagap.chloe.kernel.ThresholdCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.ThresholdCountValueKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.couple.CountCoupleMetric;
import fr.inra.sad.bagap.chloe.metric.couple.HeterogeneityIndex;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.metric.value.ShannonDiversityIndex;
import fr.inra.sad.bagap.chloe.output.AsciiGridOutput;
import fr.inra.sad.bagap.chloe.output.CsvOutput;
import fr.inra.sad.bagap.chloe.output.TextImageOutput;
import fr.inra.sad.bagap.chloe.util.Couple;
import java.util.ResourceBundle;


public class CouplesSlidingWindowAnalysis {

	public static void main(final String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("fr.inra.sad.bagap.chloe.properties.config");
		String path_input = bundle.getString("path_input");
		String path_output = bundle.getString("path_output");

		try {
			System.out.println("sliding window");
			
			short windowSize = Short.parseShort(bundle.getString("window_size"));
			short mid = (short) (windowSize/2);
			//int roiWidth = 12090;
			//int roiHeight = 12494;
			//short roiX = 17000;
			//short roiY = 700;
			int roiWidth = Integer.parseInt(bundle.getString("roi_width"));
			int roiHeight = Integer.parseInt(bundle.getString("roi_height"));
			int roiX = Integer.parseInt(bundle.getString("roi_x"));
			int roiY = Integer.parseInt(bundle.getString("roi_y"));
			int dep = Integer.parseInt(bundle.getString("deplacement"));
			int buffer = Integer.parseInt(bundle.getString("buffer_height"));

			
			buffer = (short) Math.max(dep, buffer);
			
			File file = new File(path_input + "bretagne.tif");
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
				
			
			int nodatavalue = Raster.getNoDataValue();
			Set<Short> inValues = new TreeSet<Short>();
			for(float s : inDatas){
				if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains((short) s)){
					inValues.add((short) s);
				}
			}
			System.out.println(inValues);
			short nValues = (short) inValues.size();
			float[] couples = new float[(((inValues.size()*inValues.size())-inValues.size())/2) + inValues.size()];
			int index = 0;
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
			
			float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][couples.length+2];
						
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
							shape[(j * windowSize) + i] = 3;
						}
						if(i>0 && (distance(mid, mid, i-1, j) <= mid)) {
							theoroticalHorizontalCoupleSize++;
							shape[(j * windowSize) + i] = 2;
						}
						if(i>0 && (distance(mid, mid, i-1, j) <= mid) && j>0 && (distance(mid, mid, i, j-1) <= mid)) {
							shape[(j * windowSize) + i] = 4;
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
			
			
			for(int s1=0; s1<windowSize; s1++){
				for(int s2=0; s2<windowSize; s2++){
					System.out.print(shape[s1*windowSize + s2]+" ");	
				}
				System.out.println();
			}
			*/
			DistanceWeightedCountCoupleKernel cv = new DistanceWeightedCountCoupleKernel(couples, (int) windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, nodatavalue);
			//DistanceWeightedCountCoupleKernelBis cv = new DistanceWeightedCountCoupleKernelBis(couples, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, nodatavalue);
			//GaussianWeightedCountCoupleKernel cv = new GaussianWeightedCountCoupleKernel(couples, windowSize, roiWidth, roiHeight, dep, inDatas, outDatas, nodatavalue);
			CoupleCounting vc = new CoupleCounting(nValues, couples, theoriticalCoupleSize);
			
			Metric metric;
			
			metric = new CountCoupleMetric((short) 5, (short) 6);
			metric.addObserver(new AsciiGridOutput(path_output+"image_count_5-6.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			
			vc.addMetric(metric);
			/*
			metric = new HeterogeneityIndex();
			metric.addObserver(new TextImageOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_het.txt", outWidth));
			metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_het.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			vc.addMetric(metric);
			
			for(float c : couples){
				//System.out.println(c);
				metric = new CountCoupleMetric(c);
				metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_"+c+".asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
				
				vc.addMetric(metric);
			}
		
			CsvOutput csvOut = new CsvOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image.csv", outMinX, outMaxY, cellSize, outMaxX, vc.metrics());
			vc.addObserver(csvOut);
			*/
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
						vc.setCounts(outDatas[index]);
						vc.calculate();
						vc.export();
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

	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
}
