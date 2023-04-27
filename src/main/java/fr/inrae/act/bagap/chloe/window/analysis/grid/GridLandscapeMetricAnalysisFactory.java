package fr.inrae.act.bagap.chloe.window.analysis.grid;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridPatchKernel;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.CsvOutput;
import fr.inrae.act.bagap.chloe.window.output.GeoTiffOutput;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class GridLandscapeMetricAnalysisFactory {

	public GridLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMaxY = coverage.maxy();
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
			
		// gridSize
		int gridSize = builder.getWindowSize();
		
		// taille de sortie
		int outWidth = (int) (((roiWidth-1)/gridSize)+1);
		int outHeight = (int) (((roiHeight-1)/gridSize)+1);
		double outCellSize = gridSize*inCellSize;
		double outMinX = inMinX + roiX*inCellSize;
		double outMaxX = outMinX + outWidth*outCellSize;
		double outMaxY = inMaxY - roiY*inCellSize;
		double outMinY = outMaxY - outHeight*outCellSize;
			
		// buffer ROI
		int bufferROIXMin = roiX;
		int bufferROIXMax = inWidth-(roiX+roiWidth);
		int bufferROIYMin = roiY;
		int bufferROIYMax = inHeight-(roiY+roiHeight);
		
		// tailles theoriques
		int theoreticalSize = 0;
		int theoreticalCoupleSize = 0;
		for(int j=0; j<gridSize; j++){
			for(int i=0; i<gridSize; i++){
					
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
			CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, outCellSize, Raster.getNoDataValue());
			observers.add(csvOutput);	
		}
		for(Entry<String, String> entry : builder.getAsciiOutputs(gridSize).entrySet()){
			Metric metric = null;
			for(Metric m : metrics){
				if(m.getName().equalsIgnoreCase(entry.getKey())){
					metric = m;
					break;
				}
			}
			AsciiGridOutput asciiOutput = new AsciiGridOutput(entry.getValue(), metric, outWidth, outHeight, outMinX, outMinY, outCellSize, Raster.getNoDataValue());
			observers.add(asciiOutput);
		}
		for(Entry<String, String> entry : builder.getGeoTiffOutputs(gridSize).entrySet()){
			Metric metric = null;
			for(Metric m : metrics){
				if(m.getName().equalsIgnoreCase(entry.getKey())){
					metric = m;
					break;
				}
			}
			GeoTiffOutput geotiffOutput = new GeoTiffOutput(entry.getValue(), metric, outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, Raster.getNoDataValue());
			observers.add(geotiffOutput);
		}
		/*
		for(Entry<String, float[]> entry : builder.getTabOutputs().entrySet()){
			Metric metric = null;
			for(Metric m : metrics){
				if(m.getName().equalsIgnoreCase(entry.getKey())){
					metric = m;
					break;
				}
			}
			TabOutput tabOutput = new TabOutput(entry.getValue(), metric, outWidth, displacement);
			observers.add(tabOutput);
		}*/
			
			
		GridLandscapeMetricKernel kernel = null;
		Counting counting = null;
		int nbValues = -1;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			nbValues = 8;
			
			kernel = new GridQuantitativeKernel(gridSize, Raster.getNoDataValue());
				
			counting = new QuantitativeCounting(theoreticalSize);
			
				
		}else{ // qualitative
			
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight));
			}
			
			if(MetricManager.hasOnlyQualitativeMetric(metrics)){ 
					
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
						
					kernel = new GridCountValueKernel(gridSize, Raster.getNoDataValue(), values);
							
					counting = new ValueCounting(values, theoreticalSize);	
						
				}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
					
					System.out.println("comptage des couples");
						
					nbValues = 3 + couples.length;
						
					kernel = new GridCountCoupleKernel(gridSize, Raster.getNoDataValue(), values);
						
					counting = new CoupleCounting(values.length, couples, theoreticalSize, theoreticalCoupleSize);
					
				}else{
					
					System.out.println("comptage des valeurs et des couples");

					nbValues = 5 + values.length + 3 + couples.length;
						
					kernel = new GridCountValueAndCoupleKernel(gridSize, Raster.getNoDataValue(), values);
										
					counting = new ValueAndCoupleCounting(values, couples, theoreticalSize, theoreticalCoupleSize);
					
				}
			}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
					
				System.out.println("comptage des patchs");
					
				nbValues = 7 + 3*values.length;
						
				kernel = new GridPatchKernel(gridSize, Raster.getNoDataValue(), values, inCellSize);
					
				counting = new PatchCounting(values, theoreticalSize);
					
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
	
	protected abstract GridLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, GridLandscapeMetricKernel kernel, Counting counting);
	
}
