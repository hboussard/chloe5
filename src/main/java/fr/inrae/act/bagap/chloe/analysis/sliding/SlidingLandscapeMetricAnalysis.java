package fr.inrae.act.bagap.chloe.analysis.sliding;

import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class SlidingLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private final int displacement;
	
	public SlidingLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting){
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.displacement = displacement;
	}

	public int displacement() {
		return displacement;
	}

	@Override
	public SlidingLandscapeMetricKernel kernel() {
		return (SlidingLandscapeMetricKernel) super.kernel();
	}

}
