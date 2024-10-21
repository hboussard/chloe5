package fr.inrae.act.bagap.chloe.window.analysis.map;

import fr.inrae.act.bagap.chloe.window.analysis.SingleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public abstract class MapLandscapeMetricAnalysis extends SingleLandscapeMetricAnalysis {

	public MapLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting) {
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	public MapLandscapeMetricKernel kernel() {
		return (MapLandscapeMetricKernel) super.kernel();
	}
	
}
