package fr.inrae.act.bagap.chloe.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.selected.DoubleSelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class DoubleHugeSelectedLandscapeMetricAnalysis extends TinySelectedLandscapeMetricAnalysis {
	
	private Coverage coverage2;
		
	public DoubleHugeSelectedLandscapeMetricAnalysis(Coverage coverage, Coverage coverage2, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {	
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
		
		// gestion des entrees complémentaires
		coverage2.dispose();
	}
	
	@Override
	protected void manageInDatas(Rectangle roi) {
		super.manageInDatas(roi);
		
		// gestion des entrees complémentaires
		kernel().setInDatas2(coverage2.getDatas(roi));
	}

}
