package fr.inra.sad.bagap.chloe;

import java.awt.Rectangle;

import org.geotools.coverage.grid.GridCoverage2D;

import fr.inrae.act.bagap.chloe.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public class QualitativeLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private final int nbValues;
	
	private final int displacement;
	
	private final Counting counting;
	
	private int buffer;
	
	private float[][] outDatas;
	
	private final LandscapeMetricKernel kernel;
	
	public QualitativeLandscapeMetricAnalysis(GridCoverage2D coverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, int displacement, LandscapeMetricKernel kernel, Counting counting) {
		super(coverage);
		this.nbValues = nbValues;
		this.roiX = roiX;
		this.roiY = roiY;
		this.roiWidth = roiWidth;
		this.roiHeight = roiHeight;
		this.bufferROIXMin = bufferROIXMin;
		this.bufferROIXMax = bufferROIXMax;
		this.bufferROIYMin = bufferROIYMin;
		this.bufferROIYMax = bufferROIYMax;
		this.displacement = displacement;
		this.kernel = kernel;
		this.counting = counting;
	}
	
	@Override
	protected void doInit() {
		
		kernel.setWidth(this.roiWidth + this.bufferROIXMin + this.bufferROIXMax);
		kernel.setHeight(this.roiHeight + this.bufferROIYMin + this.bufferROIYMax);
		kernel.setBufferROIXMin(this.bufferROIXMin);
		kernel.setBufferROIXMax(this.bufferROIXMax);
		kernel.setBufferROIYMin(this.bufferROIYMin);
		kernel.setBufferROIYMax(this.bufferROIYMax);
		
		// recuperation des donnees depuis le coverage
		Rectangle roi = new Rectangle(roiX - bufferROIXMin, roiY - bufferROIYMax, roiWidth + bufferROIXMin + bufferROIXMax, roiHeight + bufferROIYMin + bufferROIYMax);
		float[] inDatas = new float[roi.width * roi.height];
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
		//coverage.dispose(true); // liberation des ressources, à voir si ça marche comme ça
		kernel.setImageIn(inDatas);
		
		// ajustement du buffer de calcul
		buffer = (short) Math.max(displacement, LandscapeMetricAnalysisFactory.bufferSize());
		
		// gestion des sorties
		outDatas = new float[((((roiWidth-1)/displacement)+1)*(((buffer-1)/displacement)+1))][nbValues+2];
		kernel.setImageOut(outDatas);
		
		counting.init();
	}

	@Override
	protected void doRun() {
		int nextJ = 0;
		int index;
		for(int b=0; b<roiHeight; b+=buffer){
			System.out.println(b);
			kernel.applySlidingWindow(b, Math.min(buffer, (roiHeight-b)));
			kernel.get(outDatas);
			
			index = 0;
			for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight-b); j+=displacement){
				//System.out.println(j);
				nextJ += displacement;
				for(int i=0; i<roiWidth; i+=displacement){
					counting.setCounts(outDatas[index]);
					counting.calculate();
					counting.export(i, j+b);
					index++;
				}
			}	
		}
	}

	@Override
	protected void doClose() {
		kernel.dispose();
		counting.close();
	}

}
