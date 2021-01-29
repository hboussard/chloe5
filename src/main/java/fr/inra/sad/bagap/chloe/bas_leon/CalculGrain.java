package fr.inra.sad.bagap.chloe.bas_leon;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.kernel.ThresholdGrainKernel;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.output.GeoTiffOutput;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeigthedCountValueKernel;
import fr.inrae.act.bagap.chloe.metric.quantitative.AverageMetric;
import fr.inrae.act.bagap.chloe.metric.value.RateValueMetric;
import fr.inrae.act.bagap.chloe.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.util.Util;

public class CalculGrain {

	public static void main(final String[] args) {
		try {
			System.out.println("sliding window");
			long begin = System.currentTimeMillis();
			short windowSize = 141;
			short mid = (short) (windowSize/2);
			short roiX = 0;
			short roiY = 0;
			short dep = 1;
			short buffer = 80;
			
			buffer = (short) Math.max(dep, buffer);
			
			System.out.println("lecture de la carte");
			File file = new File("F:/bas_leon/data/grain/distance_bois.asc");
			ArcGridReader reader = new ArcGridReader(file);
			//File file = new File("F:/Requete_SIG_LabPSE/vallee_de_la_seiche/data/raster/distance_bocage.tif");
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
			
			int index = 0;
			/*
			Set<Short> inValues = new TreeSet<Short>();
			for(float s : inDatas){
				if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains((short) s)){
					inValues.add((short) s);
				}
			}
			*/
			/*
			//System.out.println(inValues);
			short[] values = new short[inValues.size()];
			
			for(Short s : inValues){
				values[index++] = (short) s;
			}
			*/
			
			float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][3];
						
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
					
					// gestion des distances pondérées (gaussienne centrée à 0)
					//formula is exp(-pow(distance, 2)/pow(dmax/2, 2))
					float d = (float) Math.exp(-1 * Math.pow(Util.distance(mid, mid, i, j), 2) / Math.pow(mid/2, 2));
					
					coeffs[(j * windowSize) + i] = d;
				}
			}
		
			/*
			for(int s=0; s<(windowSize*windowSize); s++){
				shape[s] = 1;
			}
			*/
			 
			for(int c=0; c<(windowSize*windowSize); c++){
				coeffs[c] = 1;
			}
			
			ThresholdGrainKernel cv = new ThresholdGrainKernel(windowSize, shape, roiWidth, roiHeight, dep, inDatas, outDatas, Raster.getNoDataValue(), 0);
			
			QuantitativeCounting qc = new QuantitativeCounting(theoriticalSize);
			
			Metric metric = new AverageMetric();
			qc.addMetric(metric);
			
			metric.addObserver(new AsciiGridOutput("F:/bas_leon/data/grain/grain.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			//metric.addObserver(new AsciiGridOutput("F:/temp/roxane/wetransfer-6db38c/grain.asc", outWidth, outHeight, outMinX, outMinY, outCellSize, (short) Raster.getNoDataValue()));
			//metric.addObserver(new GeoTiffOutput("F:/dispositif_bocage/dpt22/grain/bocage/grain_bocage_dpt22.tif", outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize));
			
			qc.init();
			
			int nextJ = 0;
			
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
}
