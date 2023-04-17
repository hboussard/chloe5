package fr.inrae.act.bagap.chloe.analysis.selected;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysis extends SelectedLandscapeMetricAnalysis {
	
	public TinySelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		// mise en place des infos pour le Kernel
		kernel().setWidth(roiWidth() + bufferROIXMin() + bufferROIXMax());
		kernel().setHeight(roiHeight() + bufferROIYMin() + bufferROIYMax());
		kernel().setBufferROIXMin(bufferROIXMin());
		kernel().setBufferROIXMax(bufferROIXMax());
		kernel().setBufferROIYMin(bufferROIYMin());
		kernel().setBufferROIYMax(bufferROIYMax());
		
		// recuperation des donnees depuis le coverage
		Rectangle roi = new Rectangle(roiX() - bufferROIXMin(), roiY() - bufferROIYMin(), roiWidth() + bufferROIXMin() + bufferROIXMax(), roiHeight() + bufferROIYMin() + bufferROIYMax());
		
		// gestion des entrees
		manageInDatas(roi);
		
		// gestion des sorties
		Map<Pixel, double[]> outDatas = new HashMap<Pixel, double[]>();
		for(Pixel p : pixels()){
			outDatas.put(p, new double[nbValues()]);
		}
		kernel().setOutDatas(outDatas);
		
		// initialisation du comptage
		counting().init();
	}
	
	@Override
	protected void doRun() {
		
		kernel().applySelectedWindow(roiHeight(), 0);
		
		for(Pixel p : pixels()){
			counting().setCounts(kernel().outDatas().get(p));
			counting().calculate();
			counting().export(p.x(), p.y());
		}
	}

	@Override
	protected void manageInDatas(Rectangle roi) {
		// gestion des entrees
		kernel().setInDatas(coverage().getDatas(roi));
		coverage().dispose();
	}

}
