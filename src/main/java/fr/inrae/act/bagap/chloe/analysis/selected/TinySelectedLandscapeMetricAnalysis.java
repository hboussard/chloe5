package fr.inrae.act.bagap.chloe.analysis.selected;

import java.awt.Rectangle;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysis extends SelectedLandscapeMetricAnalysis {

	private int buffer;
	
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
		
		float[] inDatas = coverage().getDatas(roi);
		coverage().dispose();
		kernel().setInDatas(inDatas);
		
		// ajustement du buffer de calcul
		buffer = LandscapeMetricAnalysis.bufferSize();
		
		// gestion des sorties
		kernel().setOutDatas(new double[(((roiWidth()-1)+1)*((buffer-1)+1))][nbValues()]);
		
		// initialisation du comptage
		counting().init();
	}

	@Override
	protected void doRun() {
		int nextJ = 0;
		int index;
		for(int b=0; b<roiHeight(); b+=buffer){
			//System.out.println(b);
			kernel().applySelectedWindow(b, Math.min(buffer, (roiHeight()-b)));
			//kernel().get(outDatas);
			
			index = 0;
			for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight()-b); j++){
				//System.out.println(j);
				nextJ++;
				for(int i=0; i<roiWidth(); i++){
					
					Pixel p = new Pixel(i, j+b);
					if(pixels().contains(p)){
						//System.out.println("ici "+i+" "+(j+b));
						
						counting().setCounts(kernel().outDatas()[index]);
						counting().calculate();
						counting().export(i, j+b);
					}
					
					index++;
				}
			}	
		}
	}

	@Override
	protected void doClose() {
		kernel().dispose();
		counting().close();
	}

}
