package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySlidingLandscapeMetricAnalysis extends SlidingLandscapeMetricAnalysis {

	private int buffer;
	
	public TinySlidingLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
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
		// attention bug de la récupération des données dans le coverage2D si le Y dépasse une certaine valeur
		// bizarement ce bug influence les données en X
		// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
		// pas de problème sur fichier TIF
		Rectangle roi = new Rectangle(roiX() - bufferROIXMin(), roiY() - bufferROIYMin(), roiWidth() + bufferROIXMin() + bufferROIXMax(), roiHeight() + bufferROIYMin() + bufferROIYMax());
		
		// gestion des entrees
		manageInDatas(roi);
		
		// ajustement du buffer de calcul
		buffer = (short) Math.max(displacement(), LandscapeMetricAnalysis.bufferSize());
		
		// gestion des sorties
		kernel().setOutDatas(new double[((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1))][nbValues()]);
		
		// initialisation du comptage
		counting().init();
		
	}

	@Override
	protected void doRun() {
		int nextJ = 0;
		int index;
		for(int b=0; b<roiHeight(); b+=buffer){
			System.out.println(b);
			kernel().applySlidingWindow(b, Math.min(buffer, (roiHeight()-b)));
			
			index = 0;
			for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight()-b); j+=displacement()){
				
				nextJ += displacement();
				for(int i=0; i<roiWidth(); i+=displacement()){
					
					counting().setCounts(kernel().outDatas()[index]);
					counting().calculate();
					counting().export(i, j+b);
					
					index++;
				}
			}	
		}
	}

	@Override
	protected void manageInDatas(Rectangle roi) {
		
		// gestion des entrees
		kernel().setInDatas(coverage().getData(roi));
		coverage().dispose();
	}

}
