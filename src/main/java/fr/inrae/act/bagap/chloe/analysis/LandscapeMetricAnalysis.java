package fr.inrae.act.bagap.chloe.analysis;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class LandscapeMetricAnalysis extends Analysis {
	
	private static int maxTile = 500000000;
	
	private static int tileYSize = 1000;
	
	private static int bufferSize = 100;
	
	private final Coverage coverage;
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private final int nbValues;
	
	private final LandscapeMetricKernel kernel;
	
	private final Counting counting;
	
	public LandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, LandscapeMetricKernel kernel, Counting counting){
		this.coverage = coverage;
		this.roiX = roiX;
		this.roiY = roiY;
		this.roiWidth = roiWidth;
		this.roiHeight = roiHeight;
		this.bufferROIXMin = bufferROIXMin;
		this.bufferROIXMax = bufferROIXMax;
		this.bufferROIYMin = bufferROIYMin;
		this.bufferROIYMax = bufferROIYMax;
		this.nbValues = nbValues;
		this.kernel = kernel;
		this.counting = counting;
	}
	
	protected Coverage coverage(){
		return coverage;
	}
	
	public int roiX() {
		return roiX;
	}

	public int roiY() {
		return roiY;
	}

	public int roiWidth() {
		return roiWidth;
	}

	public int roiHeight() {
		return roiHeight;
	}

	public int bufferROIXMin() {
		return bufferROIXMin;
	}

	public int bufferROIXMax() {
		return bufferROIXMax;
	}

	public int bufferROIYMin() {
		return bufferROIYMin;
	}

	public int bufferROIYMax() {
		return bufferROIYMax;
	}

	public int nbValues() {
		return nbValues;
	}

	public Counting counting() {
		return counting;
	}

	public LandscapeMetricKernel kernel() {
		return kernel;
	}
	
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
	
}
