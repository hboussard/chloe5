package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
//import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.kernel.FastGaussianWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.kernel.FastGaussianWeigthedByteCountKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.output.AsciiGridOutput;
import fr.inra.sad.bagap.chloe.output.CsvOutput;

public class FastValuesSlidingWindowAnalysis {
	
	public static void main(final String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("fr.inra.sad.bagap.chloe.properties.config");
		String path_input = bundle.getString("path_input");
		String path_output = bundle.getString("path_output");
		
		try {
			System.out.println("sliding window");
			
			int windowSize = Short.parseShort(bundle.getString("window_size"));
			int mid = windowSize/2;
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
			
			buffer = Math.max(dep, buffer);
			
			File file = new File("c:/Users/pmeurice/Documents/Data/GO_2018_routes_eau_bois.tif");
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
			
			int outWidth = (roiWidth-1)/dep+1;
			int outHeight = (roiHeight-1)/dep+1;
			double outCellSize = cellSize * dep;
			double outMinX = imageMinX + roiX*cellSize;
			double outMaxX = outMinX + outWidth*cellSize;
			double outMinY = imageMinY + (imageHeight-roiY)*cellSize - outHeight*outCellSize;
			double outMaxY = imageMaxY - roiY*cellSize;
			
			Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
			
			byte[] inDatas = new byte[roiWidth*roiHeight];
			inDatas = (byte[]) coverage.getRenderedImage().getData(roi).getDataElements(roi.x, roi.y, roi.width, roi.height, inDatas);
			//System.out.println(pi.getMinX()+" "+pi.getMaxX()+" "+pi.getMinTileX()+" "+pi.getMaxTileX());
				
			
			Set<Byte> inValues = new TreeSet<Byte>();
			for(byte s : inDatas){
				//if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains(s)){
					inValues.add(s);
				//}
			}
			System.out.println(inValues);
			short[] values = new short[inValues.size()];
			int index = 0;
			for(byte s : inValues){
				values[index++] = s;
				System.out.println(s);
			}
			
			float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][values.length+2];
			/*			
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
					float d = mid - distance(mid, mid, i, j);
					if(d < 0){
						d = 0;
					}
					coeffs[(j * windowSize) + i] = (float) d / mid;
					//System.out.println((float) d / mid);
				}
				//System.out.println();
			}
			*/
			/*
			for(int s=0; s<(windowSize*windowSize); s++){
				shape[s] = 1;
			}
			
			for(int c=0; c<(windowSize*windowSize); c++){
				coeffs[c] = 1;
			}
			*/
			
			FastGaussianWeigthedByteCountKernel cv = new FastGaussianWeigthedByteCountKernel(values.length, windowSize, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());

			//GaussianWeigthedCountValueKernel cv = new GaussianWeigthedCountValueKernel(values, windowSize, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());
			//DistanceWeigthedCountValueKernel cv = new DistanceWeigthedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue());
			//ThresholdCountValueKernel cv = new ThresholdCountValueKernel(values, windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas);
			int theoriticalSize = windowSize*windowSize;
			ValueCounting vc = new ValueCounting(values, theoriticalSize);
			
			Metric metric;
			//CsvOutput csvOut = new CsvOutput(path_output+"image.csv", outMinX, outMaxY, cellSize, outMaxX);
			
			metric = new CountValueMetric((short)5);
			//metric.addObserver(new TextImageOutput(path_output+"image_shdi.txt", outWidth));
			metric.addObserver(new AsciiGridOutput(path_output+"testWG.asc", (short)outWidth, (short)outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
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
