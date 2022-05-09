package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.EmpriseBocageKernel3;
import fr.inrae.act.bagap.chloe.kernel.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class MultipleLandscapeMetricAnalysis extends SlidingLandscapeMetricAnalysis {

	private int buffer;
	
	private double[][] outDatas;
	
	private final Coverage coverage2;
	
	public MultipleLandscapeMetricAnalysis(Coverage coverage, Coverage coverage2, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting) {		
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
		this.coverage2 = coverage2;
	}
	
	@Override
	public EmpriseBocageKernel3 kernel() {
		return (EmpriseBocageKernel3) super.kernel();
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
		
		//float[] inDatas = new float[roi.width * roi.height];
		//inDatas = coverage().getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
		////coverage.dispose(true); // liberation des ressources, à voir si ça marche comme ça
		
		float[] inDatas = coverage().getDatas(roi);
		
		kernel().setImageIn(inDatas);
		
		float[] inDatas2 = coverage2.getDatas(roi);
		
		kernel().setImage2(inDatas2);
		
		// ajustement du buffer de calcul
		buffer = (short) Math.max(displacement(), LandscapeMetricAnalysisFactory.bufferSize());
		
		// gestion des sorties
		outDatas = new double[((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1))][nbValues()];
		kernel().setImageOut(outDatas);
		
		// initialisation du comptage
		counting().init();
		
		//System.out.println(roiWidth() + bufferROIXMin() + bufferROIXMax()+" "+roiHeight() + bufferROIYMin() + bufferROIYMax()+" "+(roiWidth()*roiHeight())+" "+((((roiWidth()-1)/displacement())+1)*(((buffer-1)/displacement())+1)));
		
	}

	@Override
	protected void doRun() {
		int nextJ = 0;
		int index;
		for(int b=0; b<roiHeight(); b+=buffer){
			System.out.println(b);
			kernel().applySlidingWindow(b, Math.min(buffer, (roiHeight()-b)));
			kernel().get(outDatas);
			
			index = 0;
			for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight()-b); j+=displacement()){
				//System.out.println(j);
				nextJ += displacement();
				for(int i=0; i<roiWidth(); i+=displacement()){
					
					counting().setCounts(outDatas[index]);
					counting().calculate();
					counting().export(i, j+b);
					
					index++;
				}
			}	
		}
	}

	@Override
	protected void doClose() {
		kernel().dispose();
		counting().close();
		//PlanarImage planarImage = (PlanarImage) coverage().getRenderedImage();
		//ImageUtilities.disposePlanarImageChain(planarImage);
		//coverage().dispose(true);
		coverage().dispose();
	}

}
