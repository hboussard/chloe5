package fr.inrae.act.bagap.chloe.analysis.sliding;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeSlidingLandscapeMetricAnalysis extends SlidingLandscapeMetricAnalysis {
	
	private int buffer;
		
	public HugeSlidingLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		
		// la taille de tuile doit etre un modulo du deplacement  
		LandscapeMetricAnalysis.setTileYSize(LandscapeMetricAnalysis.tileYSize() - (LandscapeMetricAnalysis.tileYSize()%displacement())); 
		
		// ajustement du buffer de calcul
		buffer = (short) Math.max(displacement(), LandscapeMetricAnalysis.bufferSize());			
		
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
			
			//System.out.println("local ROI "+localROIY+" "+roiHeight()+" "+localBufferROIYMin+" "+localBufferROIYMax);
			
			// mise en place des infos pour le Kernel
			kernel().setWidth(roiWidth() + bufferROIXMin() + bufferROIXMax());
			kernel().setHeight(tYs + localBufferROIYMin + localBufferROIYMax);
			kernel().setBufferROIXMin(bufferROIXMin());
			kernel().setBufferROIXMax(bufferROIXMax());
			kernel().setBufferROIYMin(localBufferROIYMin);
			kernel().setBufferROIYMax(localBufferROIYMax);
			
			// recuperation des donnees depuis le coverage
			// attention bug de la recuperation des donnees dans le coverage2D si le Y depasse une certaine valeur qui influence les donnees en X
			// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
			// pas de probleme sur fichier TIF
			//System.out.println((roiX() - bufferROIXMin())+" "+(localROIY - localBufferROIYMin)+" "+(roiWidth() + bufferROIXMin() + bufferROIXMax())+" "+(tYs + localBufferROIYMin + localBufferROIYMax));
			roi = new Rectangle(roiX() - bufferROIXMin(), localROIY - localBufferROIYMin, roiWidth() + bufferROIXMin() + bufferROIXMax(), tYs + localBufferROIYMin + localBufferROIYMax);
			
			// gestion des entrees
			kernel().setInDatas(coverage().getDatas(roi));
			
			// gestion des sorties
			kernel().setOutDatas(new double[((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1))][nbValues()]);
			
			int nextJ = 0;
			int index;
			for(int b=0; b<tYs; b+=buffer){
				//System.out.println("buffer "+b+" "+tYs);
				
				kernel().applySlidingWindow(b, Math.min(buffer, (tYs-b)));
				kernel().get(kernel().outDatas());
				
				index = 0;
				for(int j=nextJ%buffer; j<Math.min(buffer, tYs-b); j+=displacement()){
					//System.out.println(j);
					nextJ += displacement();
					for(int i=0; i<roiWidth(); i+=displacement()){
						
						counting().setCounts(kernel().outDatas()[index]);
						counting().calculate();
						counting().export(i, localROIY+j+b);
						
						index++;
					}
				}	
			}
			
		}
		
		
		kernel().dispose();
	}

	@Override
	protected void doClose() {
		LandscapeMetricAnalysis.setTileYSize(1000);
		counting().close();
		coverage().dispose();
	}

}
