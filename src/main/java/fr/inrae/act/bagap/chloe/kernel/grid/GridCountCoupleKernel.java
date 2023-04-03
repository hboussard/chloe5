package fr.inrae.act.bagap.chloe.kernel.grid;

public class GridCountCoupleKernel extends GridLandscapeMetricKernel {

	private final int[][] mapCouples;
	
	private final int[] mapValues;
	
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
		
		for(int i=0; i<imageOut()[0].length; i++){
			imageOut()[x][i] = 0f;
		}
			
		short v, v_H, v_V;
		int mc;				
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) imageIn()[((theY+y)*width()) + (x*gridSize() + lx)];	
						
						if(y > 0) {
							v_V = (short) imageIn()[((theY+y-1)*width()) + (x*gridSize() + lx)];
							
							if(v == noDataValue() || v_V == noDataValue()){
								imageOut()[x][0] += 1;
							}else if(v == 0 || v_V == 0){
								imageOut()[x][1] += 1;
							}else{
								mc = mapCouples[mapValues[v]][mapValues[v_V]];
								imageOut()[x][mc+2] += 1;
							}
						}
						
						if(lx > 0) {
							v_H = (short) imageIn()[((theY+y)*width()) + (x*gridSize() + lx - 1)];
							
							if(v == noDataValue() || v_H == noDataValue()){
								imageOut()[x][0] += 1;
							}else if(v == 0 || v_H == 0){
								imageOut()[x][1] += 1;
							}else{
								mc = mapCouples[mapValues[v]][mapValues[v_H]];
								imageOut()[x][mc+2] += 1;
							}
						}
					}
				}
			}
		}
	}

}
