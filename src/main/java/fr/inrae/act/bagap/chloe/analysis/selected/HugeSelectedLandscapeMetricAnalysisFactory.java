package fr.inrae.act.bagap.chloe.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeSelectedLandscapeMetricAnalysisFactory extends SelectedLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi) {
		return Util.readValuesHugeRoi(coverage, roi);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createSingle(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {
		
		return new HugeSelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY,
			roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax,
			nbValues, kernel, counting);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createDouble(Coverage coverage, Coverage coverage2, Set<Pixel> pixels,
			int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin,
			int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {
		
		return new DoubleHugeSelectedLandscapeMetricAnalysis(coverage, coverage2, pixels, roiX, roiY,
				roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax,
				nbValues, kernel, counting);
	}

}
