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
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedQuantitativeKernel;
import fr.inra.sad.bagap.chloe.kernel.ThresholdCountValueKernel;
import fr.inra.sad.bagap.chloe.kernel.ThresholdQuantitativeKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.metric.quantitative.AverageMetric;
import fr.inrae.act.bagap.chloe.metric.value.CountValueMetric;
import fr.inrae.act.bagap.chloe.metric.value.RateValueMetric;
import fr.inrae.act.bagap.chloe.output.CsvOutput;

public class DrealGeoChelsa {

	public static void main(final String[] args) {
		scriptEcopaysage("04");
		scriptEcopaysage("05");
		scriptEcopaysage("06");
		scriptEcopaysage("15");
		scriptEcopaysage("16");
		scriptEcopaysage("17");
	}
	
	private static void scriptEcopaysage(String number) {
		try {
			System.out.println("sliding window");
			
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
			
			// intégration de la carte d'entrée
			System.out.println("lecture de la carte");
			File file = new File("C:/Users/hboussard/modelisation/ecopaysage/data/climato/LTC_5m_CHELSA_bio10_"+number+".tif");
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
			Raster.setNoDataValue(-32768);
			
			// définition des paramètres de l'enveloppe externe
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
			
			// récupération des données de l'enveloppe externe
			System.out.println("récupération des données");
			Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
			float[] inDatas = new float[roiWidth*roiHeight];		
			inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
			
				
			// définition de la forme de la fenêtre et des coefficients de pondération par la distance
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
					
					// gestion des distances pondérées (gaussienne centrée à 0)
					//float d = (float) Math.exp(-1 * Math.pow(distance(mid, mid, i, j)/(mid/3.0), 2));
					float d = (float) Math.exp(-1 * Math.pow(distance(mid, mid, i, j), 2) / Math.pow(mid/2, 2));
					coeffs[(j * windowSize) + i] = d;
					//System.out.print(d+" ");		
					
					//System.out.println((float) d / mid);
				}
				//System.out.println();
			}
		
			 
			//for(int s=0; s<(windowSize*windowSize); s++){
			//	shape[s] = 1;
			//}
			 
			//for(int c=0; c<(windowSize*windowSize); c++){
			//	coeffs[c] = 1;
			//}
			
			// définition des données de sorties
			double[][] outDatas = new double[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][3];
			
			//ThresholdQuantitativeKernel cv = new ThresholdQuantitativeKernel(windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas, (short) Raster.getNoDataValue(), (short) 600);
			DistanceWeightedQuantitativeKernel cv = new DistanceWeightedQuantitativeKernel(windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, (short) Raster.getNoDataValue(), 600);
			
			QuantitativeCounting qc = new QuantitativeCounting(theoriticalSize);
			
			Metric metric;
			
			metric = new AverageMetric();
			qc.addMetric(metric);
			
			CsvOutput csvOut = new CsvOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/dreal_quant_"+number+"_weighted_3km.csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, (short) Raster.getNoDataValue(), qc.metrics());
			qc.addObserver(csvOut);
			
			qc.init();
			
			int index = 0;
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
						qc.setCounts(outDatas[index]);
						qc.calculate();
						qc.export();
						index++;
					}
				}	
			}
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
			cv.dispose();
			qc.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
}
