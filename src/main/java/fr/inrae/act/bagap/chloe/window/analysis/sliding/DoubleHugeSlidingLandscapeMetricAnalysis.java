package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.DoubleSlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class DoubleHugeSlidingLandscapeMetricAnalysis extends HugeSlidingLandscapeMetricAnalysis {
	
	private Coverage coverage2;
		
	public DoubleHugeSlidingLandscapeMetricAnalysis(Coverage coverage, Coverage coverage2, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
		this.coverage2 = coverage2;
	}
	
	@Override
	public DoubleSlidingLandscapeMetricKernel kernel() {
		return (DoubleSlidingLandscapeMetricKernel) super.kernel();
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
		kernel().setInDatas2(coverage2.getDatas(roi));
	}

}