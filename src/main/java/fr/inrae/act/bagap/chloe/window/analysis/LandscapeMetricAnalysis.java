package fr.inrae.act.bagap.chloe.window.analysis;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public abstract class LandscapeMetricAnalysis extends Analysis {
	
	private static int maxTile = 500000000;
	
	private static int tileYSize = 1000;
	
	private static int bufferSize = 200;
	
	public static int maxTile(){
		return maxTile;
	}
	
	public static int tileYSize(){
		return tileYSize;
	}
	
	public static void setTileYSize(int tileYS){
		tileYSize = tileYS;
	}
	
	public static int bufferSize(){
		return bufferSize;
	}

	public static void setBufferSize(int buff){
		bufferSize = buff;
	}
	
	// fonctions utilitaires
	
	public static int getDisplacement(double inCellSize, double outCellSize){
		int displacement = 1;
		if(inCellSize != outCellSize){
			displacement = (int) (outCellSize/inCellSize);
		}
		return displacement;
	}

	public static int getWindowSize(float inCellSize, double windowRadius) {
		int windowSize = (int) (2*windowRadius / inCellSize);
		if(windowSize%2 == 0){
			windowSize++;
		}
		return windowSize;
	}
	
}
