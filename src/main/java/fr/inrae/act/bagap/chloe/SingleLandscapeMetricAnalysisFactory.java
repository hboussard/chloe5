package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.MultipleCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeightedQuantitativeKernel;
import fr.inrae.act.bagap.chloe.kernel.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.CsvOutput;
import fr.inrae.act.bagap.chloe.output.InterpolateSplineLinearAsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.InterpolateSplineLinearCsvOutput;
import fr.inrae.act.bagap.chloe.output.InterpolateSplineLinearTabOutput;
import fr.inrae.act.bagap.chloe.output.TabOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;

public class SingleLandscapeMetricAnalysisFactory {

	//public static SlidingLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, GridCoverage2D coverage) throws IOException {
	public static SlidingLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		if(builder.getAnalysisType() == WindowAnalysisType.SLIDING){
			/*
			int inWidth = (Integer) coverage.getProperty("image_width");
			int inHeight = (Integer) coverage.getProperty("image_height");
			double inMinX = coverage.getEnvelope().getMinimum(0);
			double inMinY = coverage.getEnvelope().getMinimum(1);
			double inMaxX = coverage.getEnvelope().getMaximum(0);
			double inMaxY = coverage.getEnvelope().getMaximum(1);
			double inCellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			*/
			int inWidth = coverage.width();
			int inHeight = coverage.height();
			double inMinX = coverage.minx();
			double inMinY = coverage.miny();
			double inMaxX = coverage.maxx();
			double inMaxY = coverage.maxy();
			double inCellSize = coverage.cellsize();
			
			// displacement
			int displacement = builder.getDisplacement();
			
			// ROI
			int roiX = builder.getROIX();
			int roiY = builder.getROIY();
			int roiWidth = builder.getROIWidth();
			if(roiWidth == -1){
				roiWidth = inWidth;
			}
			int roiHeight = builder.getROIHeight();
			if(roiHeight == -1){
				roiHeight = inHeight;
			}
			
			// taille de sortie
			int outWidth = (int) (((roiWidth-1)/displacement)+1);
			int outHeight = (int) (((roiHeight-1)/displacement)+1);
			double outCellSize = inCellSize * displacement;
			double outMinX = inMinX + roiX*inCellSize + inCellSize/2.0 - outCellSize/2.0;
			double outMaxX = outMinX + outWidth*outCellSize;
			double outMaxY = inMaxY - roiY*inCellSize - inCellSize/2.0 + outCellSize/2.0;
			double outMinY = outMaxY - outHeight*outCellSize;
			
			// recuperation des donnees à déléguer à l'analyse elle-même
			/*
			Rectangle roi = new Rectangle(0, 0, inWidth, inHeight);
			float[] inDatas = new float[(inWidth)*(inHeight)];
			inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
			coverage.dispose(true); // liberation des ressources, à voir si ça marche comme ça
			*/
			
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
			int midWindowSize = (int) (windowSize/2);
			
			// buffer ROI
			//int bufferROIXMin = (roiX > midWindowSize)?midWindowSize:roiX;
			//int bufferROIXMax = (inWidth-(roiX+roiWidth) > midWindowSize)?midWindowSize:inWidth-(roiX+roiWidth);
			//int bufferROIYMin = (roiY > midWindowSize)?midWindowSize:roiY;
			//int bufferROIYMax = (inHeight-(roiY+roiHeight) > midWindowSize)?midWindowSize:inHeight-(roiY+roiHeight);
			
			int bufferROIXMin = Math.min(roiX, midWindowSize);
			int bufferROIXMax = Math.min(inWidth-(roiX+roiWidth), midWindowSize);
			int bufferROIYMin = Math.min(roiY, midWindowSize);
			int bufferROIYMax = Math.min(inHeight-(roiY+roiHeight), midWindowSize);
		
			// window shape and distance
			short[] shape = new short[windowSize*windowSize];
			float[] coeffs = new float[windowSize*windowSize];
					
			int theoreticalSize = 0;
			int theoreticalCoupleSize = 0;
			
			double dMax = midWindowSize * inCellSize;
			DistanceFunction function = CombinationExpressionFactory.createDistanceFunction(builder.getWindowDistanceFunction(), dMax);
									
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
					//float d = (float) Math.exp(-1 * Math.pow(Util.distance(midWindowSize, midWindowSize, i, j), 2) / Math.pow(midWindowSize/2, 2));
					float d = (float) function.interprete(Util.distance(midWindowSize, midWindowSize, i, j)*inCellSize);
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
			if(builder.getCsv() != null){
				if(builder.getDisplacement() == 1 || builder.getInterpolation() == false){
					//CsvOutput2 csvOutput = new CsvOutput2(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, Raster.getNoDataValue());
					CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, Raster.getNoDataValue());
					observers.add(csvOutput);
				}else{
					InterpolateSplineLinearCsvOutput csvOutput = new InterpolateSplineLinearCsvOutput(builder.getCsv(), inMinX + (roiX * inCellSize), inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize), inMinY + ((inHeight -roiY) * inCellSize) - (roiHeight * inCellSize), inMaxY - (roiY * inCellSize), roiWidth, roiHeight, inCellSize, Raster.getNoDataValue(), displacement);
					observers.add(csvOutput);
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
					InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(entry.getValue(), metric, roiWidth, roiHeight, inMinX + (roiX * inCellSize), inMinY + ((inHeight -roiY) * inCellSize) - (roiHeight * inCellSize), inCellSize, Raster.getNoDataValue(), displacement);
					observers.add(asciiOutput);
				}	
			}
			for(Entry<String, float[]> entry : builder.getTabOutputs().entrySet()){
				Metric metric = null;
				for(Metric m : metrics){
					if(m.getName().equalsIgnoreCase(entry.getKey())){
						metric = m;
						break;
					}
				}
				if(builder.getDisplacement() == 1 || builder.getInterpolation() == false){
					TabOutput tabOutput = new TabOutput(entry.getValue(), metric, outWidth);
					observers.add(tabOutput);
				}else{
					InterpolateSplineLinearTabOutput tabOutput = new InterpolateSplineLinearTabOutput(entry.getValue(), metric, roiWidth, displacement);
					observers.add(tabOutput);
				}	
			}
			
			// gestion specifiques des analyses quantitatives ou qualitatives
			if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
				
				SlidingLandscapeMetricKernel kernel = null;
				if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("MD")){
					kernel = new DistanceWeightedQuantitativeKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), 100);
				}else{
					kernel = new DistanceWeightedQuantitativeKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue());
				}
				Counting counting = new QuantitativeCounting(0, 6, theoreticalSize);
				
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
				
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
				
				// analysis
				//return new QuantitativeLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, displacement, kernel, counting);
				return new SingleLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, 6, displacement, kernel, counting);
				
			}else{ // qualitative
				// recuperation des valeurs
				short[] values = builder.getValues();
				if(values == null){
					float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
					Set<Float> inValues = new HashSet<Float>();
					for(float d : datas){
						inValues.add(d);
					}
					int index = 0;
					values = new short[inValues.size()];
					for(float d : inValues){
						values[index++] = (short) d;
					}
				}
				
				// recuperation des couples
				float[] couples = null;
				if(MetricManager.hasCoupleMetric(metrics)){
					couples = new float[(((values.length*values.length)-values.length)/2) + values.length];
					int index = 0;
					for(short s1 : values){
						couples[index++] = Couple.getCouple(s1, s1);
					}
					for(short s1 : values){
						for(short s2 : values){
							if(s1 < s2) {
								couples[index++] = Couple.getCouple(s1, s2);
							}
						}
					}
				}
				
				// kernel et counting
				SlidingLandscapeMetricKernel kernel = null;
				Counting counting;
				
				int nbValues = 2;
				if(MetricManager.hasOnlyValueMetric(metrics)){
					nbValues += values.length;
					kernel = new DistanceWeightedCountValueKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), values);
					counting = new ValueCounting(0, nbValues, values, theoreticalSize);
					
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
					
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
				}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
					nbValues += couples.length;
					kernel = new DistanceWeightedCountCoupleKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), values);
					counting = new CoupleCounting(0, nbValues, values.length, couples, theoreticalCoupleSize);
					
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
					
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
				}else{
					nbValues += values.length + 2 + couples.length;
					
					Counting[] countings = new Counting[2];
					
					kernel = new DistanceWeightedCountValueAndCoupleKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), values);
					ValueCounting vCounting = new ValueCounting(0, values.length+2, values, theoreticalSize);
					// add metrics to counting
					for(Metric m : metrics){
						if(MetricManager.isValueMetric(m.getName())){
							vCounting.addMetric(m);
						}
					}
					countings[0] = vCounting;
				
					CoupleCounting cCounting = new CoupleCounting(values.length+2, nbValues, values.length, couples, theoreticalCoupleSize);
					// add metrics to counting
					for(Metric m : metrics){
						if(MetricManager.isCoupleMetric(m.getName())){
							cCounting.addMetric(m);
						}
					}
					countings[1] = cCounting;
					
					counting = new MultipleCounting(0, nbValues, countings); 
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
				}
				
				// analysis
				return new SingleLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			}
		}
		
		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognize analysis type");
		
	}
	
}
