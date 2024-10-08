package fr.inrae.act.bagap.chloe.window.kernel.grid;

public class GridCountCoupleKernel extends GridLandscapeMetricKernel {

	private int[][] mapCouples;
	
	private int[] mapValues;
	
	public GridCountCoupleKernel(int gridSize, int noDataValue, int[] values){		
		super(gridSize, noDataValue);
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
	protected void processGrid(int x, int theY) {
		
		outDatas()[x][0] = 1; // filtre ok
		
		for(int i=1; i<outDatas()[0].length; i++){
			outDatas()[x][i] = 0f;
		}
			
		short v, v_H, v_V;
		int mc;				
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) inDatas()[((theY+y)*width()) + (x*gridSize() + lx)];
						outDatas()[x][2] += 1;
						if(v == noDataValue()){
							outDatas()[x][3] += 1;
						}
						
						if(y > 0) {
							v_V = (short) inDatas()[((theY+y-1)*width()) + (x*gridSize() + lx)];
							outDatas()[x][4] += 1;
							if(v == noDataValue() || v_V == noDataValue()){
								outDatas()[x][5] += 1;
							}else if(v == 0 || v_V == 0){
								outDatas()[x][6] += 1;
							}else{
								mc = mapCouples[mapValues[v]][mapValues[v_V]];
								outDatas()[x][mc+7] += 1;
							}
						}
						
						if(lx > 0) {
							v_H = (short) inDatas()[((theY+y)*width()) + (x*gridSize() + lx - 1)];
							outDatas()[x][4] += 1;
							if(v == noDataValue() || v_H == noDataValue()){
								outDatas()[x][5] += 1;
							}else if(v == 0 || v_H == 0){
								outDatas()[x][6] += 1;
							}else{
								mc = mapCouples[mapValues[v]][mapValues[v_H]];
								outDatas()[x][mc+7] += 1;
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
