package fr.inrae.act.bagap.chloe.window.analysis.grid;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeGridLandscapeMetricAnalysis extends GridLandscapeMetricAnalysis {

	private int buffer;
	
	public HugeGridLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, GridLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		//System.out.println("cretor "+roiX+" "+roiY+" "+roiWidth+" "+roiHeight);
	}
	
	@Override
	protected void doInit() {
		
		// la taille de tuile doit etre un modulo de la taille de la grille
		LandscapeMetricAnalysis.setTileYSize(LandscapeMetricAnalysis.tileYSize() - (LandscapeMetricAnalysis.tileYSize()%kernel().gridSize())); 
				
		// ajustement du buffer de calcul
		buffer = kernel().gridSize();			
				
		// intitialisation du comptage et des sorties associées
		counting().init();
	}

	@Override
	protected void doRun() {
		
		int gridWidth;
		if(roiWidth()%kernel().gridSize() == 0){
			gridWidth = roiWidth() / kernel().gridSize();
		}else{
			gridWidth = (roiWidth() / kernel().gridSize()) + 1;
		}
		
		// gestion des sorties
		kernel().setOutDatas(new double[gridWidth][nbValues()]);
		
		int yGrid, tYs, localBufferROIYMin, localBufferROIYMax;
		Rectangle roi;
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=LandscapeMetricAnalysis.tileYSize()){
			
			//System.out.println(localROIY+" "+roiY()+" "+roiHeight()+" "+LandscapeMetricAnalysis.tileYSize());
			localBufferROIYMin = localROIY+bufferROIYMin();
			localBufferROIYMax = Math.max(bufferROIYMax(), roiHeight()+bufferROIYMax()-(localROIY+LandscapeMetricAnalysis.tileYSize()));
			tYs = Math.min(LandscapeMetricAnalysis.tileYSize(), roiHeight() + roiY() - localROIY );
			
			// mise en place des infos pour le Kernel
			
			kernel().setWidth(roiWidth());
			kernel().setHeight(tYs);
			kernel().setBufferROIXMin(bufferROIXMin());
			kernel().setBufferROIXMax(bufferROIXMax());
			kernel().setBufferROIYMin(localBufferROIYMin);
			kernel().setBufferROIYMax(localBufferROIYMax);
			
			// recuperation des donnees depuis le coverage
			roi = new Rectangle(roiX(), localROIY, roiWidth(), tYs);
			
			//System.out.println(roiX()+" "+localROIY+" "+roiWidth()+" "+tYs);
			
			// gestion des entrees
			kernel().setInDatas(coverage().getDatas(roi));
			
			yGrid = 0;
			for(int b=0; b<tYs; b+=buffer, yGrid++){
				//System.out.println("buffer "+b);
				kernel().applyGridWindow(gridWidth, b);
				kernel().get(kernel().outDatas());
				
				for(int xGrid=0; xGrid<gridWidth; xGrid++){
					
					counting().setCounts(kernel().outDatas()[xGrid]);
					counting().calculate();
					counting().export(xGrid, yGrid);
				}	
			}
		}
	}

	@Override
	protected void doClose() {
		super.doClose();
		coverage().dispose();
	}

}
