package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class MultipleSlidingLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput;
	
	private Set<Integer> coherences;
	
	private Map<String, List<String>> csvOutputs;
	
	private Map<String, List<String>> suffixMetrics;
	
	public MultipleSlidingLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
		super(builder);
	}
	
	@Override
	protected void doInit() {
		try {
			totalMetrics = builder.getMetrics();
			totalCsvOutput = builder.getCsv();
			
			String path = null;
			if(totalCsvOutput != null){
				path = new File(totalCsvOutput).getParent();
				csvOutputs = new LinkedHashMap<String, List<String>>();
				suffixMetrics = new LinkedHashMap<String, List<String>>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics = new HashSet<Metric>();
			
			if(builder.getRasterFiles().size() == 0 || builder.getRasterFiles().size() == 1){
				
				String rasterFile = builder.getRasterFile();
				
				if(totalCsvOutput != null){
					csvOutputs.put(rasterFile, new ArrayList<String>());
					suffixMetrics.put(rasterFile, new ArrayList<String>());
				}
				
				for(int ws : builder.getWindowSizes()){
					builder.setWindowSize(ws);
					
					for(int coherence : coherences){
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						if(coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null){
							builder.addCsvOutput(path+"/sliding_"+coherence+"_"+ws+".csv");
							csvOutputs.get(rasterFile).add(path+"/sliding_"+coherence+"_"+ws+".csv");
							suffixMetrics.get(rasterFile).add("_"+ws);
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
						
						metrics = new HashSet<Metric>();
					}
				}
				
			}else{
				
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					builder.setRasterFile(rasterFile);
					
					if(totalCsvOutput != null){
						csvOutputs.put(rasterFile, new ArrayList<String>());
						suffixMetrics.put(rasterFile, new ArrayList<String>());
					}
					
					for(int ws : builder.getWindowSizes()){
						builder.setWindowSize(ws);
						
						for(int coherence : coherences){
							metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
							if(coherence == 0){
								continue;
							}
							builder.setMetrics(metrics);
							
							if(totalCsvOutput != null){
								builder.addCsvOutput(path+"/"+name+"_sliding_"+coherence+"_"+ws+".csv");
								csvOutputs.get(rasterFile).add(path+"/"+name+"_sliding_"+coherence+"_"+ws+".csv");
								suffixMetrics.get(rasterFile).add("_"+ws);
							}
							
							add(LandscapeMetricAnalysisFactory.create(builder));
						
							metrics = new HashSet<Metric>();
						}
					}
				}
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() {
		for(LandscapeMetricAnalysis analysis : analyses){
			analysis.allRun();
		}
	}

	@Override
	protected void doClose() {
		
		if(totalCsvOutput != null){
			
			if(builder.getRasterFiles().size() == 0 || builder.getRasterFiles().size() == 1){
				
				String rasterFile = builder.getRasterFile();
					
				EnteteRaster entete = EnteteRaster.read(csvOutputs.get(rasterFile).iterator().next().replace(".csv", "_header.txt"));
				SpatialCsvManager.mergeXY(totalCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), "X", "Y", entete);
					
				for(String csvOut : csvOutputs.get(rasterFile)){
					new File(csvOut).delete();
					Tool.copy(csvOut.replace(".csv", "_header.txt"), totalCsvOutput.replace(".csv", "_header.txt"));
					new File(csvOut.replace(".csv", "_header.txt")).delete();
				}
				
			}else{
				
				Set<String> localCsv = new HashSet<String>();
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					String localCsvOutput = totalCsvOutput.replace(".csv", "")+"_"+name+".csv";
					
					EnteteRaster entete = EnteteRaster.read(csvOutputs.get(rasterFile).iterator().next().replace(".csv", "_header.txt"));
					SpatialCsvManager.mergeXY(localCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), "X", "Y", entete);
					
					for(String csvOut : csvOutputs.get(rasterFile)){
						new File(csvOut).delete();
						Tool.copy(csvOut.replace(".csv", "_header.txt"), localCsvOutput.replace(".csv", "_header.txt"));
						new File(csvOut.replace(".csv", "_header.txt")).delete();
					}
					
					localCsv.add(localCsvOutput);
				}
				
				SpatialCsvManager.mergeSortXY(totalCsvOutput, localCsv.toArray(new String[localCsv.size()]));
				
				for(String csv : localCsv){
					new File(csv).delete();
				}
				
			}
			
			
		
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
