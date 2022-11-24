package fr.inrae.act.bagap.chloe.kernel;

public class SlidingDistanceWeightedCountValueKernel extends SlidingLandscapeMetricKernel {

	private final int[] mapValues;
	
	public SlidingDistanceWeightedCountValueKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] values, int[] unfilters){		
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
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
			
			for(int i=0; i<imageOut()[0].length; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) { // gestion des filtres a mettre en place 
			// gestion des filtres
			
			imageOut()[ind][2] = imageIn()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			if(filter((int) imageIn()[(y * width()) + x])){
				final int mid = windowSize() / 2;
				int ic;
				int v;
				int mv;				
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								if(shape()[ic] == 1){
									v = (int) imageIn()[((y + dy) * width()) + (x + dx)];		
									if(v == noDataValue()){
										imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
									}else if(v == 0){
										imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
									}else{
										mv = mapValues[v];
										imageOut()[ind][mv+3] = imageOut()[ind][mv+3] + coeff()[ic];	
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
