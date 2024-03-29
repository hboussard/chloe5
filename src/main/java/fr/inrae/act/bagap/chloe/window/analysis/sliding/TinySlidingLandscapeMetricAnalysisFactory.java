package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySlidingLandscapeMetricAnalysisFactory extends SlidingLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi, int noDataValue) {
		return Util.readValuesTinyRoi(coverage, roi, noDataValue);
	}

	@Override
	protected SlidingLandscapeMetricAnalysis createSingle(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement,
			SlidingLandscapeMetricKernel kernel, Counting counting) {
		return new TinySlidingLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,
				bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}
	
	@Override
	protected SlidingLandscapeMetricAnalysis createMultiple(Coverage[] coverages, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement,
			SlidingLandscapeMetricKernel kernel, Counting counting) {
		return new TinySlidingLandscapeMetricAnalysis(coverages, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,
				bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}

}
