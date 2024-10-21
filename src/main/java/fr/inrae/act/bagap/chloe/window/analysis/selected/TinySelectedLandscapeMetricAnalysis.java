package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysis extends SelectedLandscapeMetricAnalysis {
	
	public TinySelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	public TinySelectedLandscapeMetricAnalysis(Coverage[] coverages, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting) {		
		super(coverages, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
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
			//counting().export(p.x(), p.y());
			counting().export(p);
		}
	}

	@Override
	protected void manageInDatas(Rectangle roi) {
		// gestion des entrees
		float[][] inDatas = new float[coverages().length][];
		for(int i=0; i<coverages().length; i++) {
			inDatas[i] = coverages()[i].getData(roi);
			coverages()[i].dispose();
		}
		kernel().setInDatas(inDatas);
		/*
		// gestion des entrees
		kernel().setInDatas(coverage().getData(roi));
		coverage().dispose();
		*/
	}

}
