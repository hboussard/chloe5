package fr.inrae.act.bagap.chloe.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySlidingLandscapeMetricAnalysisFactory extends SlidingLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi) {
		return Util.readValuesTinyRoi(coverage, roi);
	}

	@Override
	protected SlidingLandscapeMetricAnalysis createSingle(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement,
			SlidingLandscapeMetricKernel kernel, Counting counting) {
		return new TinySlidingLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,
				bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}

	@Override
	protected SlidingLandscapeMetricAnalysis createDouble(Coverage coverage, Coverage coverage2, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {
		return new DoubleTinySlidingLandscapeMetricAnalysis(coverage, coverage2, roiX, roiY, roiWidth,
				roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel,
				counting);
	}

}
