package fr.inrae.act.bagap.chloe.analysis.entity;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityQuantitativeKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.EntityTabOutput;
import fr.inrae.act.bagap.chloe.output.EntityCsvOutput;
import fr.inrae.act.bagap.chloe.output.EntityMultipleAsciiGridOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.TabCoverage;

public class TinyEntityLandscapeMetricAnalysisFactory {
	
	public static EntityLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		System.out.println("tiny entity");
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMinY = coverage.miny();
		//double inMaxX = coverage.maxx();
		//double inMaxY = coverage.maxy();
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
					
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			Counting counting = new QuantitativeCounting(0, 7);
			
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
						
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
			
			EntityLandscapeMetricKernel kernel = new EntityQuantitativeKernel(Raster.getNoDataValue());
						
			// analysis
			return new TinyEntityLandscapeMetricAnalysis(coverage, entityCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, 7, kernel, counting);
						
		}else{ // qualitative
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
				Set<Float> inValues = new HashSet<Float>();
				for(float d : datas){
					inValues.add(d);
				}
				int index = 0;
				values = new int[inValues.size()];
				for(float d : inValues){
					values[index++] = (short) d;
				}
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
						
			// kernel and counting
			EntityLandscapeMetricKernel kernel = null; 
			Counting counting = null;
			int nbValues = 2;
			if(MetricManager.hasOnlyValueMetric(metrics)){
				
				System.out.println("comptage des valeurs");
				
				nbValues += 1 + values.length;
				
				kernel = new EntityCountValueKernel(Raster.getNoDataValue(), values);
				counting = new ValueCounting(0, nbValues, values);
				
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
							
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
				
			}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
				
				System.out.println("comptage des couples");
				
				nbValues += couples.length;
				
				kernel = new EntityCountCoupleKernel(Raster.getNoDataValue(), values);
				counting = new CoupleCounting(0, nbValues, values.length, couples);
				
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
							
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
			
			}else{
				
				System.out.println("comptage des valeurs et des couples");
				
				nbValues += 1 + values.length + 2 + couples.length;
				
				kernel = new EntityCountValueAndCoupleKernel(Raster.getNoDataValue(), values);
				
				counting = new ValueAndCoupleCounting(values, couples);
				
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
				
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
			}
						
			// analysis
			return new TinyEntityLandscapeMetricAnalysis(coverage, entityCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		}
	}

}
