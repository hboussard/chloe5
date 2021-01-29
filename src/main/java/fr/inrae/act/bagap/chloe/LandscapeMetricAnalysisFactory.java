package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
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
import fr.inrae.act.bagap.chloe.output.InterpolateSplineLinearAsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.InterpolateSplineLinearCsvOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;

public class LandscapeMetricAnalysisFactory {

	public static LandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder) throws IOException {
		
		// displacement
		int displacement = builder.getDisplacement();
		
		// metriques
		Set<Metric> metrics = builder.getMetrics();
		
		// ROI
		int bufferROI = builder.getBufferROI();
		
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
		
		int roiX = 0;
		int roiY = 0;
		int roiWidth = inWidth - 2*bufferROI;
		int roiHeight = inHeight - 2*bufferROI;
		
		int outWidth = (int) (((roiWidth-1)/displacement)+1);
		int outHeight = (int) (((roiHeight-1)/displacement)+1);
		double outCellSize = inCellSize * displacement;
		double outMinX = inMinX + (roiX+bufferROI)*inCellSize + inCellSize/2.0 - outCellSize/2.0;
		double outMaxX = outMinX + outWidth*outCellSize;
		double outMaxY = inMaxY - (roiY+bufferROI)*inCellSize - inCellSize/2.0 + outCellSize/2.0;
		double outMinY = outMaxY - outHeight*outCellSize;
		
		// recuperation des donnees
		Rectangle roi = new Rectangle(roiX, roiY, roiWidth + 2*bufferROI, roiHeight + 2*bufferROI);
		float[] inDatas = new float[(roiWidth + 2*bufferROI)*(roiHeight + 2*bufferROI)];
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
		
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
		
		// observers
		Set<CountingObserver> observers = builder.getObservers();
		if(builder.getCsv() != null){
			if(builder.getDisplacement() == 1 || builder.getInterpolation() == false){
				CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, Raster.getNoDataValue());
				observers.add(csvOutput);
			}else{
				//InterpolateSplineLinearCsvOutput csvOutput = new InterpolateSplineLinearCsvOutput(builder.getCsv(), imageMinX, imageMaxX, imageMinY, imageMaxY, roiWidth, roiHeight, cellSize, Raster.getNoDataValue(), displacement);
				//observers.add(csvOutput);
			}
		}
		for(Entry<String, String> entry : builder.getAsciiOutputs().entrySet()){
			Metric metric = null;
			for(Metric m : metrics){
				if(m.getName().equalsIgnoreCase(entry.getKey())){
					metric = m;
					break;
				}
			}
			if(builder.getDisplacement() == 1 || builder.getInterpolation() == false){
				AsciiGridOutput asciiOutput = new AsciiGridOutput(entry.getValue(), metric, outWidth, outHeight, outMinX, outMinY, outCellSize, Raster.getNoDataValue());
				observers.add(asciiOutput);
			}else{
				//InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(entry.getValue(), metric, roiWidth, roiHeight, imageMinX, imageMinY, cellSize, Raster.getNoDataValue(), displacement);
				//observers.add(asciiOutput);
			}	
		}
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){
			
			LandscapeMetricKernel kernel = null;
			if(metrics.size() == 1 && MetricManager.hasMetric(metrics, "MD")){
				kernel = new DistanceWeightedQuantitativeKernel(windowSize, shape, coeffs, roiWidth, roiHeight, displacement, inDatas, Raster.getNoDataValue(), 100);
			}else{
				kernel = new DistanceWeightedQuantitativeKernel(windowSize, shape, coeffs, roiWidth, roiHeight, displacement, inDatas, Raster.getNoDataValue());
			}
			Counting counting = new QuantitativeCounting(theoreticalSize);
			
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
			
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
			
			// analysis
			return new QuantitativeLandscapeMetricAnalysis(roiWidth, roiHeight, displacement, kernel, counting);
			
		}else{
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
			
			// recuperation des couples
			float[] couples = null;
			if(MetricManager.hasCoupleMetric(metrics)){
				couples = new float[(((inValues.size()*inValues.size())-inValues.size())/2) + inValues.size()];
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
			if(MetricManager.hasOnlyCoupleMetric(metrics)){
				nbValues = couples.length;
				kernel = new DistanceWeightedCountCoupleKernel(couples, windowSize, shape, coeffs, roiWidth, roiHeight, displacement, inDatas, Raster.getNoDataValue(), bufferROI);
				counting = new CoupleCounting(inValues.size(), couples, theoreticalCoupleSize);
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
			return new QualitativeLandscapeMetricAnalysis(roiWidth, roiHeight, nbValues, displacement, kernel, counting);
		}
		
		/*
		// gestion du selected
		if(builder.getAnalysisType() == WindowAnalysisType.SELECTED){
			Set<Pixel> pixels = new TreeSet<Pixel>();
			// TODO
		}
		*/
		
		
	}

	
}
