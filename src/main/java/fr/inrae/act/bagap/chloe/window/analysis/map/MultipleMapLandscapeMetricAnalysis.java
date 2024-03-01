package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;

public class MultipleMapLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput;
	
	private Set<Integer> coherences;
	
	private List<String> csvOutputs;
	
	public MultipleMapLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
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
				csvOutputs = new ArrayList<String>();
			}
			
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics = new HashSet<Metric>();
			
			if(builder.getRasterFiles().size() == 0 || builder.getRasterFiles().size() == 1){
				
				for(int coherence : coherences){
					metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
					if(coherence == 0){
						continue;
					}
					builder.setMetrics(metrics);
					
					if(totalCsvOutput != null){
						builder.addCsvOutput(path+"/map_"+coherence+".csv");
						csvOutputs.add(path+"/map_"+coherence+".csv");
					}
					
					add(LandscapeMetricAnalysisFactory.create(builder));
				
					metrics = new HashSet<Metric>();
				}
			}else{
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					builder.setRasterFile(rasterFile);
					
					for(int coherence : coherences){
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						if(coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null){
							builder.addCsvOutput(path+"/"+name+"_map_"+coherence+".csv");
							csvOutputs.add(path+"/"+name+"_map_"+coherence+".csv");
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
					
						metrics = new HashSet<Metric>();
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void doClose() {
		
		SpatialCsvManager.merge(totalCsvOutput, csvOutputs.toArray(new String[csvOutputs.size()]));
		
		for(String csvOut : csvOutputs){
			new File(csvOut).delete();
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
