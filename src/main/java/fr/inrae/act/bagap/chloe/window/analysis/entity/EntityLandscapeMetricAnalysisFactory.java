package fr.inrae.act.bagap.chloe.window.analysis.entity;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.EntityCsvOutput;
import fr.inrae.act.bagap.chloe.window.output.EntityMultipleAsciiGridOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.TabCoverage;

public abstract class EntityLandscapeMetricAnalysisFactory {


	public EntityLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		System.out.println("tiny entity");
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMinY = coverage.miny();
		double inCellSize = coverage.cellsize();
		
		Coverage entityCoverage;
		if (builder.getEntityRasterFile() != null) {
			entityCoverage = CoverageManager.getCoverage(builder.getEntityRasterFile());
		} else if (builder.getEntityRasterTab() != null) {
			entityCoverage = new TabCoverage(builder.getEntityRasterTab(), coverage.getEntete());
		} else {
			throw new IllegalArgumentException("no entity raster declared");
		}
		
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
		int bufferROIXMin = roiX;
		int bufferROIXMax = inWidth-(roiX+roiWidth);
		int bufferROIYMin = roiY;
		int bufferROIYMax = inHeight-(roiY+roiHeight);
		
		// metriques
		Set<Metric> metrics = builder.getMetrics();
					
		// observers
		Set<CountingObserver> observers = builder.getObservers();
		if(builder.getCsv() != null){
			EntityCsvOutput csvOutput = new EntityCsvOutput(builder.getCsv());
			observers.add(csvOutput);
		}
		if(builder.getAsciiGridFolder() != null){
			EntityMultipleAsciiGridOutput asciiOutput = new EntityMultipleAsciiGridOutput(builder.getAsciiGridFolder(), entityCoverage, roiWidth, roiHeight, inMinX, inMinY, inCellSize, Raster.getNoDataValue());
			observers.add(asciiOutput);
		}
		/*if(builder.getTabOutputs().size() > 0){
			for(Entry<String, float[]> e : builder.getTabOutputs().entrySet()){
				EntityTabOutput tabOutput = new EntityTabOutput(e.getValue(), entityCoverage, MetricManager.get(e.getKey()), roiWidth, roiHeight, Raster.getNoDataValue());
				observers.add(tabOutput);
			}
		}*/
			
		// kernel and counting
		EntityLandscapeMetricKernel kernel;
		Counting counting;
		int nbValues;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			nbValues = 8;
			
			kernel = new EntityQuantitativeKernel(Raster.getNoDataValue());
			
			counting = new QuantitativeCounting(0, nbValues);
						
		}else{ // qualitative
			
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight));
			}
						
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
				
				kernel = new EntityCountValueKernel(Raster.getNoDataValue(), values);
				
				counting = new ValueCounting(0, nbValues, values);
				
			}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
				
				System.out.println("comptage des couples");
				
				nbValues = 3 + couples.length;
				
				kernel = new EntityCountCoupleKernel(Raster.getNoDataValue(), values);
				
				counting = new CoupleCounting(0, nbValues, values.length, couples);
			
			}else{
				
				System.out.println("comptage des valeurs et des couples");
				
				nbValues = 4 + values.length + 2 + couples.length;
				
				kernel = new EntityCountValueAndCoupleKernel(Raster.getNoDataValue(), values);
				
				counting = new ValueAndCoupleCounting(values, couples);
				
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
		return create(coverage, entityCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}


	protected abstract int[] readValues(Coverage coverage, Rectangle roi);

	protected abstract EntityLandscapeMetricAnalysis create(Coverage coverage, Coverage entityCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, EntityLandscapeMetricKernel kernel, Counting counting);

}
