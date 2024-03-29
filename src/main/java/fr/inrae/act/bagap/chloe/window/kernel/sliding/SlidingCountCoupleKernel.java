package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public class SlidingCountCoupleKernel extends SlidingLandscapeMetricKernel {
	
	private int[][] mapCouples;
	
	private int[] mapValues;
	
	public SlidingCountCoupleKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		
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
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				int ic, ic_V, ic_H;
				short v, v_H, v_V;
				int mc;
				float coeff;
				float nb = 0;
				float nb_nodata = 0;
				float nbC = 0;
				float nbC_nodata = 0;
				float nbC_zero = 0;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (short) inDatas()[((y + dy) * width()) + (x + dx)];
									nb += coeff;
									if(v == noDataValue()){
										nb_nodata += coeff;
									}
									
									if((dy > -mid) && ((y + dy) > 0)) {
										ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
										if(coeff()[ic_V] > 0){
											v_V = (short) inDatas()[((y + dy - 1) * width()) + (x + dx)];
											nbC += coeff;
											if(v == noDataValue() || v_V == noDataValue()){
												nbC_nodata += coeff;
											}else if(v == 0 || v_V == 0){
												nbC_zero += coeff;
											}else{
												mc = mapCouples[mapValues[v]][mapValues[v_V]];
												outDatas()[ind][mc+7] += coeff;
											}
										}
									}
									
									if((dx > -mid) && ((x + dx) > 0)) {
										ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
										if(coeff()[ic_H] > 0){
											v_H = (short) inDatas()[((y + dy) * width()) + (x + dx - 1)];
											nbC += coeff;
											if(v == noDataValue() || v_H == noDataValue()){
												nbC_nodata += coeff;
											}else if(v == 0 || v_H == 0){
												nbC_zero += coeff;
											}else{
												mc = mapCouples[mapValues[v]][mapValues[v_H]];
												outDatas()[ind][mc+7] += coeff;
											}
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = nbC;
				outDatas()[ind][5] = nbC_nodata;
				outDatas()[ind][6] = nbC_zero;
				
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
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
