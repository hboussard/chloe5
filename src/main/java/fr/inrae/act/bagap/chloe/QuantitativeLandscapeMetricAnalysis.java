package fr.inrae.act.bagap.chloe;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public class QuantitativeLandscapeMetricAnalysis extends LandscapeMetricAnalysis {
	
	private final int roiWidth, roiHeight;
	
	private final int displacement;
	
	private final Counting counting;
	
	private int buffer = 100; // default
	
	private float[][] outDatas;
	
	private final LandscapeMetricKernel kernel;
	
	public QuantitativeLandscapeMetricAnalysis(int roiWidth, int roiHeight, int displacement, LandscapeMetricKernel kernel, Counting counting) {
		this.roiWidth = roiWidth;
		this.roiHeight = roiHeight;
		this.displacement = displacement;
		this.kernel = kernel;
		this.counting = counting;
	}
	
	@Override
	protected void doInit() {
		
		// ajustement du buffer
		buffer = (short) Math.max(displacement, buffer);
		
		// gestion des sorties
		outDatas = new float[((((roiWidth-1)/displacement)+1)*(((buffer-1)/displacement)+1))][3];
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
