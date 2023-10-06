package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
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
	
	private Set<String> csvOutputs;
	
	public MultipleMapLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
		super(builder);
	}
	
	@Override
	protected void doInit() {
		
		try {
		
			totalMetrics = builder.getMetrics();
			totalCsvOutput = builder.getCsv();
			String path = new File(totalCsvOutput).getParent();
			csvOutputs = new HashSet<String>();
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics = new HashSet<Metric>();
			for(int coherence : coherences){
				metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
				if(coherence == 0){
					continue;
				}
				builder.setMetrics(metrics);
				builder.addCsvOutput(path+"/map_"+coherence+".csv");
				csvOutputs.add(path+"/map_"+coherence+".csv");
				
				add(LandscapeMetricAnalysisFactory.create(builder));
			
				metrics = new HashSet<Metric>();
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
		
		SpatialCsvManager.merge(totalCsvOutput, csvOutputs.toArray(new String[csvOutputs.size()]), new HashSet<String>(Arrays.asList(new String[]{"name"})));
		
		for(String csvOut : csvOutputs){
			new File(csvOut).delete();
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
