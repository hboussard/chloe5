package fr.inrae.act.bagap.chloe.window.analysis.grid;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.grid.GridLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyGridLandscapeMetricAnalysis extends GridLandscapeMetricAnalysis {

	private int buffer;
	
	private int gridWidth;
	
	public TinyGridLandscapeMetricAnalysis(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, GridLandscapeMetricKernel kernel, Counting counting) {		
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
		// attention bug de la récupération des données dans le coverage2D si le Y dépasse une certaine valeur
		// bizarement ce bug influence les données en X
		// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
		// pas de problème sur fichier TIF
		//Rectangle roi = new Rectangle(roiX() - bufferROIXMin(), roiY() - bufferROIYMin(), roiWidth() + bufferROIXMin() + bufferROIXMax(), roiHeight() + bufferROIYMin() + bufferROIYMax());
		Rectangle roi = new Rectangle(roiX(), roiY(), roiWidth(), roiHeight());
		
		//System.out.println("recup des donnees sur : "+roi.x+" "+roi.y+" "+roi.width+" "+roi.height);
		
		// gestion des entrees
		kernel().setInDatas(coverage().getData(roi));
		coverage().dispose();
		
		// ajustement du buffer de calcul
		buffer = kernel().gridSize();
		
		
		if(roiWidth()%kernel().gridSize() == 0){
			gridWidth = roiWidth() / kernel().gridSize();
		}else{
			gridWidth = (roiWidth() / kernel().gridSize()) + 1;
		}
		/*
		int gridHeight;
		if(roiHeight()%kernel().gridSize() == 0){
			gridHeight = roiHeight() / kernel().gridSize();
		}else{
			gridHeight = (roiHeight() / kernel().gridSize()) + 1;
		}*/
		
		// gestion des sorties
		kernel().setOutDatas(new float[gridWidth][nbValues()]);
		
		// initialisation du comptage
		counting().init();
		
		//System.out.println(roiWidth() + bufferROIXMin() + bufferROIXMax()+" "+roiHeight() + bufferROIYMin() + bufferROIYMax()+" "+(roiWidth()*roiHeight())+" "+((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1)));
	}

	@Override
	protected void doRun() {
		
		int yGrid = 0;
		for(int b=0; b<roiHeight(); b+=buffer, yGrid++){
			System.out.println("buffer "+b);
			kernel().applyGridWindow(gridWidth, b);
			
			for(int xGrid=0; xGrid<gridWidth; xGrid++){
				
				counting().setCounts(kernel().outDatas()[xGrid]);
				counting().calculate();
				counting().export(xGrid, yGrid);
			}	
		}
	}

}
