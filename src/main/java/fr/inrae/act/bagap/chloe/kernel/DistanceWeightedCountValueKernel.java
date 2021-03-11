package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeightedCountValueKernel extends LandscapeMetricKernel {
	
	private final short[] values;
	
	public DistanceWeightedCountValueKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, short[] values){		
		super(windowSize, displacement, shape, coeff, noDataValue);
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.values = values;
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}

	public void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			/*
			if(x == bufferROIXMin() && y == bufferROIYMin()){
				System.out.println(x+" "+y+" "+localY+" "+ind);
			}
			*/
			
			for(int i=0; i<values.length+2; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) { // gestion des filtres a mettre en place 
				
			final int mid = windowSize() / 2;
			int ic;
			short v;
			boolean again;
									
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							if(shape()[ic] == 1){
								v = (short) imageIn()[((y + dy) * width()) + (x + dx)];		
								if(v == noDataValue()){
									imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
								}else{
									if(v == 0){
										imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
									}else{
										again = true;
										for(int i=0; again && i<values.length; i++){
											if(v == values[i]){
												imageOut()[ind][i+2] = imageOut()[ind][i+2] + coeff()[ic];
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

}
