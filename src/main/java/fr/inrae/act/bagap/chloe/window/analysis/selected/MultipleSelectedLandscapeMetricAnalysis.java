package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class MultipleSelectedLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput, csvFolder;
	
	private Set<Integer> coherences;
	
	private Map<String, List<String>> csvOutputs;
	
	private Map<String, List<String>> suffixMetrics;
	
	public MultipleSelectedLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
		super(builder);
	}
	
	@Override
	protected void doInit() {
		
		try {
			totalMetrics = builder.getMetrics();
			totalCsvOutput = builder.getCsv();
			csvFolder = builder.getCsvFolder();
			
			String path = csvFolder;
			if(totalCsvOutput != null || csvFolder != null){
				if(path == null) {
					path = new File(totalCsvOutput).getParent();
				}
				csvOutputs = new LinkedHashMap<String, List<String>>();
				suffixMetrics = new LinkedHashMap<String, List<String>>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics;
			
			if(builder.getRasterFiles().size() <= 1){ // un seul raster --> un seul fichier CSV
				
				String rasterFile = builder.getRasterFile();
				
				if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
					csvOutputs.put(rasterFile, new ArrayList<String>());
					suffixMetrics.put(rasterFile, new ArrayList<String>());
				}
				
				for(int ws : builder.getWindowSizes()){ // pour chaque taille de fenetre
					builder.setWindowSize(ws);
					
					for(int coherence : coherences){ // pour chaque groupe de metrique coherent
							
						metrics = new HashSet<Metric>();
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						
						if(!MetricManager.hasOnlyBasicMetric(metrics) && coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null || csvFolder != null){
							builder.addCsvOutput(path+"/selected_"+coherence+"_"+ws+".csv");
							csvOutputs.get(rasterFile).add(path+"/selected_"+coherence+"_"+ws+".csv");
							suffixMetrics.get(rasterFile).add("_"+ws);
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
					}
				}
				
			}else {
				
				for(String rasterFile : builder.getRasterFiles()){
					
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					builder.setRasterFile(rasterFile);
					builder.setPixelsFilter((Set<? extends Pixel>) null); 
					
					if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
						csvOutputs.put(rasterFile, new ArrayList<String>());
						suffixMetrics.put(rasterFile, new ArrayList<String>());
					}					
					
					for(int ws : builder.getWindowSizes()){
						builder.setWindowSize(ws);
						
						for(int coherence : coherences){
							metrics = new HashSet<Metric>();
							metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
							
							if(!MetricManager.hasOnlyBasicMetric(metrics) && coherence == 0){
								continue;
							}
							builder.setMetrics(metrics);
							
							if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
								builder.addCsvOutput(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
								csvOutputs.get(rasterFile).add(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
								suffixMetrics.get(rasterFile).add("_"+ws);
							}
							
							
							
							add(LandscapeMetricAnalysisFactory.create(builder));
						}
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doClose() {
		
		if(totalCsvOutput != null || csvFolder != null){
			
			if(builder.getRasterFiles().size() <= 1){
				
				String rasterFile = builder.getRasterFile();
			
				String localCsvOutput;
				if(totalCsvOutput != null) {
					localCsvOutput = totalCsvOutput;
				}else {
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					localCsvOutput = csvFolder+name+".csv";
				}
				
				//Coverage cov = CoverageManager.getCoverage(rasterFile);
				//EnteteRaster entete = cov.getEntete();
				//cov.dispose();
				EnteteRaster entete = builder.getEntete();
				Set<Pixel> pixels = builder.getRefPixels();
				/*
				Set<Pixel> pixels = null;
				if(builder.getRefPixels() != null){
					pixels = builder.getRefPixels();
				}else if(builder.getPixelsFilter() != null){
					pixels = CoordinateManager.initWithPixels(builder.getPixelsFilter());
					builder.setPixelsFilter(pixels);
				}else if(builder.getRefPoints() != null){
					throw new IllegalArgumentException();
				}else{
					pixels = CoordinateManager.initWithPoints(builder.getPointsFilter(), entete);
					builder.setPixelsFilter(pixels);
				}
				*/
				SpatialCsvManager.mergeFromPixels(localCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.get(rasterFile).size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), entete, pixels);
				
				for(String csvOut : csvOutputs.get(rasterFile)){
					new File(csvOut).delete();
				}
			}else {
				
				int noDataValue = -1;
				Set<Pixel> pixels = null; 
				Map<String, String> localCsv = new TreeMap<String, String>();
				for(String rasterFile : builder.getRasterFiles()){
					
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					
					String localCsvOutput;
					if(totalCsvOutput != null) {
						localCsvOutput = totalCsvOutput.replace(".csv", "")+"_"+name+".csv";
					}else {
						localCsvOutput = csvFolder+name+".csv";
					}
					
					Coverage cov = CoverageManager.getCoverage(rasterFile);
					EnteteRaster entete = cov.getEntete();
					noDataValue = entete.noDataValue();
					cov.dispose();
					pixels = builder.getRefPixels();
					SpatialCsvManager.mergeFromPixels(localCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.get(rasterFile).size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), entete, pixels);
					
					for(String csvOut : csvOutputs.get(rasterFile)){
						new File(csvOut).delete();
					}
					
					localCsv.put(localCsvOutput, new File(rasterFile).getName());	
					
				}
				
				if(totalCsvOutput != null) {
					SpatialCsvManager.mergeMapPixels(totalCsvOutput, localCsv, noDataValue, pixels);
					
					for(String csv : localCsv.keySet()){
						new File(csv).delete();
					}
				}	
			}
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
