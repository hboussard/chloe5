package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;
import fr.inrae.act.bagap.chloe.kernel.SlidingLandscapeMetricKernel;


public class FastGaussianWeightedCountCoupleKernel extends SlidingLandscapeMetricKernel {

	private final int[] values;
	private final int[] mapValues;
	private final int[][] mapCouples;
	private final int nCouplesValues;

	private float[][] buf;
	private final float[] gauss;
	
	
	public FastGaussianWeightedCountCoupleKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] values, int[] unfilters){
		super( windowSize, displacement, shape, coeff, noDataValue, unfilters);

		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		//this.setExecutionMode(Kernel.EXECUTION_MODE.SEQ);
		
		this.values = values;
		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
		mapCouples = new int[values.length][values.length];
		int index = 3;
		for(int v : values){
			mapCouples[mapValues[v]][mapValues[v]] = index;
			index++;
		}
		
		for(int v1 : values){
			for(int v2 : values){
				if(v1 < v2) {
					mapCouples[mapValues[v1]][mapValues[v2]] = index;
					mapCouples[mapValues[v2]][mapValues[v1]] = index;
					index++;
				}
			}
		}
		nCouplesValues = index;
		this.gauss = new float[windowSize()];
		
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
		for(i=0;i<buf[0].length;i++) {
			buf[x][i]=0;
		}
		for (dy = -windowSize()+1; dy < windowSize(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				int ic = abs(dy);
				int v = (int)imageIn()[((y + dy) * width()) + x];
				int mc;
				if(y+dy>0) {
					int v_V=(int)imageIn()[((y + dy - 1) * width()) + x];
					if(v == noDataValue() || v_V == noDataValue())
						mc=0;
					else if (v==0 || v_V == 0)
						mc=1;
					else
						mc = mapCouples[mapValues[v]][mapValues[v_V]];
					buf[x][mc] += gauss[ic];
				}
				if(x>0) {
					int v_H=(int)imageIn()[((y + dy) * width()) + x - 1];
					if(v == noDataValue() || v_H == noDataValue())
						mc=0;
					else if (v==0 || v_H == 0)
						mc=1;
					else
						mc = mapCouples[mapValues[v]][mapValues[v_H]];
					buf[x][mc] += gauss[ic];
				}
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
		
		for(int value=0; value<nCouplesValues;value++) {
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
		this.buf = new float[width()][nCouplesValues];

	}
	
	@Override
	public void applySlidingWindow(int theY, int buffer) {
		this.setTheY(theY); // indique position de la première ligne du buffer dans imageIn
		//execute(width * buffer);
		//execute((width - bufferROIXMin - bufferROIXMax) * buffer);
		final int nlines = ((buffer-1) - (displacement()-theY()%displacement())%displacement() )/displacement()+1;
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
