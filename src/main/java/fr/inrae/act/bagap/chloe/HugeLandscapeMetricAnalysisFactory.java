package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedQuantitativeKernel;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.CsvOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;

public class HugeLandscapeMetricAnalysisFactory {

public static LandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder) throws IOException {
		
		// coverage et infos associées
		GridCoverage2DReader reader;
		if(builder.getRaster().endsWith(".asc")){
			File file = new File(builder.getRaster());
			reader = new ArcGridReader(file);
		}else if(builder.getRaster().endsWith(".tif")){
			File file = new File(builder.getRaster());
			reader = new GeoTiffReader(file);
		}else{
			throw new IllegalArgumentException(builder.getRaster()+" is not a recognize raster");
		}
		GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		int inWidth = (Integer) coverage.getProperty("image_width");
		int inHeight = (Integer) coverage.getProperty("image_height");
		double inMinX = coverage.getEnvelope().getMinimum(0);
		double inMinY = coverage.getEnvelope().getMinimum(1);
		double inMaxX = coverage.getEnvelope().getMaximum(0);
		double inMaxY = coverage.getEnvelope().getMaximum(1);
		double inCellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		// displacement
		int displacement = builder.getDisplacement();
		
		// windowSize
		int windowSize = -1;
		if(builder.getWindowSize() > 0){
			windowSize = builder.getWindowSize();
		}else if(builder.getWindowRadius() > 0){
			double v = (2 * builder.getWindowRadius() / inCellSize);
			windowSize = v%2==0?new Double(v+1).intValue():new Double(v).intValue();
		}else{
			throw new IllegalArgumentException("windowSize must be defined");
		}
		int midWindowSize = (short) (windowSize/2);
				
		// ROI - a definir en fonction de la taille de fenêtre --> moitie de la fenetre
		int bufferROI = midWindowSize;
		
		// cellSize de sortie
		double outCellSize = inCellSize * displacement;
		
		// taille de tuile
		int tileSize = 500; // à priori
		
		List<QualitativeLandscapeMetricAnalysis> analyses = new ArrayList<QualitativeLandscapeMetricAnalysis>();
		
		// mise en place du tuilage
		for(int roiY=0; roiY<inHeight; roiY+=tileSize){
			for(int roiX=0; roiX<inWidth; roiX+=tileSize){
				//int roiWidth = Math.min(tileSize, );
				//int roiHeight = Math.min(tileSize, );
				int roiWidth = tileSize;
				int roiHeight = tileSize;
				int outWidth = (int) (((roiWidth-1)/displacement)+1);
				int outHeight = (int) (((roiHeight-1)/displacement)+1);
				
				double outMinX = inMinX + roiX*inCellSize + inCellSize/2.0 - outCellSize/2.0;
				double outMaxX = outMinX + outWidth*outCellSize;
				double outMaxY = inMaxY - roiY*inCellSize - inCellSize/2.0 + outCellSize/2.0;
				double outMinY = outMaxY - outHeight*outCellSize;
				
				// recuperation des donnees
				Rectangle roi = new Rectangle(roiX - bufferROI, roiY - bufferROI, roiWidth + 2*bufferROI, roiHeight + 2*bufferROI);
				float[] inDatas = new float[(roiWidth + 2*bufferROI)*(roiHeight + 2*bufferROI)];
				inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
				
				// window shape and distance
				short[] shape = new short[windowSize*windowSize];
				float[] coeffs = new float[windowSize*windowSize];
						
				int theoreticalSize = 0;
				int theoreticalCoupleSize = 0;
				for(int j=0; j<windowSize; j++){
					for(int i=0; i<windowSize; i++){
						// gestion en cercle
						if(Util.distance(midWindowSize, midWindowSize, i, j) <= midWindowSize){
							shape[(j * windowSize) + i] = 1;
							theoreticalSize++; 
							if(j>0 && (Util.distance(midWindowSize, midWindowSize, i, j-1) <= midWindowSize)) {
								theoreticalCoupleSize++;
							}
							if(i>0 && (Util.distance(midWindowSize, midWindowSize, i-1, j) <= midWindowSize)) {
								theoreticalCoupleSize++;
							}
						}else{
							shape[(j * windowSize) + i] = 0;
						}
								
						// gestion des distances pondérées (gaussienne centrée à 0)
						//exp(-pow(distance, 2)/pow(dmax/2, 2))
						float d = (float) Math.exp(-1 * Math.pow(Util.distance(midWindowSize, midWindowSize, i, j), 2) / Math.pow(midWindowSize/2, 2));
						coeffs[(j * windowSize) + i] = d;
					}
				}
					
				if(builder.getWindowShapeType() == WindowShapeType.SQUARE){
					for(int s=0; s<(windowSize*windowSize); s++){
						shape[s] = 1;
					}
				}
						 
				if(builder.getWindowDistanceType() == WindowDistanceType.THRESHOLD){
					for(int c=0; c<(windowSize*windowSize); c++){
						coeffs[c] = 1;
					}
				}
				
				// metriques
				Set<Metric> metrics = builder.getMetrics();
				
				// observers
				Set<CountingObserver> observers = builder.getObservers();
				if(builder.getDisplacement() == 1 || builder.getInterpolation() == false){
					CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, Raster.getNoDataValue());
					observers.add(csvOutput);
				}
				for(Entry<String, String> entry : builder.getAsciiOutputs().entrySet()){
					Metric metric = null;
					for(Metric m : metrics){
						if(m.getName().equalsIgnoreCase(entry.getKey())){
							metric = m;
							break;
						}
					}	
				}
				
				// recuperation des valeurs
				Set<Short> inValues = new TreeSet<Short>();
				for(float s : inDatas){
					if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains((short) s)){
						inValues.add((short) s);
					}
				}
				short[] values = null;
				if(MetricManager.hasValueMetric(metrics)){
					values = new short[inValues.size()];
					int index = 0;
					for(Short s : inValues){
						values[index++] = (short) s;
					}
				}
					
				
				// kernel et counting
				LandscapeMetricKernel kernel = null;
				Counting counting = null;
				int nbValues = 0;
				if(MetricManager.hasOnlyValueMetric(metrics)){
					nbValues = values.length;
					kernel = new DistanceWeightedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, displacement, inDatas, Raster.getNoDataValue(), bufferROI);
					counting = new ValueCounting(values, theoreticalSize);
				}
				
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
				// analysis
				analyses.add(new QualitativeLandscapeMetricAnalysis(roiWidth, roiHeight, nbValues, displacement, kernel, counting));
				
			}
		}
		
		return new HugeQualitativeLandscapeMetricAnalysis(analyses);
		
	}

}
