package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;
import fr.inrae.act.bagap.chloe.kernel.SlidingLandscapeMetricKernel;


public class FastGaussianWeightedCountValueKernel extends SlidingLandscapeMetricKernel {

	

	
	private final int[] values;
	private final int nbValues;
	private final int[] mapValues;
	

	private float[][] buf;
	private final float[] gauss;
	
	
	
	public FastGaussianWeightedCountValueKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] values, int[] unfilters){
		super( windowSize, displacement, shape, coeff, noDataValue, unfilters);

		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		//this.setExecutionMode(Kernel.EXECUTION_MODE.SEQ);
		
		this.values = values;
		this.nbValues = values.length;
		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}

		this.gauss = new float[windowSize()];
		this.buf = new float[width()][values.length+3];
		
		float r=(windowSize-1)/2.f;
		for(int i=0;i<windowSize;i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i]=(float) Math.exp(-d*d);
			//gauss[i]=1;
		}
		
	
	}

	
	public void processVerticalPixel(int x,int line) {
		int y = theY() + line;
		int i,dy;
		// parcours vertical de l'image centré sur ligne theY+line et stocké dans buf
		for(i=0;i<values.length+3;i++) {
			buf[x][i]=0;
		}
		for (dy = -windowSize()+1; dy < windowSize(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				int ic = abs(dy);
				int v = (int)imageIn()[((y + dy) * width()) + x];
				int mv;			
				if(v == noDataValue())
					mv = 0;
				else if(v==0)
					mv = 1;
				else
					mv = mapValues[v]+3;
				buf[x][mv] += gauss[ic];

			}
		}
	}

	public void processHorizontalPixel(int x,int line) {

		// parcours horizontal de buf: position dans imageOut : (x,y=line/dep)
		int x_buf = x*displacement();
		int y=line/displacement();
		int ind = y*((width()-1)/displacement()+1) + x;
		imageOut()[ind][2] = (int)imageIn()[((theY() + line)* width()) + x];
		float val;
		
		for(int value=0; value<values.length+3;value++) {
			if(value==2) continue;
			val=0;
			for(int i=max(x_buf-windowSize()+1,0);i<min(x_buf+windowSize(),width());i++) {
				val+=buf[i][value]*gauss[abs(i-x_buf)];
			}
			imageOut()[ind][value] = val;
		}

	}


	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.buf = new float[width()][values.length+3];
	}
	
	@Override
	public void applySlidingWindow(int theY, int buffer) {
		this.setTheY(theY); // indique position de la première ligne du buffer dans imageIn
		//execute(width * buffer);
		//execute((width - bufferROIXMin - bufferROIXMax) * buffer);
		final int nlines = (buffer - (displacement()-theY()%displacement())%displacement() )/displacement();
		execute(width(),2*nlines);

	}
	
	@Override
	public void run() {
		//final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		//final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		final int x = bufferROIXMin() + getGlobalId(0);
		final int pass = getPassId();
		final int vertical = pass%2;
		final int line= (displacement()-theY()%displacement())%displacement() + (pass/2)*displacement();
		if(vertical == 0)
			processVerticalPixel(x,line);
		else {
			if( x < (width()- bufferROIXMin() - bufferROIXMax()-1)/displacement()+1 )
				processHorizontalPixel(x,line);
		}
	}
}
