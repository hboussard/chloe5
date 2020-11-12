package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;
import com.aparapi.Range;

import fr.inra.sad.bagap.chloe.Util;


public class GaussianWeigthedCountValueKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float imageIn[];
	
	private final float imageOut[][];
	
	private final int rayon;
	
	private final short[] values;
	
	private int theY;
	
	private int line;
	
	private int vertical;
	
	private final float[][] buf;
	
	private final float[] gauss;
	
	private final int noDataValue;
	
	private final int enveloppeInterne; // in pixels
	
	public GaussianWeigthedCountValueKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue){
		this(values, windowSize, width, height, dep, imageIn, imageOut, noDataValue, 0);
	}
	
	public GaussianWeigthedCountValueKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.CPU);
		this.values = values;
		this.rayon = windowSize/2 + 1;
		this.width = width;
		this.height = height;
		this.dep = dep;
		this.imageIn = imageIn;
		this.imageOut = imageOut;
		this.noDataValue = noDataValue;
		this.enveloppeInterne = enveloppeInterne;
		this.buf = new float[width][values.length+2];
		this.gauss = new float[rayon];
		
		float r=(rayon-1)/2.f;
		for(int i=0;i<rayon;i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i] = (float) Math.exp(-d*d);
			//gauss[i]=(float) 1;
		}
	}

	public void processPixel(int x) {
		if(vertical > 0) {
			int y = theY + line;
			// parcours vertical de l'image centré sur ligne theY+line et stocké dans buf
			for(int i=0;i<values.length+2;i++) {
				buf[x][i]=0;
			}
			for (int dy = -rayon+1; dy < rayon; dy++) {
				if(((y + dy) >= 0) && ((y + dy) < height)){
					
					int ic = abs(dy);
					short v = (short) imageIn[((y + dy) * width) + x];
								
					if(v == noDataValue){
						buf[x][0] += gauss[ic];
					}else{
						if(v == 0){
							buf[x][1] += gauss[ic];
						}else{
							boolean again = true;
							for(int i=0; again && i<values.length; i++){
								if(v == values[i]){
									buf[x][i+2] += gauss[ic];
									again = false;
								}
							}
						}
					}
				}
			}
		}
		else {
			// parcours horizontal de buf: position dans imageOut : (x,line/dep)
			int x_buf = x*dep;
			int y=line/dep;
			int ind = y*((width-1)/dep+1) + x;
			
			for(int value=0; value<values.length+2; value++) {
				float val=0;
				for(int i=max(x_buf-rayon+1,0); i<min(x_buf+rayon,width); i++){
					val += buf[i][value] * gauss[abs(i-x_buf)];
				}
				imageOut[ind][value] = val;
			}

		}
	}

	public void applySlidingWindow(int theY, short buffer) {
		this.theY = theY;
		for(line=(dep-theY%dep)%dep; line<buffer; line+=dep) {
			this.vertical = 1;
			execute(width);
			this.vertical = 0;
//			System.out.println("horizontal : line = "+Integer.toString(line));
			execute((width-1)/dep+1);
		}
	}

	@Override
	public void run() {
		processPixel(getGlobalId(0));
	}
}
