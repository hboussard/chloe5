package fr.inrae.act.bagap.chloe.analysis.grid;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.grid.GridCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.grid.GridCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.grid.GridCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.grid.GridPatchKernel;
import fr.inrae.act.bagap.chloe.kernel.grid.GridQuantitativeKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.CsvOutput;
import fr.inrae.act.bagap.chloe.output.GeoTiffOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyGridLandscapeMetricAnalysisFactory {

	public static GridLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		System.out.println("tiny");
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMinY = coverage.miny();
		double inMaxX = coverage.maxx();
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
		int gridSize = builder.getGridSize();
		
		// taille de sortie
		int outWidth = (int) (((roiWidth-1)/gridSize)+1);
		int outHeight = (int) (((roiHeight-1)/gridSize)+1);
		double outCellSize = gridSize*inCellSize;
		double outMinX = inMinX + roiX*inCellSize; //+ inCellSize/2.0 - outCellSize/2.0;
		double outMaxX = outMinX + outWidth*outCellSize;
		double outMaxY = inMaxY - roiY*inCellSize; //- inCellSize/2.0 + outCellSize/2.0;
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
		for(Entry<String, String> entry : builder.getAsciiOutputs().entrySet()){
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
		for(Entry<String, String> entry : builder.getGeoTiffOutputs().entrySet()){
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
			
			
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			GridLandscapeMetricKernel kernel = new GridQuantitativeKernel(gridSize, Raster.getNoDataValue());
				
			Counting counting = new QuantitativeCounting(0, 7, theoreticalSize);
				
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
				
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
				
			// analysis
			return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, 7, kernel, counting);
				
		}else if(MetricManager.hasOnlyQualitativeMetric(metrics)){ // qualitative
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
				Set<Float> inValues = new HashSet<Float>();
				for(float d : datas){
					if(d != 0 && d != Raster.getNoDataValue()){
						inValues.add(d);
					}
				}
				int index = 0;
				values = new int[inValues.size()];
				for(float d : inValues){
					values[index++] = (int) d;
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
				
			// kernel et counting
			GridLandscapeMetricKernel kernel = null;
			Counting counting;
				
			int nbValues = 2;
			if(MetricManager.hasOnlyValueMetric(metrics)){
					
				System.out.println("comptage des valeurs");
					
				nbValues += 1 + values.length;
					
				kernel = new GridCountValueKernel(gridSize, Raster.getNoDataValue(), values);
						
				counting = new ValueCounting(0, nbValues, values, theoreticalSize);
						
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
				
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
						
				// analysis
				return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
					
					
			}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
				
				System.out.println("comptage des couples");
					
				nbValues += couples.length;
					
				kernel = new GridCountCoupleKernel(gridSize, Raster.getNoDataValue(), values);
					
				counting = new CoupleCounting(0, nbValues, values.length, couples, theoreticalCoupleSize);
					
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
				// analysis
				return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
				
			}else{
				
				System.out.println("comptage des valeurs et des couples");

				nbValues += 1 + values.length + 2 + couples.length;
					
				kernel = new GridCountValueAndCoupleKernel(gridSize, Raster.getNoDataValue(), values);
									
				counting = new ValueAndCoupleCounting(values, couples, theoreticalSize, theoreticalCoupleSize);
					
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
				// analysis
				return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
				
			}
				
		}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
			
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
				Set<Float> inValues = new HashSet<Float>();
				for(float d : datas){
					if(d != 0){
						inValues.add(d);
					}
				}
				int index = 0;
				values = new int[inValues.size()];
				for(float d : inValues){
					values[index++] = (int) d;
				}
			}
				
			int nbValues = 3 + 3*values.length;
				
			GridLandscapeMetricKernel kernel = new GridPatchKernel(gridSize, Raster.getNoDataValue(), values, inCellSize);
			Counting counting = new PatchCounting(values);
				
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
				
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
				
			// analysis
			return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
			
		}
		return null;
	}
	
}
