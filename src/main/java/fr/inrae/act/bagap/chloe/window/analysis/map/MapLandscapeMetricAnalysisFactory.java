package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.raster.Raster;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapPatchKernel;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.MapCsvOutput;
import fr.inrae.act.bagap.apiland.raster.Coverage;

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
		double theoreticalSize = 0;
		double theoreticalCoupleSize = 0;
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
		//Set<CountingObserver> observers = builder.getObservers();
		Set<CountingObserver> observers = new HashSet<CountingObserver>();
		
		if(builder.getCsv() != null){
			String name = new File(builder.getRasterFile()).getName();
			MapCsvOutput csvOutput = new MapCsvOutput(builder.getCsv(), name);
			observers.add(csvOutput);	
		}else if (builder.getCsvFolder() != null){
			String name = new File(builder.getRasterFile()).getName();
			String csvName = builder.getCsvFolder()+new File(builder.getRasterFile()).getName().replace(".tif", "").replace(".asc", "").toString()+".csv";
			MapCsvOutput csvOutput = new MapCsvOutput(csvName, name);
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
				
			counting = new QuantitativeCounting(theoreticalSize);
				
		}else{
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight), coverage.getEntete().noDataValue());
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
						
					nbValues = 5 + values.length;
						
					kernel = new MapCountValueKernel(Raster.getNoDataValue(), values);
							
					counting = new ValueCounting(inCellSize, values, theoreticalSize);
						
				}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
					
					System.out.println("comptage des couples");
						
					nbValues = 7 + couples.length;
						
					kernel = new MapCountCoupleKernel(Raster.getNoDataValue(), values);
						
					counting = new CoupleCounting(inCellSize, values.length, couples, theoreticalSize, theoreticalCoupleSize);
					
				}else{
					
					System.out.println("comptage des valeurs et des couples");

					nbValues = 5 + values.length + 3 + couples.length;
						
					kernel = new MapCountValueAndCoupleKernel(Raster.getNoDataValue(), values);
										
					counting = new ValueAndCoupleCounting(inCellSize, values, couples, theoreticalSize, theoreticalCoupleSize);
					
				}	
			}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
				
				System.out.println("comptage des patchs");
					
				nbValues = 8 + 4*values.length;
					
				kernel = new MapPatchKernel(Raster.getNoDataValue(), values, inCellSize);
				
				counting = new PatchCounting(inCellSize, values, theoreticalSize);
				
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
	
	protected abstract int[] readValues(Coverage coverage, Rectangle roi, int noDataValue);
	
	protected abstract MapLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting);
	
	
}
