package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public class SlidingQuantitativeKernel extends SlidingLandscapeMetricKernel {
	
	private final float threshold;
	
	public SlidingQuantitativeKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters){
		this(windowSize, displacement, coeff, noDataValue, -1, unfilters);
	}
		
	public SlidingQuantitativeKernel(int windowSize, int displacement, float[] coeff, int noDataValue, float threshold, int[] unfilters){
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		this.threshold = threshold;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			// phase d'initialisation de la structure de donnees
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0.0f;
			}
			
			outDatas()[ind][7] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			if(filter((short) inDatas()[(y * width()) + x])){
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				final int mid = windowSize() / 2;
				int ic;
				float v, coeff;
				float nb_nodata = 0;
				float nb = 0;
				float sum = 0;
				double square_sum = 0;
				float min = Float.MAX_VALUE;
				float max = Float.MIN_VALUE;
				if(threshold != -1){
					
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									ic = ((dy+mid) * windowSize()) + (dx+mid);
									coeff = coeff()[ic];
									if(coeff > 0){
										v = inDatas()[((y + dy) * width()) + (x + dx)];
										
										if(v == noDataValue()) {
											nb_nodata += coeff;
										}else{
											nb += coeff;
											if(v > threshold){
												sum += threshold*coeff;
												square_sum += threshold*coeff * threshold*coeff;
											}else{
												sum += v*coeff;
												square_sum += v*coeff * v*coeff;
												min = Math.min(min, v*coeff);
												max = Math.max(max, v*coeff);
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
									ic = ((dy+mid) * windowSize()) + (dx+mid);
									coeff = coeff()[ic];
									if(coeff > 0){
										v = inDatas()[((y + dy) * width()) + (x + dx)];
										
										if(v == noDataValue()) {
											nb_nodata += coeff;
										}else{
											nb += coeff;
											sum += v*coeff;
											square_sum += v*coeff * v*coeff;
											min = Math.min(min, v*coeff);
											max = Math.max(max, v*coeff);
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][1] = nb_nodata;
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = sum;
				outDatas()[ind][4] = square_sum;
				outDatas()[ind][5] = min;
				outDatas()[ind][6] = max;
				
			}
		}
	}

	
}
