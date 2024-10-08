package fr.inrae.act.bagap.chloe.window.kernel.map;

public class MapCountValueAndCoupleKernel extends MapLandscapeMetricKernel {

	private final int nbValues;
	
	private int[][] mapCouples;
	
	private int[] mapValues;
	
	private short[] lastValueLine;
	
	public MapCountValueAndCoupleKernel(int noDataValue, int[] values){		
		super(noDataValue);
		this.nbValues = values.length;
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
	public void init(){
		lastValueLine = new short[width()];
	}
	
	@Override
	public void applyMapWindow(int theY) {
			
		short v, v_H, v_V;
		int mv;				
		for(int y=0; y<height(); y++) {
			for(int x=0; x<width(); x++) {
					
				v = (short) inDatas()[(y*width()) + x];			
				outDatas()[2] += 1;
				
				if(v == noDataValue()){
					outDatas()[3] += 1;
				}else if(v == 0){
					outDatas()[4] += 1;
				}else{
					mv = mapValues[v];
					outDatas()[mv+5] += 1;	
				}
						
				if(y > 0) {
					v_V = (short) inDatas()[((y-1)*width()) + x];
					outDatas()[nbValues+5] += 1;
					
					if(v == noDataValue() || v_V == noDataValue()){
						outDatas()[nbValues+6] += 1;
					}else if(v == 0 || v_V == 0){
						outDatas()[nbValues+7] += 1;
					}else{
						mv = mapCouples[mapValues[v]][mapValues[v_V]];
						outDatas()[nbValues+mv+8] += 1;
					}
				}else if(theY != 0 && y == 0){
					
					v_V = lastValueLine[x];
					outDatas()[nbValues+5] += 1;
					
					if(v == noDataValue() || v_V == noDataValue()){
						outDatas()[nbValues+6] += 1;
					}else if(v == 0 || v_V == 0){
						outDatas()[nbValues+7] += 1;
					}else{
						mv = mapCouples[mapValues[v]][mapValues[v_V]];
						outDatas()[nbValues+mv+8] += 1;
					}
				}
						
				if(x > 0) {
					v_H = (short) inDatas()[(y*width()) + (x - 1)];
					outDatas()[nbValues+5] += 1;
					
					if(v == noDataValue() || v_H == noDataValue()){
						outDatas()[nbValues+6] += 1;
					}else if(v == 0 || v_H == 0){
						outDatas()[nbValues+7] += 1;
					}else{
						mv = mapCouples[mapValues[v]][mapValues[v_H]];
						outDatas()[nbValues+mv+8] += 1;
					}
				}
				
				if(y == height()-1){
					lastValueLine[x] = v;
				}
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
		mapValues = null;
		lastValueLine = null;
	}
	
}
