package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public class HugeLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private int buffer;
	
	private float[][] outDatas;
	
	private int tileYSize = 500;
	
	public HugeLandscapeMetricAnalysis(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, LandscapeMetricKernel kernel, Counting counting) {
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
	}
	
	@Override
	protected void doInit() {
		
		// la taille de tuile doit etre un modulo du deplacement  
		tileYSize -= tileYSize%displacement(); 
		
		// ajustement du buffer de calcul
		buffer = (short) Math.max(displacement(), LandscapeMetricAnalysisFactory.bufferSize());			
		
		// intitialisation du comptage et des sorties associées
		counting().init();
	}

	@Override
	protected void doRun() {
		
		int midWindowSize = (int) (kernel().windowSize()/2);
		
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=tileYSize){
			
			int localBufferROIYMin = Math.min(localROIY+bufferROIYMin(), midWindowSize);
			int localBufferROIYMax = Math.min(Math.max(bufferROIYMax(), roiHeight()+bufferROIYMax()-(localROIY+tileYSize)), midWindowSize);
			int tYs = Math.min(tileYSize, roiHeight() + roiY() - localROIY );
			
			System.out.println("nouveau local ROI "+localROIY+" "+roiHeight()+" "+localBufferROIYMin+" "+localBufferROIYMax);
			
			// mise en place des infos pour le Kernel
			kernel().setWidth(roiWidth() + bufferROIXMin() + bufferROIXMax());
			kernel().setHeight(tYs + localBufferROIYMin + localBufferROIYMax);
			kernel().setBufferROIXMin(bufferROIXMin());
			kernel().setBufferROIXMax(bufferROIXMax());
			kernel().setBufferROIYMin(localBufferROIYMin);
			kernel().setBufferROIYMax(localBufferROIYMax);
			
			// recuperation des donnees depuis le coverage
			// attention bug de la récupération des données dans le coverage2D si le Y dépasse une certaine valeur qui influence les données en X
			// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
			// pas de problème sur fichier TIF
			//System.out.println((roiX() - bufferROIXMin())+" "+(localROIY - localBufferROIYMin)+" "+(roiWidth() + bufferROIXMin() + bufferROIXMax())+" "+(tYs + localBufferROIYMin + localBufferROIYMax));
			Rectangle roi = new Rectangle(roiX() - bufferROIXMin(), localROIY - localBufferROIYMin, roiWidth() + bufferROIXMin() + bufferROIXMax(), tYs + localBufferROIYMin + localBufferROIYMax);
			float[] inDatas = new float[roi.width * roi.height];
			inDatas = coverage().getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
			//coverage.dispose(true); // liberation des ressources, à voir si ça marche comme ça
			kernel().setImageIn(inDatas);
			
			// gestion des sorties
			outDatas = new float[((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1))][nbValues()];
			kernel().setImageOut(outDatas);
			
			int nextJ = 0;
			int index;
			for(int b=0; b<tYs; b+=buffer){
				System.out.println("buffer "+b+" "+tYs);
				//System.out.println(b+" "+Math.min(buffer, (tYs-b)));
				kernel().applySlidingWindow(b, Math.min(buffer, (tYs-b)));
				kernel().get(outDatas);
				
				index = 0;
				for(int j=nextJ%buffer; j<Math.min(buffer, tYs-b); j+=displacement()){
					//System.out.println(j);
					//System.out.println("count "+(count++)+" nextJ "+nextJ+" ad "+(localROIY+j+b)+" loalROIY "+localROIY+" j "+j+" b "+b+" buffer "+buffer+" tYs "+tYs);
					nextJ += displacement();
					for(int i=0; i<roiWidth(); i+=displacement()){
						counting().setCounts(outDatas[index]);
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
		counting().close();
		PlanarImage planarImage = (PlanarImage) coverage().getRenderedImage();
		ImageUtilities.disposePlanarImageChain(planarImage);
		coverage().dispose(true);
	}

}
