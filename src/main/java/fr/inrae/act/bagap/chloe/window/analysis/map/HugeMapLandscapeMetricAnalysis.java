package fr.inrae.act.bagap.chloe.window.analysis.map;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeMapLandscapeMetricAnalysis extends MapLandscapeMetricAnalysis {
	
	public HugeMapLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		
		// mise en place des infos pour le Kernel
		kernel().setWidth(roiWidth());
		
		// gestion des sorties
		kernel().setOutDatas(new double[nbValues()]);
		kernel().outDatas()[0] = 1; // filtre ok
		
		// initialisation du comptage
		counting().init();
		
		// initialisation du kernel
		kernel().init();
	}

	@Override
	protected void doRun() {
		
		int tYs;
		Rectangle roi;
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=LandscapeMetricAnalysis.tileYSize()){
			
			//System.out.println("pass "+localROIY);
			
			tYs = Math.min(LandscapeMetricAnalysis.tileYSize(), roiHeight() + roiY() - localROIY );
			
			kernel().setHeight(tYs);
			
			// recuperation des donnees depuis le coverage
			roi = new Rectangle(roiX(), localROIY, roiWidth(), tYs);
			
			// gestion des entrees
			kernel().setInDatas(coverage().getData(roi));
			
			// analyse
			kernel().applyMapWindow(localROIY);
		}
		
		// export counting
		counting().setCounts(kernel().outDatas());
		counting().calculate();
		counting().export(0);
	}
	
	@Override
	protected void doClose() {
		super.doClose();
		coverage().dispose();
	}

}

