package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class DistanceWeigthedCountValueKernel2d extends Kernel {

	private int width, height;
	
	private int dep;

	private float imageIn[];
	
	private float imageOut[][];
	
	private short[] shape;
	
	private float[] coeff;
	
	private int windowSize;
	
	private short[] values;
	
	private int theY;
	
	public DistanceWeigthedCountValueKernel2d(short[] values, int windowSize, short[] shape, float[] coeff, int width, int height, int dep, float[] imageIn, float[][] imageOut){
		this.setExplicit(true);
		this.values = values;
		this.windowSize = windowSize;
		this.shape = shape;
		this.coeff = coeff;
		this.width = width;
		this.height = height;
		this.dep = dep;
		this.imageIn = imageIn;
		this.imageOut = imageOut;
	}

	public void processPixel(int x, int y, int localY, int dx, int dy) {
		if(x%dep == 0 && y%dep == 0){
			
			int ind = ((((localY)/dep))*(((width-1)/dep)+1) + (((x)/dep)));
			
			int mid = windowSize / 2;
			int ic;
			short v;
			boolean again;
									
			if(((y + dy) >= 0) && ((y + dy) < height)){
				if(((x + dx) >= 0) && ((x + dx) < width)){
					ic = ((dy+mid) * windowSize) + (dx+mid);
					if(shape[ic] == 1){
						v = (short) imageIn[((y + dy) * width) + (x + dx)];
						
						if(v == -1){
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
		
			//System.out.println(imageOut[ind][2]);
		}
	}

	public void applySlidingWindow(int theY, short buffer) {
		this.theY = theY;
		for(int ind=0; ind<imageOut.length; ind++){
			for(int i=0; i<values.length+2; i++){
				imageOut[ind][i] = 0f;
			}
		}
		execute(Range.create2D(width * buffer, windowSize * windowSize));
	}

	@Override
	public void run() {
		final int mid = windowSize / 2;
		final int x = getGlobalId(0) % width;
		final int y = getGlobalId(0) / width;
		final int dx = getGlobalId(1) % windowSize;
		final int dy = getGlobalId(1) / windowSize;
		processPixel(x, theY + y, y, dx-mid, dy-mid);
	}
}
