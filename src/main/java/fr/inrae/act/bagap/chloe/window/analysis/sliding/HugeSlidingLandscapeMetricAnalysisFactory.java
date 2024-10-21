package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class HugeSlidingLandscapeMetricAnalysisFactory extends SlidingLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi, int noDataValue) {
		return Util.readValuesHugeRoi(coverage, roi, noDataValue);
	}

	@Override
	protected SlidingLandscapeMetricAnalysis createSingle(Coverage coverage, int roiX, int roiY, int roiWidth,
			int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb,
			int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {
		
		return new HugeSlidingLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,
				bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}
	
	@Override
	protected SlidingLandscapeMetricAnalysis createMultiple(Coverage[] coverages, int roiX, int roiY, int roiWidth,
			int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb,
			int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {
		
		return new HugeSlidingLandscapeMetricAnalysis(coverages, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,
				bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}

	/*
	@Override
	protected SlidingLandscapeMetricAnalysis createDouble(Coverage coverage, Coverage coverage2, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {
		
		return new DoubleHugeSlidingLandscapeMetricAnalysis(coverage, coverage2, roiX, roiY, roiWidth,
				roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, displacement, kernel, counting);
	}

	@Override
	protected SlidingLandscapeMetricAnalysis createTriple(Coverage coverage, Coverage coverage2, Coverage coverage3,
			int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin,
			int bufferROIYMax, int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}
