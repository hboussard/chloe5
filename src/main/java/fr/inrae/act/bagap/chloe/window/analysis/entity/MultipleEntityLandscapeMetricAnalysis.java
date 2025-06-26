package fr.inrae.act.bagap.chloe.window.analysis.entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;

public class MultipleEntityLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput, csvFolder;
	
	private Set<Integer> coherences;
	
	private List<String> csvOutputs;
	
	public MultipleEntityLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
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
				csvOutputs = new ArrayList<String>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics;
			
			for(int coherence : coherences){
				metrics = new HashSet<Metric>();
				metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
				
				
				for(Metric m : metrics) {
					System.out.println(m);
				}
				
				/*
				if(MetricManager.hasOnlyPatchMetric(metrics)) {
					continue;
				}
				*/
				
				if(!MetricManager.hasOnlyBasicMetric(metrics) && coherence == 0){
					continue;
				}
				builder.setMetrics(metrics);
					
				if(totalCsvOutput != null || csvFolder != null){
					builder.addCsvOutput(path+"/entity_"+coherence+".csv");
					csvOutputs.add(path+"/entity_"+coherence+".csv");
				}
					
				add(LandscapeMetricAnalysisFactory.create(builder));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doClose() {
		
		String localCsvOutput;
		if(totalCsvOutput != null) {
			localCsvOutput = totalCsvOutput;
		}else {
			String name = new File(builder.getRasterFile()).getName().replace(".tif", "").replace(".asc", "");
			localCsvOutput = csvFolder+name+".csv";
		}
		
		SpatialCsvManager.merge(localCsvOutput, csvOutputs.toArray(new String[csvOutputs.size()]));
		
		for(String csvOut : csvOutputs){
			new File(csvOut).delete();
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}
	
}
