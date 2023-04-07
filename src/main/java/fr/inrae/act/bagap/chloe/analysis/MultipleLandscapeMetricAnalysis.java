package fr.inrae.act.bagap.chloe.analysis;

import java.util.HashSet;
import java.util.Set;

public class MultipleLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	private Set<LandscapeMetricAnalysis> analyses;
	
	public MultipleLandscapeMetricAnalysis(){
		analyses = new HashSet<LandscapeMetricAnalysis>();
	}
	
	public void add(LandscapeMetricAnalysis analysis){
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
