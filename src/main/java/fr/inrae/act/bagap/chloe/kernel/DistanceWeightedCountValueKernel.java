package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeightedCountValueKernel extends LandscapeMetricKernel {
	
	private final int dep;

	private final float imageIn[];
	
	private final short[] shape;
	
	private final float[] coeff;
	
	private final int windowSize;
	
	private final short[] values;
	
	private final int noDataValue;
	
	public DistanceWeightedCountValueKernel(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, int noDataValue){
		this(values, windowSize, shape, coeff, width, height, dep, imageIn, noDataValue, 0);
	}
		
	public DistanceWeightedCountValueKernel(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, int noDataValue, int internalROI){
		super(width + 2*internalROI, height + 2*internalROI, internalROI);
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.values = values;
		this.windowSize = windowSize;
		this.shape = shape;
		this.coeff = coeff;
		this.dep = dep;
		this.imageIn = imageIn;
		this.noDataValue = noDataValue;
	}

	public void processPixel(int x, int y, int localY) {
		
		if(x%dep == 0 && y%dep == 0){
			
			int ind = ((((localY-internalROI())/dep))*((((width()-2*internalROI())-1)/dep)+1) + (((x-internalROI())/dep)));
			
			for(int i=0; i<values.length+2; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) { // gestion des filtres a mettre en place 
				
			final int mid = windowSize / 2;
			int ic;
			short v;
			boolean again;
									
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize) + (dx+mid);
							if(shape[ic] == 1){
								v = (short) imageIn[((y + dy) * width()) + (x + dx)];
										
								if(v == noDataValue){
									imageOut()[ind][0] = imageOut()[ind][0] + coeff[ic];
								}else{
									if(v == 0){
										imageOut()[ind][1] = imageOut()[ind][1] + coeff[ic];
									}else{
										again = true;
										for(int i=0; again && i<values.length; i++){
											if(v == values[i]){
												imageOut()[ind][i+2] = imageOut()[ind][i+2] + coeff[ic];
												again = false;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void run() {
		final int x = internalROI() + (getGlobalId(0) % (width() - 2 * internalROI()));
		final int y = internalROI() + (getGlobalId(0) / (width() - 2 * internalROI()));
		processPixel(x, theY() + y, y);
	}
}
