package fr.inrae.act.bagap.chloe.analysis.map;

import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class MapLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	public MapLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight,
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting) {
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	public MapLandscapeMetricKernel kernel() {
		return (MapLandscapeMetricKernel) super.kernel();
	}
	
}
