package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class HugeMapLandscapeMetricAnalysisFactory extends MapLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi, int noDataValue) {
		return Util.readValuesHugeRoi(coverage, roi, noDataValue);
	}

	@Override
	protected MapLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues,
			MapLandscapeMetricKernel kernel, Counting counting) {
		
		// analyse
		return new HugeMapLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

}
