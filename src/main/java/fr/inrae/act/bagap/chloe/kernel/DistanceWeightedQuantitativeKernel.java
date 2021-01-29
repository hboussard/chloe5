package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeightedQuantitativeKernel extends LandscapeMetricKernel {
	
	private final int dep;

	private final float imageIn[];
	
	private final short[] shape;
	
	private final float[] coeff;
	
	private final int windowSize;
	
	private final int noDataValue;
	
	private final int internalROI; // in pixels
	
	private final float threshold;
	
	public DistanceWeightedQuantitativeKernel(int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, int noDataValue){
		this(windowSize, shape, coeff, width, height, dep, imageIn, noDataValue, -1, 0);
	}
	
	public DistanceWeightedQuantitativeKernel(int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, int noDataValue, float threshold){
		this(windowSize, shape, coeff, width, height, dep, imageIn, noDataValue, threshold, 0);
	}
		
	public DistanceWeightedQuantitativeKernel(int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, int noDataValue, float threshold, int internalROI){
		super(width, height);
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.shape = shape;
		this.coeff = coeff;
		this.dep = dep;
		this.imageIn = imageIn;
		this.noDataValue = noDataValue;
		this.threshold = threshold;
		this.internalROI = internalROI;
	}

	public void processPixel(int x, int y, int localY) {
		
		if(x%dep == 0 && y%dep == 0){
			
			int ind = ((((localY)/dep))*(((width()-1)/dep)+1) + (((x)/dep)));
			
			// phase d'initialisation de la structure de données
			for(int i=0; i<3; i++){
				imageOut()[ind][i] = 0.0f;
			}
			
			
			if(!(x < internalROI || (width() - x) < internalROI || y <internalROI || (height() - y) < internalROI)) {
				final int mid = windowSize / 2;
				int ic;
				float v;
				float nb = 0;
				float sum = 0;
				
				if(threshold != -1){
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									ic = ((dy+mid) * windowSize) + (dx+mid);
									if(shape[ic] == 1){
										v = imageIn[((y + dy) * width()) + (x + dx)];
										if(v == noDataValue) {
											imageOut()[ind][0] = imageOut()[ind][0] + coeff[ic];
										}else{
											nb = nb + coeff[ic];
											if(v > threshold){
												sum = sum + threshold*coeff[ic];
											}else{
												sum = sum + v*coeff[ic];
											}
										}
									}
								}
							}
						}
					}
				}else{
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									ic = ((dy+mid) * windowSize) + (dx+mid);
									if(shape[ic] == 1){
										v = imageIn[((y + dy) * width()) + (x + dx)];
										if(v == noDataValue) {
											imageOut()[ind][0] = imageOut()[ind][0] + coeff[ic];
										}else{
											nb = nb + coeff[ic];
											sum = sum + v*coeff[ic];
										}
									}
								}
							}
						}
					}
				}
				
				imageOut()[ind][1] = nb;
				imageOut()[ind][2] = sum;
			}
		}
	}

	@Override
	public void run() {
		final int x = getGlobalId(0) % width();
		final int y = getGlobalId(0) / width();
		processPixel(x, theY() + y, y);
	}
}
