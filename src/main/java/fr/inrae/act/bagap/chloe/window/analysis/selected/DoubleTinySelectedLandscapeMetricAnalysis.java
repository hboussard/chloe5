package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.DoubleSelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class DoubleTinySelectedLandscapeMetricAnalysis extends TinySelectedLandscapeMetricAnalysis {

	private Coverage coverage2;
	
	public DoubleTinySelectedLandscapeMetricAnalysis(Coverage coverage, Coverage coverage2, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.coverage2 = coverage2;
	}
	
	@Override
	public DoubleSelectedLandscapeMetricKernel kernel() {
		return (DoubleSelectedLandscapeMetricKernel) super.kernel();
	}

	@Override
	protected void doClose() {
		super.doClose();
		
		// gestion des entrees compl�mentaires
		coverage2.dispose();
	}
	
	@Override
	protected void manageInDatas(Rectangle roi) {
		super.manageInDatas(roi);
		
		// gestion des entrees compl�mentaires
		kernel().setInDatas2(coverage2.getData(roi));
	}

}
