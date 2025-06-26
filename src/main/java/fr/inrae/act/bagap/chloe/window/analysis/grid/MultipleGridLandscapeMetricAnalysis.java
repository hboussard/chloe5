package fr.inrae.act.bagap.chloe.window.analysis.grid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class MultipleGridLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput, csvFolder;
	
	private Set<Integer> coherences;
	
	private Map<String, Map<Integer, List<String>>> csvOutputs;
	
	private Map<String, Map<Integer, List<String>>> suffixMetrics;
	
	public MultipleGridLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
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
				csvOutputs = new LinkedHashMap<String, Map<Integer, List<String>>>();
				suffixMetrics = new LinkedHashMap<String, Map<Integer, List<String>>>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics;
			
			if(builder.getRasterFiles().size() <= 1){ // un seul raster 
				
				String rasterFile = builder.getRasterFile();
				
				if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
					csvOutputs.put(rasterFile, new TreeMap<Integer, List<String>>());
					suffixMetrics.put(rasterFile, new TreeMap<Integer, List<String>>());
				}
				
				for(int ws : builder.getWindowSizes()){ // pour chaque taille de fenetre, un fichier CSV
					builder.setWindowSize(ws);
					
					if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
						csvOutputs.get(rasterFile).put(ws, new ArrayList<String>());
						suffixMetrics.get(rasterFile).put(ws, new ArrayList<String>());
					}
					
					for(int coherence : coherences){ // pour chaque groupe coherent de metriques
						metrics = new HashSet<Metric>();
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						
						if(!MetricManager.hasOnlyBasicMetric(metrics) && coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
							builder.addCsvOutput(path+"/grid_"+coherence+"_"+ws+".csv");
							csvOutputs.get(rasterFile).get(ws).add(path+"/grid_"+coherence+"_"+ws+".csv");
							suffixMetrics.get(rasterFile).get(ws).add("_"+ws);
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
					}
				}
			}else{ // plusieurs rasters 
				
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					builder.setRasterFile(rasterFile);
					
					if(totalCsvOutput != null || csvFolder != null){
						csvOutputs.put(rasterFile, new TreeMap<Integer, List<String>>());
						suffixMetrics.put(rasterFile, new TreeMap<Integer, List<String>>());
					}
					
					for(int ws : builder.getWindowSizes()){ // pour chaque taille de fenetre
						builder.setWindowSize(ws);
						
						if(totalCsvOutput != null || csvFolder != null){ // export CSV demande
							csvOutputs.get(rasterFile).put(ws, new ArrayList<String>());
							suffixMetrics.get(rasterFile).put(ws, new ArrayList<String>());
						}
						
						for(int coherence : coherences){ // pour chaque groupe coherent de metriques
							metrics = new HashSet<Metric>();
							metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
							
							if(!MetricManager.hasOnlyBasicMetric(metrics) && coherence == 0){
								continue;
							}
							builder.setMetrics(metrics);
							
							if(totalCsvOutput != null || csvFolder != null){
								builder.addCsvOutput(path+"/"+name+"_grid_"+coherence+"_"+ws+".csv");
								csvOutputs.get(rasterFile).get(ws).add(path+"/"+name+"_grid_"+coherence+"_"+ws+".csv");
								suffixMetrics.get(rasterFile).get(ws).add("_"+ws);
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
				
				for(int ws : builder.getWindowSizes()){ // pour chaque taille de grille
					
					EnteteRaster entete = EnteteRaster.read(csvOutputs.get(rasterFile).get(ws).iterator().next().replace(".csv", "_header.txt"));
					
					String localCsvOutput;
					if(totalCsvOutput != null) {
						if(builder.getWindowSizes().size() == 1) {
							
							localCsvOutput = totalCsvOutput;
						}else {
							
							localCsvOutput = totalCsvOutput.replace(".csv", "")+"_"+ws+".csv";
						}
					}else {
						String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
						localCsvOutput = csvFolder+name+"_"+ws+".csv";
					}
					
					SpatialCsvManager.mergeXY(localCsvOutput, getCsvOutputs(rasterFile, ws), getSuffixMetrics(rasterFile, ws), "X", "Y", entete);
					
					// nettoyage
					for(String csvOut : csvOutputs.get(rasterFile).get(ws)){
						new File(csvOut).delete();
						Tool.copy(csvOut.replace(".csv", "_header.txt"), localCsvOutput.replace(".csv", "_header.txt"));
						new File(csvOut.replace(".csv", "_header.txt")).delete();
					}
				}
			}else{
				
				//Set<String> localCsv = new HashSet<String>();
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					
					for(int ws : builder.getWindowSizes()){ // pour chaque taille de grille
						
						EnteteRaster entete = EnteteRaster.read(csvOutputs.get(rasterFile).get(ws).iterator().next().replace(".csv", "_header.txt"));
						
						String localCsvOutput;
						if(totalCsvOutput != null) {
							localCsvOutput = totalCsvOutput.replace(".csv", "")+"_"+name+"_"+ws+".csv";
						}else {
							localCsvOutput = csvFolder+name+"_"+ws+".csv";
						}
						
						SpatialCsvManager.mergeXY(localCsvOutput, getCsvOutputs(rasterFile, ws), getSuffixMetrics(rasterFile, ws), "X", "Y", entete);
						
						// nettoyage
						for(String csvOut : csvOutputs.get(rasterFile).get(ws)){
							new File(csvOut).delete();
							Tool.copy(csvOut.replace(".csv", "_header.txt"), localCsvOutput.replace(".csv", "_header.txt"));
							new File(csvOut.replace(".csv", "_header.txt")).delete();
						}
						
						//localCsv.add(localCsvOutput);
					}
				}
				
				
				//SpatialCsvManager.mergeSortXY(totalCsvOutput, localCsv.toArray(new String[localCsv.size()]));
				
				//for(String csv : localCsv){
				//	new File(csv).delete();
				//}
					
			}
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

	private String[] getCsvOutputs(String rasterFile, int ws) {
		return csvOutputs.get(rasterFile).get(ws).toArray(new String[csvOutputs.get(rasterFile).get(ws).size()]);
	}
	
	private String[] getSuffixMetrics(String rasterFile, int ws) {
		return suffixMetrics.get(rasterFile).get(ws).toArray(new String[suffixMetrics.get(rasterFile).get(ws).size()]);
	}

}
