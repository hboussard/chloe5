package fr.inrae.act.bagap.chloe.analysis.map;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.analysis.grid.GridLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.map.MapLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyMapLandscapeMetricAnalysis extends MapLandscapeMetricAnalysis {

	private int buffer;
	
	public TinyMapLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, MapLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		// mise en place des infos pour le Kernel
		
		kernel().setWidth(roiWidth());
		kernel().setHeight(roiHeight());
		kernel().setBufferROIXMin(bufferROIXMin());
		kernel().setBufferROIXMax(bufferROIXMax());
		kernel().setBufferROIYMin(bufferROIYMin());
		kernel().setBufferROIYMax(bufferROIYMax());
		
		//System.out.println(bufferROIXMin()+" "+bufferROIXMax()+" "+bufferROIYMin()+" "+bufferROIYMax());		
		
		// recuperation des donnees depuis le coverage
		Rectangle roi = new Rectangle(roiX(), roiY(), roiWidth(), roiHeight());
		
		// gestion des entrees
		kernel().setInDatas(coverage().getDatas(roi));
		coverage().dispose();
		
		// gestion des sorties
		kernel().setOutDatas(new double[nbValues()]);
		
		// initialisation du comptage
		counting().init();
		
		buffer = LandscapeMetricAnalysis.bufferSize();
		//System.out.println(roiWidth() + bufferROIXMin() + bufferROIXMax()+" "+roiHeight() + bufferROIYMin() + bufferROIYMax()+" "+(roiWidth()*roiHeight())+" "+((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1)));
	}

	@Override
	protected void doRun() {
		
		for(int b=0; b<roiHeight(); b+=buffer){
			System.out.println("buffer "+b);
			kernel().applyMapWindow(b);
		}
		
		counting().setCounts(kernel().outDatas());
		counting().calculate();
		counting().export(0);
	}

	@Override
	protected void doClose() {
		kernel().dispose();
		counting().close();
	}

}

