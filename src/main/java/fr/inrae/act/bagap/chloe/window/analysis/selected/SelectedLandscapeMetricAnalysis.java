package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.analysis.SingleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class SelectedLandscapeMetricAnalysis extends SingleLandscapeMetricAnalysis {
	
	private Set<Pixel> pixels;
	
	public SelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting){
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.pixels = pixels;
	}
	
	public SelectedLandscapeMetricAnalysis(Coverage[] coverages, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting){
		super(coverages, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.pixels = pixels;
	}
	
	@Override
	public SelectedLandscapeMetricKernel kernel() {
		return (SelectedLandscapeMetricKernel) super.kernel();
	}
	
	public Set<Pixel> pixels(){
		return pixels;
	}
	
	protected abstract void manageInDatas(Rectangle roi);
	
	@Override
	protected void doClose() {
		super.doClose();
		pixels = null;
	}
}
