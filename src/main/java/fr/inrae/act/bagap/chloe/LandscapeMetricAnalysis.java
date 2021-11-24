package fr.inrae.act.bagap.chloe;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.chloe.counting.Counting;

public abstract class LandscapeMetricAnalysis extends Analysis {

	public abstract Counting counting();
	
	
}
