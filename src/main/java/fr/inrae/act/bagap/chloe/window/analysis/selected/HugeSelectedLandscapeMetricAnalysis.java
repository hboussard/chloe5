package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeSelectedLandscapeMetricAnalysis extends SelectedLandscapeMetricAnalysis {

	public HugeSelectedLandscapeMetricAnalysis(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth,
			int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues,
			SelectedLandscapeMetricKernel kernel, Counting counting) {
		super(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax,
				nbValues, kernel, counting);
	}

	@Override
	protected void doInit() {
				
		// gestion des sorties
		Map<Pixel, double[]> outDatas = new HashMap<Pixel, double[]>();
		for(Pixel p : pixels()){
			outDatas.put(p, new double[nbValues()]);
		}
		kernel().setOutDatas(outDatas);
		
		// intitialisation du comptage et des sorties associées
		counting().init();
	}

	@Override
	protected void doRun() {

		int midWindowSize = (int) (kernel().windowSize()/2);
		Rectangle roi;
		int localBufferROIYMin, localBufferROIYMax, tYs;
		
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=LandscapeMetricAnalysis.tileYSize()){
			
			localBufferROIYMin = Math.min(localROIY+bufferROIYMin(), midWindowSize);
			localBufferROIYMax = Math.min(Math.max(bufferROIYMax(), roiHeight()+bufferROIYMax()-(localROIY+LandscapeMetricAnalysis.tileYSize())), midWindowSize);
			tYs = Math.min(LandscapeMetricAnalysis.tileYSize(), roiHeight() + roiY() - localROIY );
			
			// mise en place des infos pour le Kernel
			kernel().setWidth(roiWidth() + bufferROIXMin() + bufferROIXMax());
			kernel().setHeight(tYs + localBufferROIYMin + localBufferROIYMax);
			kernel().setBufferROIXMin(bufferROIXMin());
			kernel().setBufferROIXMax(bufferROIXMax());
			kernel().setBufferROIYMin(localBufferROIYMin);
			kernel().setBufferROIYMax(localBufferROIYMax);
			
			// recuperation des donnees depuis le coverage
			roi = new Rectangle(roiX() - bufferROIXMin(), localROIY - localBufferROIYMin, roiWidth() + bufferROIXMin() + bufferROIXMax(), tYs + localBufferROIYMin + localBufferROIYMax);
			
			// gestion des entrees
			manageInDatas(roi);
			
			kernel().applySelectedWindow(tYs, localROIY);
		}
		
		for(Pixel p : pixels()){
			counting().setCounts(kernel().outDatas().get(p));
			counting().calculate();
			counting().export(p.x(), p.y());
		}
	}

	@Override
	protected void doClose() {
		super.doClose();
		coverage().dispose();
	}
	
	@Override
	protected void manageInDatas(Rectangle roi) {
		// gestion des entrees
		kernel().setInDatas(coverage().getDatas(roi));
	}

}
