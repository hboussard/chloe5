package fr.inrae.act.bagap.chloe.window.analysis.entity;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

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
import fr.inrae.act.bagap.chloe.window.output.EntityRasterOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
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
		//Set<CountingObserver> observers = builder.getObservers();
		Set<CountingObserver> observers = new HashSet<CountingObserver>();
		
		if(builder.getCsv() != null){
			EntityCsvOutput csvOutput = new EntityCsvOutput(builder.getCsv());
			observers.add(csvOutput);
		}
		/*
		if(builder.getAsciiGridFolder() != null){
			EntityMultipleAsciiGridOutput asciiOutput = new EntityMultipleAsciiGridOutput(builder.getAsciiGridFolder(), entityCoverage, roiWidth, roiHeight, inMinX, inMinY, inCellSize, Raster.getNoDataValue());
			observers.add(asciiOutput);
		}
		if(builder.getTabOutputs().size() > 0){
			for(Entry<String, float[]> e : builder.getTabOutputs().entrySet()){
				EntityTabOutput tabOutput = new EntityTabOutput(e.getValue(), entityCoverage, MetricManager.get(e.getKey()), roiWidth, roiHeight, Raster.getNoDataValue());
				observers.add(tabOutput);
			}
		}*/
		
		if(builder.getAsciiOutputs(0) != null){
			for (Entry<String, String> entry : builder.getAsciiOutputs(0).entrySet()) {
				Metric metric = null;
				for (Metric m : metrics) {
					if (m.getName().equalsIgnoreCase(entry.getKey())) {
						metric = m;
						break;
					}
				}
				if(metric != null){
					
					EntityRasterOutput rasterOutput = new EntityRasterOutput(entry.getValue(), metric, entityCoverage, coverage.getEntete().noDataValue());
					observers.add(rasterOutput);
					
				}
			}
		}
		
		if(builder.getAsciiGridFolder() != null){
			
			String rasterFolder = builder.getAsciiGridFolder();
			if(!(rasterFolder.endsWith("/") || rasterFolder.endsWith("\\"))){
				rasterFolder += "/";
			}
			String tifFile;
			for (Metric m : metrics) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(new File(builder.getRasterFile()).getName().replace(".asc", "").replace(".tif", "")); // file name, assume it exists
				sb.append("_"+m.getName());
				sb.append(".asc");
				
				tifFile = rasterFolder+sb.toString();
				
				EntityRasterOutput geotiffOutput = new EntityRasterOutput(tifFile, m, entityCoverage, coverage.getEntete().noDataValue());
				observers.add(geotiffOutput);
			}
		}
		
		if(builder.getGeoTiffOutputs(0) != null){
			for (Entry<String, String> entry : builder.getGeoTiffOutputs(0).entrySet()) {
				Metric metric = null;
				for (Metric m : metrics) {
					if (m.getName().equalsIgnoreCase(entry.getKey())) {
						metric = m;
						break;
					}
				}
				if(metric != null){
					
					EntityRasterOutput rasterOutput = new EntityRasterOutput(entry.getValue(), metric, entityCoverage, coverage.getEntete().noDataValue());
					observers.add(rasterOutput);
					
				}
			}
		}
		
		if(builder.getGeoTiffFolder() != null){
			
			String tifFolder = builder.getGeoTiffFolder();
			if(!(tifFolder.endsWith("/") || tifFolder.endsWith("\\"))){
				tifFolder += "/";
			}
			String tifFile;
			for (Metric m : metrics) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(new File(builder.getRasterFile()).getName().replace(".asc", "").replace(".tif", "")); // file name, assume it exists
				sb.append("_"+m.getName());
				sb.append(".tif");
				
				tifFile = tifFolder+sb.toString();
				
				EntityRasterOutput rasterOutput = new EntityRasterOutput(tifFile, m, entityCoverage, coverage.getEntete().noDataValue());
				observers.add(rasterOutput);
			}
		}
			
		// kernel and counting
		EntityLandscapeMetricKernel kernel;
		Counting counting;
		int nbValues;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			nbValues = 8;
			
			kernel = new EntityQuantitativeKernel(Raster.getNoDataValue());
			
			counting = new QuantitativeCounting();
						
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
				
				nbValues = 5 + values.length;
				
				kernel = new EntityCountValueKernel(Raster.getNoDataValue(), values);
				
				counting = new ValueCounting(values);
				
			}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
				
				System.out.println("comptage des couples");
				
				nbValues = 7 + couples.length;
				
				kernel = new EntityCountCoupleKernel(Raster.getNoDataValue(), values);
				
				counting = new CoupleCounting(values.length, couples);
			
			}else{
				
				System.out.println("comptage des valeurs et des couples");
				
				nbValues = 5 + values.length + 3 + couples.length;
				
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
