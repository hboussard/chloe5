package fr.inra.sad.bagap.chloe.dreal;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.Util;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeigthedCountValueKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.metric.value.RateValueMetric;
import fr.inra.sad.bagap.chloe.output.CsvOutput;

public class GrandOuestComposition {

	public static void main(final String[] args) {
		try {
			System.out.println("sliding window");
			
			// threshold
			// windowSize = 41; --> 100m de rayon
			// windowSize = 101; --> 250m de rayon
			// windowSize = 201; --> 500m de rayon
			// windowSize = 401; --> 1000m de rayon
			// windowSize = 601; --> 1500m de rayon
			// windowSize = 801; --> 2000m de rayon
			// windowSize = 1001; --> 2500m de rayon
			// windowSize = 1201; --> 3000m de rayon
			// windowSize = 1601; --> 4000m de rayon
			// windowSize = 2001; --> 5000m de rayon
			// windowSize = 2401; --> 6000m de rayon
			
			// gaussian
			// windowSize = 81; --> 100m de rayon
			// windowSize = 201; --> 250m de rayon
			// windowSize = 401; --> 500m de rayon
			// windowSize = 801; --> 1000m de rayon
			// windowSize = 1201; --> 1500m de rayon
			// windowSize = 1601; --> 2000m de rayon
			// windowSize = 2001; --> 2500m de rayon
			// windowSize = 2401; --> 3000m de rayon
			// windowSize = 3201; --> 4000m de rayon
			// windowSize = 4001; --> 5000m de rayon
			// windowSize = 4801; --> 6000m de rayon
			
			
			short windowSize = 2401;
			short mid = (short) (windowSize/2);
			//int roiWidth = 12599;
			//int roiHeight = 13063;
			//short roiX = 16507;
			//short roiY = 280;
			short roiX = 0;
			short roiY = 0;
			short dep = 40;
			short buffer = 80;
			
			buffer = (short) Math.max(dep, buffer);
			
			System.out.println("lecture de la carte");
			//File file = new File("C:/Users/hboussard/modelisation/ecopaysage/data/image_dreal.asc");
			//File file = new File("F:/Ecopaysage/emprise_LTC/data/LTC_buffer12km.asc");
			//ArcGridReader reader = new ArcGridReader(file);
			File file = new File("F:/data/sig/grand_ouest/GO_eau_dessus_2018.tif");
			GeoTiffReader reader = new GeoTiffReader(file);
			//System.out.println(reader.getCoordinateReferenceSystem());
			GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			
			int imageWidth = (Integer) coverage.getProperty("image_width");
			int imageHeight = (Integer) coverage.getProperty("image_height");
			double imageMinX = coverage.getEnvelope().getMinimum(0);
			double imageMinY = coverage.getEnvelope().getMinimum(1);
			double imageMaxX = coverage.getEnvelope().getMaximum(0);
			double imageMaxY = coverage.getEnvelope().getMaximum(1);
			double cellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			System.out.println(imageWidth+" "+imageHeight+" "+imageMinX+" "+imageMinY+" "+imageMaxX+" "+imageMaxY+" "+cellSize);
			
			int roiWidth = imageWidth;
			int roiHeight = imageHeight;
			short outWidth = (short) (((roiWidth-1)/dep)+1);
			short outHeight = (short) (((roiHeight-1)/dep)+1);
			double outCellSize = cellSize * dep;
			double outMinX = imageMinX + roiX*cellSize + cellSize/2.0 - outCellSize/2.0;
			double outMaxX = outMinX + outWidth*outCellSize;
			double outMaxY = imageMaxY - roiY*outCellSize - cellSize/2.0 + outCellSize/2.0;
			double outMinY = outMaxY - outHeight*outCellSize;
			
			
			System.out.println(outWidth+" "+outHeight+" "+outMinX+" "+outMinY+" "+outMaxX+" "+outMaxY+" "+outCellSize);
			
			Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
			float[] inDatas = new float[roiWidth*roiHeight];
			System.out.println("récupération des données");
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
					if(Util.distance(mid, mid, i, j) <= mid){
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
					
					// gestion des distances pondérées (gaussienne centrée à 0)
					//float d = (float) Math.exp(-1 * Math.pow(distance(mid, mid, i, j)/(mid/3.0), 2));
					float d = (float) Math.exp(-1 * Math.pow(Util.distance(mid, mid, i, j), 2) / Math.pow(mid/2, 2));
					//exp(-pow(distance, 2)/pow(dmax/2, 2))
					coeffs[(j * windowSize) + i] = d;
					//System.out.print(d+" ");		
					
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
			
			
			
			DistanceWeigthedCountValueKernel cv = new DistanceWeigthedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue(), 0);
			//ThresholdCountValueKernel cv = new ThresholdCountValueKernel(values, windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue(), 0);
			
			ValueCounting vc = new ValueCounting(values, theoriticalSize);
			
			Metric metric;
			
			//metric = new ShannonDiversityIndex();
			//metric.addObserver(new TextImageOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_shdi.txt", outWidth));
			//vc.addMetric(metric);
			
			for(short v : values){
				//metric = new CountValueMetric(v);
				//vc.addMetric(metric);
				metric = new RateValueMetric(v);
				vc.addMetric(metric);
				
				//metric.addObserver(new TextImageOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_"+v+".txt", (short) (((roiWidth-1)/dep)+1)));
				//metric.addObserver(new AsciiGridOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/image_count_"+v+".asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
				
			}
			
			CsvOutput csvOut = new CsvOutput("F:/data/sig/grand_ouest/GO_comp_gaussian_3km.csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, (short) Raster.getNoDataValue(), vc.metrics());
			vc.addObserver(csvOut);
			
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
}
