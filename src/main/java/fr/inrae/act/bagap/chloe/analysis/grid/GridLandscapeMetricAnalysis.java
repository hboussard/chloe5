package fr.inrae.act.bagap.chloe.analysis.grid;

import fr.inrae.act.bagap.chloe.analysis.SingleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class GridLandscapeMetricAnalysis extends SingleLandscapeMetricAnalysis {
	
	public GridLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, GridLandscapeMetricKernel kernel, Counting counting){
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	public GridLandscapeMetricKernel kernel() {
		return (GridLandscapeMetricKernel) super.kernel();
	}

}
