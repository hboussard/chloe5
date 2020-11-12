package fr.inra.sad.bagap.chloe.labpse;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.Util;
import fr.inra.sad.bagap.chloe.counting.CoupleCounting;
import fr.inra.sad.bagap.chloe.counting.ValueCounting;
import fr.inra.sad.bagap.chloe.kernel.DistanceWeightedCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.ThresholdCountCoupleKernel;
import fr.inra.sad.bagap.chloe.kernel.ThresholdCountValueKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.metric.couple.CountCoupleMetric;
import fr.inra.sad.bagap.chloe.metric.couple.HeterogeneityFragmentationIndex;
import fr.inra.sad.bagap.chloe.metric.couple.HeterogeneityIndex;
import fr.inra.sad.bagap.chloe.metric.couple.RateCoupleMetric;
import fr.inra.sad.bagap.chloe.metric.value.CountValueMetric;
import fr.inra.sad.bagap.chloe.metric.value.RateValueMetric;
import fr.inra.sad.bagap.chloe.output.CsvOutput;
import fr.inra.sad.bagap.chloe.util.Couple;

public class SudMayenneConfiguration {
	
	public static void main(final String[] args) {
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
			short buffer = 40;
			
			buffer = (short) Math.max(dep, buffer);
			
			System.out.println("lecture de la carte");
			//File file = new File("C:/Users/hboussard/modelisation/ecopaysage/data/image_dreal.asc");
			File file = new File("F:/Requete_SIG_LabPSE/raster/occsol_sm.asc");
			ArcGridReader reader = new ArcGridReader(file);
			//File file = new File("C:/Users/hboussard/data/bretagne.tif");
			//GeoTiffReader reader = new GeoTiffReader(file);
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
			
			int theoriticalCoupleSize;
			int theoroticalVerticalCoupleSize = 0;
			int theoroticalHorizontalCoupleSize = 0;
			for(short j=0; j<windowSize; j++){
				for(short i=0; i<windowSize; i++){
					
					// gestion en cercle
					if(Util.distance(mid, mid, i, j) <= mid){
						shape[(j * windowSize) + i] = 1;
						
						if(j>0 && (Util.distance(mid, mid, i, j-1) <= mid)) {
							theoroticalVerticalCoupleSize++;
							shape[(j * windowSize) + i] = 1;
						}
						if(i>0 && (Util.distance(mid, mid, i-1, j) <= mid)) {
							theoroticalHorizontalCoupleSize++;
							shape[(j * windowSize) + i] = 1;
						}
						if(i>0 && (Util.distance(mid, mid, i-1, j) <= mid) && j>0 && (Util.distance(mid, mid, i, j-1) <= mid)) {
							shape[(j * windowSize) + i] = 1;
						}
						
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
					//float d = (float) Math.exp(-1 * Math.pow(Util.distance(mid, mid, i, j)/(mid/3.0), 2));
					float d = (float) Math.exp(-1 * Math.pow(Util.distance(mid, mid, i, j), 2) / Math.pow(mid/2, 2));
					coeffs[(j * windowSize) + i] = d;
					//System.out.print(d+" ");		
					
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
			
			DistanceWeightedCountCoupleKernel cv = new DistanceWeightedCountCoupleKernel(couples, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas, (float) Raster.getNoDataValue(), 0);
			//ThresholdCountCoupleKernel cv = new ThresholdCountCoupleKernel(couples, windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue(), 0);
			
			CoupleCounting cc = new CoupleCounting(nValues, couples, theoriticalCoupleSize);
			
			Metric metric;
			
			for(float c : couples){
				//metric = new CountCoupleMetric(c);
				//cc.addMetric(metric);
				
				metric = new RateCoupleMetric(c);
				cc.addMetric(metric);
			}
			
			//CsvOutput csvOut = new CsvOutput("F:/Ecopaysage/ecopaysages/data/sliding/gaussian/6km/carte_LTC_GPA_conf_gaussian_6km.csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, (short) Raster.getNoDataValue(), cc.metrics());
			CsvOutput csvOut = new CsvOutput("F:/Requete_SIG_LabPSE/raster/ecopaysage/sud_mayenne_conf_gaussian_3km.csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, (short) Raster.getNoDataValue(), cc.metrics());
			
			cc.addObserver(csvOut);
			
			cc.init();
			
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
						cc.setCounts(outDatas[index]);
						cc.calculate();
						cc.export();
						index++;
					}
				}	
			}
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
			cv.dispose();
			cc.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
