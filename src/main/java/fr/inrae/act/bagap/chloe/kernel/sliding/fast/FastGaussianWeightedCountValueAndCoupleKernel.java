package fr.inrae.act.bagap.chloe.kernel.sliding.fast;

import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;

public class FastGaussianWeightedCountValueAndCoupleKernel extends SlidingLandscapeMetricKernel {

	//private final int[] values;
	
	private int[] mapValues;
	
	private int[][] mapCouples;
	
	private final int nValues;
	
	private final int nValuesTot;
	
	private float[][] buf;
	
	private int rayon;
	
	private float[] gauss;
	
	public FastGaussianWeightedCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super( windowSize, displacement, null, noDataValue, unfilters);

		//this.values = values;
		this.nValues = values.length;
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
		int index = 0;
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
		nValuesTot = 2+nValues+index+3;
		this.rayon = windowSize()/2+1;
		this.gauss = new float[rayon];
		
		float r=(rayon-1)/2.f; // sigma = r/sqrt(2), rayon = 2*sqrt(2)*sigma
		for(int i=0; i<rayon; i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i] = (float) Math.exp(-d*d);
		}
	}
	
	public void processVerticalPixel(int x,int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, ic, v, mv, v_V, v_H;
		
		// parcours vertical de l'image centre sur ligne theY+line et stocke dans buf
		for(i=0;i<buf[0].length;i++) {
			buf[x][i]=0;
		}
		
		for (dy = -rayon+1; dy < rayon; dy++) {
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
				int mc;
				if(y+dy>0) {
					v_V = (int) inDatas()[((y + dy - 1) * width()) + x];
					if(v == noDataValue() || v_V == noDataValue()){
						mc = nValues+3;
					}else if (v==0 || v_V == 0){
						mc = nValues+4;
					}else{
						mc = nValues+5+mapCouples[mapValues[v]][mapValues[v_V]];
					}
					buf[x][mc] += gauss[ic];
				}
				if(x>0) {
					v_H = (int) inDatas()[((y + dy) * width()) + x - 1];
					if(v == noDataValue() || v_H == noDataValue()){
						mc=nValues+3;
					}else if (v==0 || v_H == 0){
						mc=nValues+4;
					}else{
						mc = nValues+5+mapCouples[mapValues[v]][mapValues[v_H]];
					}
					buf[x][mc] += gauss[ic];
				}
			}
		}
	}

	public void processHorizontalPixel(int x,int line) {

		// parcours horizontal de buf: position dans imageOut : (x,y=line/dep)
		int x_buf = x * displacement() + bufferROIXMin();
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		outDatas()[ind][2] = (int) inDatas()[((theY() + line) * width()) + x*displacement() + bufferROIXMin()];
		float val;
		
		for(int value=0; value<nValuesTot; value++) {
			val=0;
			for(int i=max(x_buf-rayon+1, 0); i<min(x_buf+rayon,width()); i++) {
				val += buf[i][value] * gauss[abs(i-x_buf)];
			}
			outDatas()[ind][value] = val;
		}
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.buf = new float[width][nValuesTot];
	}
	
	@Override
	public void applySlidingWindow(int theY, int buffer) {
		this.setTheY(theY); // indique position de la premiere ligne du buffer dans imageIn
		final int nlines = ((buffer-1) - (displacement()-theY()%displacement())%displacement() )/displacement()+1;
		execute(width(), 2*nlines);
	}
	
	@Override
	public void run() {
		final int x = getGlobalId(0);
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
		mapValues = null;
		mapCouples = null;
		buf = null;
		gauss = null;
	}
	
}
