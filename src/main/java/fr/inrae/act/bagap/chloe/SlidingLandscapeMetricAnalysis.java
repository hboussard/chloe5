package fr.inrae.act.bagap.chloe;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class SlidingLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	//private final GridCoverage2D coverage;
	private final Coverage coverage;
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private final int nbValues;
	
	private final int displacement;
	
	private final Counting counting;
	
	private final SlidingLandscapeMetricKernel kernel;
	
	//public SlidingLandscapeMetricAnalysis(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting){
	public SlidingLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting){
			
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
		this.displacement = displacement;
		this.kernel = kernel;
		this.counting = counting;
	}
	
	//protected GridCoverage2D coverage(){
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

	public int displacement() {
		return displacement;
	}

	@Override
	public Counting counting() {
		return counting;
	}

	public SlidingLandscapeMetricKernel kernel() {
		return kernel;
	}
	

}
