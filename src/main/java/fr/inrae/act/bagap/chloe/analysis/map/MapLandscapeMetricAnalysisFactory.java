package fr.inrae.act.bagap.chloe.analysis.map;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.map.MapCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapPatchKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapQuantitativeKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.MapCsvOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class MapLandscapeMetricAnalysisFactory {


	public MapLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inCellSize = coverage.cellsize();
			
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
			
		// buffer ROI
		int bufferROIXMin = 0;
		int bufferROIXMax = 0;
		int bufferROIYMin = 0;
		int bufferROIYMax = 0;
		
		// tailles theoriques
		int theoreticalSize = 0;
		int theoreticalCoupleSize = 0;
		for(int j=0; j<roiHeight; j++){
			for(int i=0; i<roiWidth; i++){
					
				theoreticalSize++;
				if(j>0) {
					theoreticalCoupleSize++;
				}
				if(i>0) {
					theoreticalCoupleSize++;
				}
			}
		}
		
		// metriques
		Set<Metric> metrics = builder.getMetrics();
			
		// observers
		Set<CountingObserver> observers = builder.getObservers();
		
		if(builder.getCsv() != null){
			String name = new File(builder.getRasterFile()).getName();
			MapCsvOutput csvOutput = new MapCsvOutput(builder.getCsv(), name);
			observers.add(csvOutput);	
		}
			
		// kernel et counting
		MapLandscapeMetricKernel kernel = null;
		Counting counting = null;
		int nbValues = -1;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			nbValues = 8;
			
			kernel = new MapQuantitativeKernel(Raster.getNoDataValue());
				
			counting = new QuantitativeCounting(0, nbValues, theoreticalSize);
				
		}else{
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight));
			}

			if(MetricManager.hasOnlyQualitativeMetric(metrics)){ // qualitative
				
				// recuperation des couples
				float[] couples = null;
				if(MetricManager.hasCoupleMetric(metrics)){
					couples = new float[(((values.length*values.length)-values.length)/2) + values.length];
					int index = 0;
					for(int s1 : values){
						couples[index++] = Couple.getCouple(s1, s1);
					}
					for(int s1 : values){
						for(int s2 : values){
							if(s1 < s2) {
								couples[index++] = Couple.getCouple(s1, s2);
							}
						}
					}
				}
					
				if(MetricManager.hasOnlyValueMetric(metrics)){
						
					System.out.println("comptage des valeurs");
						
					nbValues = 4 + values.length;
						
					kernel = new MapCountValueKernel(Raster.getNoDataValue(), values);
							
					counting = new ValueCounting(0, nbValues, values, theoreticalSize);
						
				}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
					
					System.out.println("comptage des couples");
						
					nbValues = 3 + couples.length;
						
					kernel = new MapCountCoupleKernel(Raster.getNoDataValue(), values);
						
					counting = new CoupleCounting(0, nbValues, values.length, couples, theoreticalCoupleSize);
					
				}else{
					
					System.out.println("comptage des valeurs et des couples");

					nbValues = 4 + values.length + 2 + couples.length;
						
					kernel = new MapCountValueAndCoupleKernel(Raster.getNoDataValue(), values);
										
					counting = new ValueAndCoupleCounting(values, couples, theoreticalSize, theoreticalCoupleSize);
					
				}	
			}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
				
				System.out.println("comptage des patchs");
					
				nbValues = 4 + 3*values.length;
					
				kernel = new MapPatchKernel(Raster.getNoDataValue(), values, inCellSize);
				
				counting = new PatchCounting(values);
				
			}
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
		return create(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	protected abstract int[] readValues(Coverage coverage, Rectangle roi);
	
	protected abstract MapLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting);
	
	
}