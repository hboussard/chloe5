package fr.inrae.act.bagap.chloe;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class SelectedLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	//private final GridCoverage2D coverage;
	private final Coverage coverage;
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private final int nbValues;
	
	private final Counting counting;
	
	private final SelectedLandscapeMetricKernel kernel;
	
	private Set<Pixel> pixels;
	
	public SelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting){
			
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
		this.pixels = pixels;
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

	@Override
	public Counting counting() {
		return counting;
	}

	public SelectedLandscapeMetricKernel kernel() {
		return kernel;
	}
	
	public Set<Pixel> pixels(){
		return pixels;
	}
}
