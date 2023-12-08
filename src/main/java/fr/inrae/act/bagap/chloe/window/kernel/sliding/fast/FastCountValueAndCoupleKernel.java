package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastCountValueAndCoupleKernel extends FastQualitativeKernel {
	
	private final int nValues;
	
	private int[][] mapCouples;
	
	public FastCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
		
		nValues = values.length;
		
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
		setNValuesTot(5 + values.length + 3 + index);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, v, mv, v_V, v_H;
		float coeff;
		for(i=0;i<nValuesTot();i++) {
			buf()[x][i] = 0;
		}
		float nb = 0;
		float nb_nodata = 0;
		float nb_zero = 0;
		float nbC = 0;
		float nbC_nodata = 0;
		float nbC_zero = 0;
		for (dy = -rayon()+1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = (int) inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);	
				
				nb += coeff;
				
				if(v == noDataValue()){
					nb_nodata += coeff;
				}else if(v==0){
					nb_zero += coeff;
				}else{
					mv = mapValues()[v] + 5;
					buf()[x][mv] += coeff;
				}
				
				int mc;
				if(y+dy>0) {
					v_V = (int) inDatas()[((y + dy - 1) * width()) + x];
					nbC += coeff;
					
					if(v == noDataValue() || v_V == noDataValue()){
						nbC_nodata += coeff;
					}else if (v==0 || v_V == 0){
						nbC_zero += coeff;
					}else{
						mc = nValues + 8 + mapCouples[mapValues()[v]][mapValues()[v_V]];
						buf()[x][mc] += coeff;
					}
					
				}
				
				if(x>0) {
					v_H = (int) inDatas()[((y + dy) * width()) + x - 1];
					nbC += coeff;
					if(v == noDataValue() || v_H == noDataValue()){
						nbC_nodata += coeff;
					}else if (v==0 || v_H == 0){
						nbC_zero += coeff;
					}else{
						mc = nValues + 8 + mapCouples[mapValues()[v]][mapValues()[v_H]];
						buf()[x][mc] += coeff;
					}
					
				}
			}
		}
		
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = nb_zero;
		buf()[x][nValues+5] = nbC;
		buf()[x][nValues+6] = nbC_nodata;
		buf()[x][nValues+7] = nbC_zero;
		
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
	}
	
}
