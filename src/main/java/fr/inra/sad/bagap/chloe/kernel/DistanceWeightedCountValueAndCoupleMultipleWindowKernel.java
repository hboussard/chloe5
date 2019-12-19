package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeightedCountValueAndCoupleMultipleWindowKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float[] imageIn;
	
	private final float[][][] imageOut;
	
	private final short[][] shape;
	
	private final float[][] coeff;
	
	private final short[] windowSize;
	
	private final int[] mid;
	
	private final short[] values;
	
	private final float[] couples;
	
	private int theY;
	
	public DistanceWeightedCountValueAndCoupleMultipleWindowKernel(short[] values, float[] couples, short[] windowSize, short[][] shape, float[][] coeff, int width, int height, int dep, float[] imageIn, float[][][] imageOut){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.GPU);
		this.values = values;
		this.couples = couples;
		this.windowSize = windowSize;
		this.mid = new int[windowSize.length];
		for(int w=0; w<windowSize.length; w++) {
			this.mid[w] = this.windowSize[w] / 2;
		}
		this.shape = shape;
		this.coeff = coeff;
		this.width = width;
		this.height = height;
		this.dep = dep;
		this.imageIn = imageIn;
		this.imageOut = imageOut;
	}

	public void processPixel(int x, int y, int localY) {
		if(x%dep == 0 && y%dep == 0){
			
			int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
			
			for(int w=0; w<windowSize.length; w++) {
				for(int i=0; i<imageOut[ind][w].length; i++){
					imageOut[ind][w][i] = 0f;
				}
			}
			
			
			int ic, ic_V, ic_H;
			short v = 0, v_H, v_V;
			float c = 0.0f;
			boolean again;
			boolean encore;
			
			for (int dy = -mid[0]; dy <= mid[0]; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height)){
					for (int dx = -mid[0]; dx <= mid[0]; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width)){
							encore = true;
							for(int w=0; encore && w<windowSize.length; w++) {
								encore = false;
								if(dy>=-mid[w] && dy<=mid[w] && dx>=-mid[w] && dx<=mid[w]) {
									
									ic = ((dy+mid[w]) * windowSize[w]) + (dx+mid[w]);
									
									if(shape[w][ic] == 1) {
										encore = true;
										if(w == 0) {
											v = (short) imageIn[((y + dy) * width) + (x + dx)];
										}
										
										if(v == -1){
											imageOut[ind][w][0] = imageOut[ind][w][0] + coeff[w][ic];
										}else{
											if(v == 0){
												imageOut[ind][w][1] = imageOut[ind][w][1] + coeff[w][ic];
											}else{
												again = true;
												for(int i=0; again && i<values.length; i++){
													if(v == values[i]){
														imageOut[ind][w][i+2] = imageOut[ind][w][i+2] + coeff[w][ic];
														again = false;
													}
												}
											}
										}
										
										if((dy > -mid[w]) && ((y + dy) > 0)) {
											ic_V = ((dy+mid[w]-1) * windowSize[w]) + (dx+mid[w]);
											if(shape[w][ic_V] == 1){
												if(w == 0) {
													v_V = (short) imageIn[((y + dy - 1) * width) + (x + dx)];
													c = getCouple(v, v_V);
												}
												if(c == -1){
													imageOut[ind][w][values.length+2] = imageOut[ind][w][values.length+2] + coeff[w][ic];
												}else{
													if(c == 0){
														imageOut[ind][w][values.length+3] = imageOut[ind][w][values.length+3] + coeff[w][ic];
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
																imageOut[ind][w][i+values.length+4] = imageOut[ind][w][i+values.length+4] + coeff[w][ic];
																again = false;
															}
														}
													}
												}
											}
										}
									
										if((dx > -mid[w]) && ((x + dx) > 0)) {
											ic_H = ((dy+mid[w]) * windowSize[w]) + (dx+mid[w]-1);
											if(shape[w][ic_H] == 1){
												if(w == 0) {
													v_H = (short) imageIn[((y + dy) * width) + (x + dx - 1)];
													c = getCouple(v, v_H);
												}
												if(c == -1){
													imageOut[ind][w][values.length+2] = imageOut[ind][w][values.length+2] + coeff[w][ic];
												}else{
													if(c == 0){
														imageOut[ind][w][values.length+3] = imageOut[ind][w][values.length+3] + coeff[w][ic];
													}else{
														again = true;
														for(int i=0; again && i<couples.length; i++){
															if(c == couples[i]){
																imageOut[ind][w][i+values.length+4] = imageOut[ind][w][i+values.length+4] + coeff[w][ic];
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

	public void applySlidingWindow(int theY, short buffer) {
		this.theY = theY;
		execute(width * buffer);
	}

	@Override
	public void run() {
		final int x = getGlobalId(0) % width;
		final int y = getGlobalId(0) / width;
		processPixel(x, theY + y, y);
	}
}
