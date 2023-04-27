package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastCountCoupleKernel extends FastQualitativeKernel {
	
	private int[][] mapCouples;
	
	public FastCountCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
		
		mapCouples = new int[values.length][values.length];
		int index = 0;
		for(int v : values){
			mapCouples[mapValues()[v]][mapValues()[v]] = index;
			index++;
		}
		
		for(int v1 : values){
			for(int v2 : values){
				if(v1 < v2) {
					mapCouples[mapValues()[v1]][mapValues()[v2]] = index;
					mapCouples[mapValues()[v2]][mapValues()[v1]] = index;
					index++;
				}
			}
		}
		setNValuesTot(7 + index);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, v, mc, v_V, v_H;
		float coeff;
		for(i=0;i<nValuesTot();i++) {
			buf()[x][i] = 0;
		}
		float nb = 0;
		float nb_nodata = 0;
		float nbC = 0;
		float nbC_nodata = 0;
		float nbC_zero = 0;
		for (dy = -rayon()+1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = (int) inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);
				//buf()[x][2] += coeff;
				nb += coeff;
				if(v == noDataValue()){
					//buf()[x][3] += coeff;
					nb_nodata += coeff;
				}
				
				if(y+dy>0) {
					v_V = (int) inDatas()[((y + dy - 1) * width()) + x];
					//buf()[x][4] += coeff;
					nbC += coeff;
					if(v == noDataValue() || v_V == noDataValue()){
						//mc = 5;
						nbC_nodata += coeff;
					}else if (v==0 || v_V == 0){
						//mc = 6;
						nbC_zero += coeff;
					}else{
						mc = 7 + mapCouples[mapValues()[v]][mapValues()[v_V]];
						buf()[x][mc] += coeff;
					}
					
				}
				
				if(x>0) {
					v_H = (int) inDatas()[((y + dy) * width()) + x - 1];
					buf()[x][4] += coeff;
					if(v == noDataValue() || v_H == noDataValue()){
						//mc = 5;
						nbC_nodata += coeff;
					}else if (v==0 || v_H == 0){
						//mc = 6;
						nbC_zero += coeff;
					}else{
						mc = 7 + mapCouples[mapValues()[v]][mapValues()[v_H]];
						buf()[x][mc] += coeff;
					}
					
				}
			}
		}
		
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = nbC;
		buf()[x][5] = nbC_nodata;
		buf()[x][6] = nbC_zero;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
	}
	
}
