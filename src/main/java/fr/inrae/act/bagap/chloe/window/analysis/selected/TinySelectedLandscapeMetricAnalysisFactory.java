package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysisFactory extends SelectedLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi, int noDataValue) {
		return Util.readValuesTinyRoi(coverage, roi, noDataValue);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createSingle(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {

		return new TinySelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	protected SelectedLandscapeMetricAnalysis createMultiple(Coverage[] coverages, Set<Pixel> pixels,
			int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin,
			int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {
		
		return new TinySelectedLandscapeMetricAnalysis(coverages, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
}
