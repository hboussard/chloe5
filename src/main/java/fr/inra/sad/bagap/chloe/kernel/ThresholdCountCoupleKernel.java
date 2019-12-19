package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class ThresholdCountCoupleKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float[] imageIn;
	
	private final float[][] imageOut;
	
	private final short[] shape;
	
	private final int windowSize;
	
	private final float[] couples;
	
	private int theY;
	
	private final float noDataValue;
	
	private final int enveloppeInterne;
	
	public ThresholdCountCoupleKernel(float[] couples, int windowSize, short[] shape, int width, int height, int dep, float[] imageIn, float[][] imageOut, float noDataValue, int enveloppeInterne){
		this.setExplicit(true);
		this.couples = couples;
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
			
			//System.out.println(x+" "+y);
			
			int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
			
			for(int i=0; i<couples.length+2; i++){
				imageOut[ind][i] = 0f;
			}
			
			if(imageIn[(y * width) + x] != 0f) {
				if(!(x < enveloppeInterne || (width - x) < enveloppeInterne || y <enveloppeInterne || (height - y) < enveloppeInterne)) {
					int mid = windowSize / 2;
					int ic, ic_V, ic_H;
					short v, v_H, v_V;
					float c;
					boolean again;
							
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height)){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width)){
									ic = ((dy+mid) * windowSize) + (dx+mid);
									if(shape[ic] == 1) {
										v = (short) imageIn[((y + dy) * width) + (x + dx)];
										if(dy > -mid) {
											ic_V = ((dy+mid-1) * windowSize) + (dx+mid);
											if(shape[ic_V] == 1){
												v_V = (short) imageIn[((y + dy - 1) * width) + (x + dx)];
												c = getCouple(v, v_V);
													
												if(c == noDataValue){
													imageOut[ind][0] = imageOut[ind][0] + 1;
												}else{
													if(c == 0){
														imageOut[ind][1] = imageOut[ind][1] + 1;
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
																imageOut[ind][i+2] = imageOut[ind][i+2] + 1;
																again = false;
															}
														}
													}
												}
											}
										}
										
										if(dx > -mid) {
											ic_H = ((dy+mid) * windowSize) + (dx+mid-1);
											if(shape[ic_H] == 1){
												v_H = (short) imageIn[((y + dy) * width) + (x + dx - 1)];
												c = getCouple(v, v_H);
												
												if(c == noDataValue){
													imageOut[ind][0] = imageOut[ind][0] + 1;
												}else{
													if(c == 0){
														imageOut[ind][1] = imageOut[ind][1] + 1;
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
																imageOut[ind][i+2] = imageOut[ind][i+2] + 1;
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
