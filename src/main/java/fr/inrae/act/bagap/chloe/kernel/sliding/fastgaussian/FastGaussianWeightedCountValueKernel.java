package fr.inrae.act.bagap.chloe.kernel.sliding.fastgaussian;

import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;

public class FastGaussianWeightedCountValueKernel extends SlidingLandscapeMetricKernel {

	private int[] values;
	
	//private final int nbValues;
	
	private int[] mapValues;
	
	private float[][] buf;
	
	private float[] gauss;
	
	public FastGaussianWeightedCountValueKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super( windowSize, displacement, null, noDataValue, unfilters);

		this.values = values;
		//this.nbValues = values.length;
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
		for(int i=0; i<windowSize; i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i] = (float) Math.exp(-d*d);
		}
	}

	public void processVerticalPixel(int x, int line) {
		int y = theY() + line;
		int i, dy, ic, v, mv;
		
		// parcours vertical de l'image centre sur ligne theY+line et stocke dans buf
		for(i=0;i<values.length+3;i++) {
			buf[x][i]=0;
		}
		for (dy = -windowSize()+1; dy < windowSize(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				ic = abs(dy);
				v = (int) inDatas()[((y + dy) * width()) + x];
							
				if(v == noDataValue()){
					mv = 0;
				}else if(v==0){
					mv = 1;
				}else{
					mv = mapValues[v]+3;
				}
				buf[x][mv] += gauss[ic];
			}
		}
	}

	public void processHorizontalPixel(int x, int line) {

		// parcours horizontal de buf: position dans imageOut : (x,y=line/dep)
		int x_buf = x*displacement();
		int y=line/displacement();
		int ind = y*((width()-1)/displacement()+1) + x;
		outDatas()[ind][2] = (int) inDatas()[((theY() + line)* width()) + x];
		float val;
		
		for(int value=0; value<values.length+3; value++) {
			if(value==2){
				continue;
			}
			val=0;
			for(int i=max(x_buf-windowSize()+1,0); i<min(x_buf+windowSize(),width()); i++) {
				val+=buf[i][value]*gauss[abs(i-x_buf)];
			}
			outDatas()[ind][value] = val;
		}
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.buf = new float[width()][values.length+3];
	}
	
	@Override
	public void applySlidingWindow(int theY, int buffer) {
		this.setTheY(theY); // indique position de la premiere ligne du buffer dans imageIn
		final int nlines = ((buffer-1) - (displacement()-theY()%displacement())%displacement())/displacement()+1;
		execute(width(), 2*nlines);
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + getGlobalId(0);
		final int pass = getPassId();
		final int vertical = pass % 2;	
		final int line = (displacement() - theY()%displacement())%displacement() + (pass/2)*displacement();
	
		if(vertical == 0){
			processVerticalPixel(x, line);
		}else if(x < (width() - bufferROIXMin() - bufferROIXMax()-1) / displacement() + 1){
			processHorizontalPixel(x, line);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
		mapValues = null;
		buf = null;
		gauss = null;
	}

}
