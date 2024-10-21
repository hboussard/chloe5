package fr.inrae.act.bagap.chloe.window.analysis;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.LandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public abstract class SingleLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	private final Coverage[] coverages;
	
	/**
	 * coordonnees en pixels (X, Y) du premier pixel en haut � gauche du ROI a analyser
	 * info relative par rapport au coverage
	 */
	private final int roiX, roiY; 
	
	/**
	 * dimensions en pixels du ROI a analyser
	 * info relative par rapport au coverage
	 */
	private final int roiWidth, roiHeight;
	
	/**
	 * decalage en nombre de pixels a gauche (XMin), a droite (XMax), en haut (YMin) et en bas (YMax)
	 * a prendre en compte autour du ROI pour enlever les effets de bords
	 */
	private final int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private final int nbValues;
	
	private final LandscapeMetricKernel kernel;
	
	private final Counting counting;
	
	public SingleLandscapeMetricAnalysis(Coverage[] coverages, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, LandscapeMetricKernel kernel, Counting counting){
		this.coverages = coverages;
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
	
	public SingleLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, LandscapeMetricKernel kernel, Counting counting){
		this(new Coverage[] {coverage}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	protected Coverage[] coverages(){
		return coverages;
	}
	
	protected Coverage coverage(){
		return coverages[0];
	}
	
	protected Coverage coverage(int index){
		return coverages[index-1];
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
	
	@Override
	protected void doClose() {
		kernel().dispose();
		counting().close();
	}
}
