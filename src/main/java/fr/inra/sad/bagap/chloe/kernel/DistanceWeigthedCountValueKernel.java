package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class DistanceWeigthedCountValueKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float imageIn[];
	
	private final float imageOut[][];
	
	private final short[] shape;
	
	private final float[] coeff;
	
	private final int windowSize;
	
	private final short[] values;
	
	private int theY;
	
	private final int noDataValue;
	
	private final int enveloppeInterne; // in pixels
	
	public DistanceWeigthedCountValueKernel(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue){
		this(values, windowSize, shape, coeff, width, height, dep, imageIn, imageOut, noDataValue, 0);
	}
		
	
	public DistanceWeigthedCountValueKernel(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
		this.setExplicit(true);
		//this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.GPU);
		this.values = values;
		this.windowSize = windowSize;
		this.shape = shape;
		this.coeff = coeff;
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
			
			int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
			
			for(int i=0; i<values.length+2; i++){
				imageOut[ind][i] = 0f;
			}
			
			if(imageIn[(y * width) + x] != 0f) {
				
				if(!(x < enveloppeInterne || (width - x) < enveloppeInterne || y <enveloppeInterne || (height - y) < enveloppeInterne)) {
					final int mid = windowSize / 2;
					int ic;
					short v;
					boolean again;
					/*
					imageOut[ind][0] = imageIn[((y) * width) + (x)];
					imageOut[ind][1] = imageIn[((y) * width) + (x)];
					for(int i=0; i<values.length; i++){
						imageOut[ind][i+2] = imageIn[((y) * width) + (x)];
					}
					*/							
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height)){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width)){
									ic = ((dy+mid) * windowSize) + (dx+mid);
									if(shape[ic] == 1){
										v = (short) imageIn[((y + dy) * width) + (x + dx)];
										
										if(v == noDataValue){
											imageOut[ind][0] = imageOut[ind][0] + coeff[ic];
										}else{
											if(v == 0){
												imageOut[ind][1] = imageOut[ind][1] + coeff[ic];
											}else{
												again = true;
												for(int i=0; again && i<values.length; i++){
													if(v == values[i]){
														imageOut[ind][i+2] = imageOut[ind][i+2] + coeff[ic];
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
			//System.out.println(imageOut[ind][2]);
		}
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
