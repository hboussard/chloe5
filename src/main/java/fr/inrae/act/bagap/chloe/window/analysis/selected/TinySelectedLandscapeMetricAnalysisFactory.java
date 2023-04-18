package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysisFactory extends SelectedLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi) {
		return Util.readValuesTinyRoi(coverage, roi);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createSingle(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {

		return new TinySelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createDouble(Coverage coverage, Coverage coverage2, Set<Pixel> pixels,
			int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin,
			int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {
		
		return new DoubleTinySelectedLandscapeMetricAnalysis(coverage, coverage2, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
}
