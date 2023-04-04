package fr.inrae.act.bagap.chloe.analysis.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class SelectedLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private Set<Pixel> pixels;
	
	public SelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting){
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.pixels = pixels;
	}
	
	@Override
	public SelectedLandscapeMetricKernel kernel() {
		return (SelectedLandscapeMetricKernel) super.kernel();
	}
	
	public Set<Pixel> pixels(){
		return pixels;
	}
}
