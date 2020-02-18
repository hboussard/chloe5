package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;


public class GaussianWeigthedCountValueKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float imageIn[];
	
	private final float imageOut[][];
	
	private final int rayon;
	
	private final short[] values;
	
	private int theY;

	private final float[][] buf;
	private final float[] gauss;
	
	private final int noDataValue;
	
	private final int enveloppeInterne; // in pixels
	
	public GaussianWeigthedCountValueKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue){
		this(values, windowSize, width, height, dep, imageIn, imageOut, noDataValue, 0);
	}
		
	
	public GaussianWeigthedCountValueKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
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
			gauss[i]=(float) Math.exp(-d*d);
			gauss[i]=1;
		}
	}

	public void processVerticalPixel(int x,int line) {
		int y = theY + line;
		int i,dy;
		// parcours vertical de l'image centré sur ligne theY+line et stocké dans buf
		for(i=0;i<values.length+2;i++) {
			buf[x][i]=0;
		}
		for (dy = -rayon+1; dy < rayon; dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height)){
				
				int ic = abs(dy);
				float v = imageIn[((y + dy) * width) + x];
							
				if(v == noDataValue){
					buf[x][0] += gauss[ic];
				} else {
					if(v == 0){
						buf[x][1] += gauss[ic];
					} else {
						boolean again = true;
						for(i=0; again && i<values.length; i++){
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

	public void processHorizontalPixel(int x,int line) {

		// parcours horizontal de buf: position dans imageOut : (x,y=line/dep)
		int x_buf = x*dep;
		int y=line/dep;
		int ind = y*((width-1)/dep+1) + x;
		float val;
		
		for(int value=0; value<values.length+2;value++) {
			val=0;
			for(int i=max(x_buf-rayon+1,0);i<min(x_buf+rayon,width);i++) {
				val+=buf[i][value]*gauss[abs(i-x_buf)];
			}
			imageOut[ind][value] = val;
		}

	}


	public void applySlidingWindow(int theY, short buffer) {
		this.theY = theY;
		int nlines = (buffer - ((dep-theY%dep)%dep) )/dep;
		execute(width,2*nlines);
	}

	@Override
	public void run() {
		int x = getGlobalId(0);
		int pass = getPassId();
		int vertical = pass%2;
		int line= (dep-theY%dep)%dep + pass/2;
		if(vertical == 0)
			processVerticalPixel(getGlobalId(0),line);
		else {
			if( x < (width-1)/dep+1 )
				processHorizontalPixel(x,line);
		}
	}
}
