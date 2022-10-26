package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.MultipleCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.AreaCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.AreaCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.AreaCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.AreaLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.AreaQuantitativeKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.AreaCsvOutput;
import fr.inrae.act.bagap.chloe.output.AreaMultipleAsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.AreaTabOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.FileCoverage;
import fr.inrae.act.bagap.raster.TabCoverage;

public class AreaLandscapeMetricAnalysisFactory  {

	//public static AreaLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, GridCoverage2D coverage) throws IOException {
	public static AreaLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
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
		//double inMaxX = coverage.maxx();
		//double inMaxY = coverage.maxy();
		double inCellSize = coverage.cellsize();
		
		Coverage areaCoverage;
		
		if(builder.getAreaRasterFile() != null){
			// area coverage 
			GridCoverage2DReader reader;
			if(builder.getAreaRasterFile().endsWith(".asc")){
				File file = new File(builder.getAreaRasterFile());
				reader = new ArcGridReader(file);
			}else if(builder.getAreaRasterFile().endsWith(".tif")){
				File file = new File(builder.getAreaRasterFile());
				reader = new GeoTiffReader(file);
			}else{
				throw new IllegalArgumentException(builder.getRasterFile()+" is not a recognize raster");
			}
			GridCoverage2D areaCoverage2D = (GridCoverage2D) reader.read(null);
			reader.dispose(); 
			
			areaCoverage = new FileCoverage(areaCoverage2D, coverage.getEntete());
			
		}else if(builder.getAreaRasterTab() != null){
			
			areaCoverage = new TabCoverage(builder.getAreaRasterTab(), coverage.getEntete());
		}else{
			throw new IllegalArgumentException("no area raster declared");
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
		
		// metriques
		Set<Metric> metrics = builder.getMetrics();
					
		// observers
		Set<CountingObserver> observers = builder.getObservers();
		if(builder.getCsv() != null){
			AreaCsvOutput csvOutput = new AreaCsvOutput(builder.getCsv());
			observers.add(csvOutput);
		}
		if(builder.getAsciiGridFolder() != null){
			AreaMultipleAsciiGridOutput asciiOutput = new AreaMultipleAsciiGridOutput(builder.getAsciiGridFolder(), areaCoverage, roiWidth, roiHeight, inMinX, inMinY, inCellSize, Raster.getNoDataValue());
			observers.add(asciiOutput);
		}
		if(builder.getTabOutputs().size() > 0){
			for(Entry<String, float[]> e : builder.getTabOutputs().entrySet()){
				AreaTabOutput tabOutput = new AreaTabOutput(e.getValue(), areaCoverage, MetricManager.get(e.getKey()), roiWidth, roiHeight, Raster.getNoDataValue());
				observers.add(tabOutput);
			}
		}
					
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			Counting counting = new QuantitativeCounting(0, 6);
			
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
						
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
			
			AreaLandscapeMetricKernel kernel = new AreaQuantitativeKernel(Raster.getNoDataValue());
						
			// analysis
			return new HugeAreaLandscapeMetricAnalysis(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, 6, kernel, counting);
						
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
			AreaLandscapeMetricKernel kernel = null; 
			Counting counting = null;
			int nbValues = 2;
			if(MetricManager.hasOnlyValueMetric(metrics)){
				nbValues += values.length;
				kernel = new AreaCountValueKernel(Raster.getNoDataValue(), values);
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
				nbValues += couples.length;
				kernel = new AreaCountCoupleKernel(Raster.getNoDataValue(), values);
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
				nbValues += values.length + 2 + couples.length;
				
				Counting[] countings = new Counting[2];
				
				kernel = new AreaCountValueAndCoupleKernel(Raster.getNoDataValue(), values);
				ValueCounting vCounting = new ValueCounting(0, values.length+2, values);
				// add metrics to counting
				for(Metric m : metrics){
					if(MetricManager.isValueMetric(m.getName())){
						vCounting.addMetric(m);
					}
				}
				countings[0] = vCounting;
			
				CoupleCounting cCounting = new CoupleCounting(values.length+2, nbValues, values.length, couples);
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
			//return new AreaLandscapeMetricAnalysis(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, nbValues, kernel, counting);
			return new HugeAreaLandscapeMetricAnalysis(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, nbValues, kernel, counting);
		}
	}

}