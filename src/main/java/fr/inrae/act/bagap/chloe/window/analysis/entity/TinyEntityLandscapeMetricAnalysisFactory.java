package fr.inrae.act.bagap.chloe.window.analysis.entity;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyEntityLandscapeMetricAnalysisFactory extends EntityLandscapeMetricAnalysisFactory {

	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi, int noDataValue) {
		return Util.readValuesTinyRoi(coverage, roi, noDataValue);
	}

	@Override
	protected EntityLandscapeMetricAnalysis create(Coverage coverage, Coverage entityCoverage, int roiX, int roiY,
			int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax,
			int nb, EntityLandscapeMetricKernel kernel, Counting counting) {

		return new TinyEntityLandscapeMetricAnalysis(coverage, entityCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nb, kernel, counting);
	}

}
