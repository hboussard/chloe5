package fr.inrae.act.bagap.chloe.api;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;

public class PluginProgression implements AnalysisObserver {

	private int total;
	
	private int actual;
	
	public PluginProgression(int total){
		this.total = total;
		this.actual = 0;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}

	@Override
	public void updateProgression(Analysis a, int progression) {
		int v = progression * 100 / total;
		if(v > actual){
			actual = v;
			System.out.println("#"+actual);
		}
	}

}
