package fr.inrae.act.bagap.chloe.analysis;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.chloe.counting.Counting;

public abstract class LandscapeMetricAnalysis extends Analysis {

	private static int maxTile = 500000000;
	
	public static int maxTile(){
		return maxTile;
	}
	
	private static int tileYSize = 1000;
	
	public static int tileYSize(){
		return tileYSize;
	}
	
	public static void setTileYSize(int tileYS){
		tileYSize = tileYS;
	}
	
	private static int bufferSize = 100;
	
	public static int bufferSize(){
		return bufferSize;
	}

	public static void setBufferSize(int buff){
		bufferSize = buff;
	}
	
	public abstract Counting counting();
	
}
