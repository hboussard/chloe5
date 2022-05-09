package fr.inrae.act.bagap.chloe.kernel;

public class DistanceWeightedCountValueAndCoupleKernelOld extends SlidingLandscapeMetricKernel {
	
	private final short[] values;
	
	private final float[] couples;
	
	public DistanceWeightedCountValueAndCoupleKernelOld(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, short[] values, float[] couples, int[] unfilters){
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
		this.values = values;
		this.couples = couples;
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
			
			for(int i=0; i<values.length+2+couples.length+2; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) {
				
					
			final int mid = windowSize() / 2;
			int ic, ic_V, ic_H;
			short v, v_H, v_V;
			float c;
			boolean again;
			
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							if(shape()[ic] == 1) {
								v = (short) imageIn()[((y + dy) * width()) + (x + dx)];
								
								
								if(v == -1){
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
								
								if((dy > -mid) && ((y + dy) > 0)) {
									ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
									if(shape()[ic_V] == 1){
										v_V = (short) imageIn()[((y + dy - 1) * width()) + (x + dx)];
										c = getCouple(v, v_V);
										if(c == noDataValue()){
											imageOut()[ind][values.length+2] = imageOut()[ind][values.length+2] + coeff()[ic];
										}else{
											if(c == 0){
												imageOut()[ind][values.length+3] = imageOut()[ind][values.length+3] + coeff()[ic];
											}else{
												again = true;
												for(int i=0; again && i<couples.length; i++){
													if(c == couples[i]){
														imageOut()[ind][i+values.length+4] = imageOut()[ind][i+values.length+4] + coeff()[ic];
														again = false;
													}
												}
											}
										}
									}
								}
								
								if((dx > -mid) && ((x + dx) > 0)) {
									ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
									if(shape()[ic_H] == 1){
										v_H = (short) imageIn()[((y + dy) * width()) + (x + dx - 1)];
										c = getCouple(v, v_H);
										if(c == noDataValue()){
											imageOut()[ind][values.length+2] = imageOut()[ind][values.length+2] + coeff()[ic];
										}else{
											if(c == 0){
												imageOut()[ind][values.length+3] = imageOut()[ind][values.length+3] + coeff()[ic];
											}else{
												again = true;
												for(int i=0; again && i<couples.length; i++){
													if(c == couples[i]){
														imageOut()[ind][i+values.length+4] = imageOut()[ind][i+values.length+4] + coeff()[ic];
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
	}
	
	private float getCouple(short v1, short v2) {
		
		if(v1 == -1 || v2 == -1) {
			return -1;
		}
		if(v1 == 0 || v2 == 0) {
			return 0;
		}
		if(v1 < v2) {
			return (float) (v2 * 0.0001 + v1);
		}else {
			return (float) (v1 * 0.0001 + v2);
		}
		
		//return 1.0001f;
	}

}
