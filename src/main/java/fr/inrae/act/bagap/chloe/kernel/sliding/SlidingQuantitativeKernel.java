package fr.inrae.act.bagap.chloe.kernel.sliding;

public class SlidingQuantitativeKernel extends SlidingLandscapeMetricKernel {
	
	private final float threshold;
	
	public SlidingQuantitativeKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters){
		this(windowSize, displacement, shape, coeff, noDataValue, -1, unfilters);
	}
		
	public SlidingQuantitativeKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, float threshold, int[] unfilters){
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
		this.threshold = threshold;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			// phase d'initialisation de la structure de données
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0.0f;
			}
			
			outDatas()[ind][6] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			if(filter((short) inDatas()[(y * width()) + x])){
				final int mid = windowSize() / 2;
				int ic;
				float v, c;
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
									if(shape()[ic] == 1){
										v = inDatas()[((y + dy) * width()) + (x + dx)];
										c = coeff()[ic];
										
										if(v == noDataValue()) {
											nb_nodata = nb_nodata + c;
										}else{
											nb = nb + c;
											if(v > threshold){
												sum = sum + threshold*c;
												square_sum = square_sum + threshold*c * threshold*c;
											}else{
												sum = sum + v*c;
												square_sum = square_sum + v*c * v*c;
												min = Math.min(min, v*c);
												max = Math.max(max, v*c);
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
									if(shape()[ic] == 1){
										v = inDatas()[((y + dy) * width()) + (x + dx)];
										c = coeff()[ic];
										if(v == noDataValue()) {
											nb_nodata += c;
										}else{
											nb += c;
											sum += v*c;
											square_sum += v*c * v*c;
											min = Math.min(min, v*c);
											max = Math.max(max, v*c);
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][0] = nb_nodata;
				outDatas()[ind][1] = nb;
				outDatas()[ind][2] = sum;
				outDatas()[ind][3] = square_sum;
				outDatas()[ind][4] = min;
				outDatas()[ind][5] = max;
				
				//System.out.println(nb_nodata+" "+nb+" "+sum+" "+square_sum+" "+min+" "+max);
			}
		}
	}

	
}
