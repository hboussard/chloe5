package fr.inrae.act.bagap.chloe.kernel.sliding.fast;

public class FastSquareCountValueAndCoupleKernel extends FastKernel {
	
	private final int nValues;
	
	private int[][] mapCouples;
	
	public FastSquareCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
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
		setNValuesTot(4 + values.length + 2 + index);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, v, mv, v_V, v_H;
		
		for(i=0;i<nValuesTot();i++) {
			buf()[x][i] = 0;
		}
		
		for (dy = -rayon()+1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = (int) inDatas()[((y + dy) * width()) + x];
							
				if(v == noDataValue()){
					mv = 1;
				}else if(v==0){
					mv = 2;
				}else{
					mv = mapValues()[v] + 4;
				}
				buf()[x][mv] += 1;
				int mc;
				if(y+dy>0) {
					v_V = (int) inDatas()[((y + dy - 1) * width()) + x];
					if(v == noDataValue() || v_V == noDataValue()){
						mc = nValues + 4;
					}else if (v==0 || v_V == 0){
						mc = nValues + 5;
					}else{
						mc = nValues + 6 + mapCouples[mapValues()[v]][mapValues()[v_V]];
					}
					buf()[x][mc] += 1;
				}
				if(x>0) {
					v_H = (int) inDatas()[((y + dy) * width()) + x - 1];
					if(v == noDataValue() || v_H == noDataValue()){
						mc = nValues + 4;
					}else if (v==0 || v_H == 0){
						mc = nValues + 5;
					}else{
						mc = nValues + 6 + mapCouples[mapValues()[v]][mapValues()[v_H]];
					}
					buf()[x][mc] += 1;
				}
			}
		}
	}

	@Override
	protected void processHorizontalPixel(int x, int line) {

		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		outDatas()[ind][3] = (int) inDatas()[((theY() + line) * width()) + x*displacement() + bufferROIXMin()]; // valeur pixel central
		
		if(filter((int) inDatas()[((theY() + line)* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok
		
			int x_buf = x * displacement() + bufferROIXMin();
			float val;
			for(int value=1; value<nValuesTot(); value++) {
				if(value==3){
					continue;
				}
				val = 0;
				for(int i=max(x_buf-rayon()+1, 0); i<min(x_buf+rayon(), width()); i++) {
					val += buf()[i][value];
				}
				outDatas()[ind][value] = val;
			}
			
		}else{
			// filtre pas ok
			for(int value=0; value<nValuesTot(); value++) {
				outDatas()[ind][value] = 0;
			}			
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
	}
	
}
