package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeightedCountCoupleKernel extends LandscapeMetricKernel {
	
	private final int dep;

	private final float[] imageIn;
	
	private final short[] shape;
	
	private final float[] coeff;
	
	private final int windowSize;
	
	private final float[] couples;
	
	private final float noDataValue;
	
	public DistanceWeightedCountCoupleKernel(float[] couples, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, float noDataValue){
		this(couples, windowSize, shape, coeff, width, height, dep, imageIn, noDataValue, 0);
	}
	
	public DistanceWeightedCountCoupleKernel(float[] couples, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, float noDataValue, int internalROI){
		super(width + 2*internalROI, height + 2*internalROI, internalROI);
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.couples = couples;
		this.windowSize = windowSize;
		this.shape = shape;
		this.coeff = coeff;
		this.dep = dep;
		this.imageIn = imageIn;
		this.noDataValue = noDataValue;
	}

	public void processPixel(int x, int y, int localY) {
		if(x%dep == 0 && y%dep == 0){
			
			//int ind = ((((localY)/dep))*(((width()-1)/dep)+1) + (((x)/dep)));
			int ind = ((((localY-internalROI())/dep))*((((width()-2*internalROI())-1)/dep)+1) + (((x-internalROI())/dep)));
			
			for(int i=0; i<couples.length+2; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) {
				
					
					final int mid = windowSize / 2;
					int ic, ic_V, ic_H;
					short v, v_H, v_V;
					float c;
					boolean again;
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									ic = ((dy+mid) * windowSize) + (dx+mid);
									if(shape[ic] == 1) {
										v = (short) imageIn[((y + dy) * width()) + (x + dx)];
										
										if((dy > -mid) && ((y + dy) > 0)) {
											ic_V = ((dy+mid-1) * windowSize) + (dx+mid);
											if(shape[ic_V] == 1){
												v_V = (short) imageIn[((y + dy - 1) * width()) + (x + dx)];
												c = getCouple(v, v_V);
												if(c == noDataValue){
													imageOut()[ind][0] = imageOut()[ind][0] + coeff[ic];
												}else{
													if(c == 0){
														imageOut()[ind][1] = imageOut()[ind][1] + coeff[ic];
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
																imageOut()[ind][i+2] = imageOut()[ind][i+2] + coeff[ic];
																again = false;
															}
														}
													}
												}
											}
										}
										
										if((dx > -mid) && ((x + dx) > 0)) {
											ic_H = ((dy+mid) * windowSize) + (dx+mid-1);
											if(shape[ic_H] == 1){
												v_H = (short) imageIn[((y + dy) * width()) + (x + dx - 1)];
												c = getCouple(v, v_H);
												if(c == noDataValue){
													imageOut()[ind][0] = imageOut()[ind][0] + coeff[ic];
												}else{
													if(c == 0){
														imageOut()[ind][1] = imageOut()[ind][1] + coeff[ic];
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
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
				//}
			//}
					
			//System.out.println(imageOut[ind][2]);
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

	@Override
	public void run() {
		final int x = internalROI() + (getGlobalId(0) % (width() - 2 * internalROI()));
		final int y = internalROI() + (getGlobalId(0) / (width() - 2 * internalROI()));
		
		//final int x = getGlobalId(0) % width();
		//final int y = getGlobalId(0) / width();
		processPixel(x, theY() + y, y);
	}
}
