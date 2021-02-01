package fr.inrae.act.bagap.chloe;

import java.util.List;

public class HugeQualitativeLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private List<QualitativeLandscapeMetricAnalysis> analyses;
	
	public HugeQualitativeLandscapeMetricAnalysis(List<QualitativeLandscapeMetricAnalysis> analyses) {
		this.analyses = analyses;
	}

	@Override
	protected void doRun() {
		for(QualitativeLandscapeMetricAnalysis analyse : analyses){
			analyse.allRun();
		}
	}

	@Override
	protected void doInit() {
		// do nothing	
	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
