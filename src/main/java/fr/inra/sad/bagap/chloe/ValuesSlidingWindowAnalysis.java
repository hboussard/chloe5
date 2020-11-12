package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
//import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.kernel.GaussianWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.kernel.TestDistanceWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.output.AsciiGridOutput;
import fr.inra.sad.bagap.chloe.output.CsvOutput;

public class ValuesSlidingWindowAnalysis {
	
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
			short roiX = 1000;
			short roiY = 1000;
			short dep = 1;
			short buffer = 500;
			
			buffer = (short) Math.max(dep, buffer);
			
			//File file = new File(path_input+"bretagne.tif");
			//GeoTiffReader reader = new GeoTiffReader(file);
			//System.out.println(reader.getCoordinateReferenceSystem());
			//GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			
			File file = new File("C:/Hugues/temp/dreal/dreal_leguer_2.asc");
			ArcGridReader reader = new ArcGridReader(file);
			//File file = new File("C:/Users/hboussard/data/bretagne.tif");
			//GeoTiffReader reader = new GeoTiffReader(file);
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
			
			float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][values.length+2];
						
			short[] shape = new short[windowSize*windowSize];
			float[] coeffs = new float[windowSize*windowSize];
			
			int theoriticalSize = 0;
			
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
					//float d = mid - distance(mid, mid, i, j);
					//if(d < 0){
					//	d = 0;
					//}
					//coeffs[(j * windowSize) + i] = (float) d / mid;
					float d = (float) Math.exp(-1 * Math.pow(Util.distance(mid, mid, i, j), 2) / Math.pow(mid/2, 2));
					//exp(-pow(distance, 2)/pow(dmax/2, 2))
					coeffs[(j * windowSize) + i] = d;
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
			
			//TestDistanceWeigthedCountValueKernel cv = new TestDistanceWeigthedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());
			DistanceWeigthedCountValueKernel cv = new DistanceWeigthedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());
			//GaussianWeigthedCountValueKernel cv = new GaussianWeigthedCountValueKernel(values, windowSize, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());
			//ThresholdCountValueKernel cv = new ThresholdCountValueKernel(values, windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas);
			
			ValueCounting vc = new ValueCounting(values, theoriticalSize);
			
			Metric metric;
			//CsvOutput csvOut = new CsvOutput(path_output+"image.csv", outMinX, outMaxY, cellSize, outMaxX);
			
			metric = new CountValueMetric((short)5);
			//metric.addObserver(new TextImageOutput(path_output+"image_shdi.txt", outWidth));
			metric.addObserver(new AsciiGridOutput(path_output+"test.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			//csvOut.addMetric(metric);
			vc.addMetric(metric);
			/*
			for(short v : values){
				metric = new CountValueMetric(v);
				metric.addObserver(new TextImageOutput(path_output+"image_count_"+v+".txt", (short) (((roiWidth-1)/dep)+1)));
				metric.addObserver(new AsciiGridOutput(path_output+"image_count_"+v+".asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
				vc.addMetric(metric);
			}
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
					
					nextJ += dep;
					for(int i=0; i<roiWidth; i+=dep){
						//System.out.println(i+" "+j);
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
