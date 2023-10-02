package fr.inrae.act.bagap.chloe.analysis;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public abstract class ChloeAnalysis extends Analysis {

	private static int maxTile = 500000000;
	
	public static int maxTile(){
		return maxTile;
	}
	
}
