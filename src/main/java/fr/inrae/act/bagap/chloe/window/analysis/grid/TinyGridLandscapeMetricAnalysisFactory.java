package fr.inrae.act.bagap.chloe.window.analysis.grid;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyGridLandscapeMetricAnalysisFactory extends GridLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi) {
		return Util.readValuesTinyRoi(coverage, roi);
	}

	@Override
	protected GridLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues,
			GridLandscapeMetricKernel kernel, Counting counting) {

		// analysis
		return new TinyGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
}