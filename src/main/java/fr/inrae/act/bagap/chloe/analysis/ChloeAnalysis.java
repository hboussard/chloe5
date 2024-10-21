package fr.inrae.act.bagap.chloe.analysis;

import fr.inrae.act.bagap.apiland.analysis.Analysis;

public abstract class ChloeAnalysis extends Analysis {

	private static int maxTile = 400000000;
	
	public static int maxTile(){
		return maxTile;
	}
	
}
