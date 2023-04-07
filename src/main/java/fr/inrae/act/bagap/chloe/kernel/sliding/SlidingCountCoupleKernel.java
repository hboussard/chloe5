package fr.inrae.act.bagap.chloe.kernel.sliding;

public class SlidingCountCoupleKernel extends SlidingLandscapeMetricKernel {
	
	private int[][] mapCouples;
	
	private int[] mapValues;
	
	public SlidingCountCoupleKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
		
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
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0f;
			}
			
			if(filter((short) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				final int mid = windowSize() / 2;
				int ic, ic_V, ic_H;
				short v, v_H, v_V;
				int mc;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								if(shape()[ic] == 1) {
									v = (short) inDatas()[((y + dy) * width()) + (x + dx)];
									
									if((dy > -mid) && ((y + dy) > 0)) {
										ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
										if(shape()[ic_V] == 1){
											v_V = (short) inDatas()[((y + dy - 1) * width()) + (x + dx)];
											if(v == noDataValue() || v_V == noDataValue()){
												outDatas()[ind][0] += coeff()[ic];
											}else if(v == 0 || v_V == 0){
												outDatas()[ind][1] += coeff()[ic];
											}else{
												mc = mapCouples[mapValues[v]][mapValues[v_V]];
												outDatas()[ind][mc+2] += coeff()[ic];
											}
										}
									}
									
									if((dx > -mid) && ((x + dx) > 0)) {
										ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
										if(shape()[ic_H] == 1){
											v_H = (short) inDatas()[((y + dy) * width()) + (x + dx - 1)];
											if(v == noDataValue() || v_H == noDataValue()){
												outDatas()[ind][0] += coeff()[ic];
											}else if(v == 0 || v_H == 0){
												outDatas()[ind][1] += coeff()[ic];
											}else{
												mc = mapCouples[mapValues[v]][mapValues[v_H]];
												outDatas()[ind][mc+2] += coeff()[ic];
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
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
		mapValues = null;
	}
	
}
