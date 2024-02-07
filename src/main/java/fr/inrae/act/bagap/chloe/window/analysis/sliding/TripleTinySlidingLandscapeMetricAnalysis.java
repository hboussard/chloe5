package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.TripleSlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TripleTinySlidingLandscapeMetricAnalysis extends DoubleTinySlidingLandscapeMetricAnalysis {
	
	private Coverage coverage3;
	
	public TripleTinySlidingLandscapeMetricAnalysis(Coverage coverage, Coverage coverage2, Coverage coverage3, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, coverage2, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
		this.coverage3 = coverage3;
	}
	
	@Override
	public TripleSlidingLandscapeMetricKernel kernel() {
		return (TripleSlidingLandscapeMetricKernel) super.kernel();
	}
	
	@Override
	protected void manageInDatas(Rectangle roi) {
		super.manageInDatas(roi);
		
		// gestion des entrees compl�mentaires
		kernel().setInDatas3(coverage3.getData(roi));
		coverage3.dispose();
	}

}
