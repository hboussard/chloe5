package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class TinyMapLandscapeMetricAnalysis extends MapLandscapeMetricAnalysis {
	
	public TinyMapLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		// mise en place des infos pour le Kernel
		
		kernel().setWidth(roiWidth());
		kernel().setHeight(roiHeight());
		
		//System.out.println(bufferROIXMin()+" "+bufferROIXMax()+" "+bufferROIYMin()+" "+bufferROIYMax());		
		
		// recuperation des donnees depuis le coverage
		Rectangle roi = new Rectangle(roiX(), roiY(), roiWidth(), roiHeight());
		
		// gestion des entrees
		kernel().setInDatas(coverage().getData(roi));
		coverage().dispose();
		
		// gestion des sorties
		kernel().setOutDatas(new double[nbValues()]);
		kernel().outDatas()[0] = 1; // filtre ok
		
		// initialisation du comptage
		counting().init();
		
		// initialisation du kernel
		kernel().init();
		
		//System.out.println(roiWidth() + bufferROIXMin() + bufferROIXMax()+" "+roiHeight() + bufferROIYMin() + bufferROIYMax()+" "+(roiWidth()*roiHeight())+" "+((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1)));
	}

	@Override
	protected void doRun() {
		
		kernel().applyMapWindow(0);
		
		counting().setCounts(kernel().outDatas());
		counting().calculate();
		counting().export(0);
	}

}

