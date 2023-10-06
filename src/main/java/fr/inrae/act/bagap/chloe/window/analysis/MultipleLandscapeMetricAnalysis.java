package fr.inrae.act.bagap.chloe.window.analysis;

import java.util.HashSet;
import java.util.Set;

public class MultipleLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	protected LandscapeMetricAnalysisBuilder builder;
	
	protected Set<LandscapeMetricAnalysis> analyses;
	
	public MultipleLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder){
		this.builder = builder;
		analyses = new HashSet<LandscapeMetricAnalysis>();
	}
	
	protected void add(LandscapeMetricAnalysis analysis){
		analyses.add(analysis);
	}

	@Override
	protected void doInit() {
		// do nothing
	}

	@Override
	protected void doRun() {
		for(LandscapeMetricAnalysis analysis : analyses){
			analysis.allRun();
		}
	}

	@Override
	protected void doClose() {
		// do nothing
	}
	
}
