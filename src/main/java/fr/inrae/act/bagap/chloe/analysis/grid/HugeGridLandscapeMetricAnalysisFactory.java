package fr.inrae.act.bagap.chloe.analysis.grid;

import java.awt.Rectangle;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeGridLandscapeMetricAnalysisFactory extends GridLandscapeMetricAnalysisFactory {
	
	@Override
	protected int[] readValues(Coverage coverage, Rectangle roi) {
		return Util.readValuesHugeRoi(coverage, roi);
	}

	@Override
	protected GridLandscapeMetricAnalysis create(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues,
			GridLandscapeMetricKernel kernel, Counting counting) {
		
		// analysis
		return new HugeGridLandscapeMetricAnalysis(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);			
	}
	
}
