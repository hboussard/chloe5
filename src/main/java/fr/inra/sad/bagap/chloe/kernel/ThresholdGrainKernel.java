package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class ThresholdGrainKernel extends Kernel {

	private final int width, height;
	
	private final float noDataValue;
	
	private final int dep;

	private final float[] imageIn;
	
	private final float[][] imageOut;
	
	private final short[] shape;
	
	private final int windowSize;
	
	private final int enveloppeInterne; // in pixels
	
	private int theY;
	
	private final float threshold = 100;
	
	public ThresholdGrainKernel(int windowSize, short[] shape, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.shape = shape;
		this.width = width;
		this.height = height;
		this.dep = dep;
		this.imageIn = imageIn;
		this.imageOut = imageOut;
		this.noDataValue = noDataValue;
		this.enveloppeInterne = enveloppeInterne;
	}

	public void processPixel(int x, int y, int localY) {
		if(x%dep == 0 && y%dep == 0){
			
			int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
			
			// phase d'initialisation de la structure de données
			for(int i=0; i<3; i++){
				imageOut[ind][i] = 0.0f;
			}
			// fin initialisation de la structure de données
			
			if(!(x < enveloppeInterne || (width - x) < enveloppeInterne || y <enveloppeInterne || (height - y) < enveloppeInterne)) {
				int mid = windowSize / 2;
				int ic;
				float v;
				int nb = 0;
				float sum = 0;
				
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height)){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width)){
								ic = ((dy+mid) * windowSize) + (dx+mid);
								if(shape[ic] == 1){
									v = imageIn[((y + dy) * width) + (x + dx)];
									if(v == noDataValue) {
										imageOut[ind][0] = imageOut[ind][0] + 1.0f;
									}else{
										nb = nb + 1;
										if(v > threshold){
											sum = sum + 1;
										}else{
											sum = sum + (v/threshold);
										}
									}
								}
							}
						}
					}
				}
				imageOut[ind][1] = (float) nb;
				imageOut[ind][2] = sum;
			}
		}
	}

	public void applySlidingWindow(int theY, short tile) {
		this.theY = theY;
		execute(width * tile);
	}

	@Override
	public void run() {
		final int x = getGlobalId(0) % width;
		final int y = getGlobalId(0) / width;
		processPixel(x, theY + y, y);
	}
}
