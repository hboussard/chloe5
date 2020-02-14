package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;
import com.aparapi.Range;


public class SquareCountKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final float imageIn[];
	
	private final float imageOut[][];
	
	private final int rayon;
	
	private final short[] values;
	
	private int theY;

	private final float[][] buf;
	
	private final int noDataValue;
	
	private final int enveloppeInterne; // in pixels
	
	public SquareCountKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue){
		this(values, windowSize, width, height, dep, imageIn, imageOut, noDataValue, 0);
	}
		
	
	public SquareCountKernel(short[] values, int windowSize, int width, int height, int dep, float[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
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

	}

	public void processVerticalPixel(int x,int line) {
		int y = theY + line;
		int i,dy;
		// parcours vertical de l'image centré sur ligne theY+line et stocké dans buf
		for(i=0;i<values.length;i++) {
			buf[x][i]=0;
		}
		for (dy = -rayon+1; dy < rayon; dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height)){
				
				int ic = abs(dy);
				int v = (int)imageIn[((y + dy) * width) + x];
				buf[x][v] ++;	

			}
		}
	}

	public void processHorizontalPixel(int x,int line) {

		// parcours horizontal de buf: position dans imageOut : (x,line/dep)
		int x_buf = x*dep;
		int y=line;
		int ind = y*((width-1)/dep+1) + x;
		int i;
		float val;
		
		for(int value=0; value<values.length;value++) {
			val=0;
			for(i=max(x_buf-rayon+1,0);i<min(x_buf+rayon,width);i++) {
				val+=buf[i][value];
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
