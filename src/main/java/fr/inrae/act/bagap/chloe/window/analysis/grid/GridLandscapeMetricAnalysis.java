package fr.inrae.act.bagap.chloe.window.analysis.grid;

import fr.inrae.act.bagap.chloe.window.analysis.SingleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public abstract class GridLandscapeMetricAnalysis extends SingleLandscapeMetricAnalysis {
	
	public GridLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, GridLandscapeMetricKernel kernel, Counting counting){
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	public GridLandscapeMetricKernel kernel() {
		return (GridLandscapeMetricKernel) super.kernel();
	}

}
