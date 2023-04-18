package fr.inrae.act.bagap.chloe.window.kernel.entity;

public class EntityCountCoupleKernel extends EntityLandscapeMetricKernel{

	private int[][] mapCouples;
	
	private int[] mapValues;
	
	private int[] lastEntityLine;
	
	private short[] lastValueLine;

	public EntityCountCoupleKernel(int noDataValue, int[] values){
		super(noDataValue);
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
		lastEntityLine = new int[width()];
		lastValueLine = new short[width()];
	}
	
	@Override
	public void applyEntityWindow(){
		
		int mc;
		short v = 0, v_H, v_V;
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				int va = (int) entityDatas()[y*width() + x];
				if(va != 0 && va != noDataValue()){
					
					v = (short) inDatas()[y*width() + x];
					
					// couple vertical
					if((y > 0) && entityDatas()[(y-1)*width() + x] == va){
					
						v_V = (short) inDatas()[(y-1)*width() + x];
						
						if(v == noDataValue() || v_V == noDataValue()){
							outDatas().get(va)[1] += 1;
						}else if(v == 0 || v_V == 0){
							outDatas().get(va)[2] += 1;
						}else{
							mc = mapCouples[mapValues[v]][mapValues[v_V]];
							outDatas().get(va)[mc+3] += 1;
						}
					}else if((y == 0) && lastEntityLine[x] == va){
						
						v_V = lastValueLine[x];
						
						if(v == noDataValue() || v_V == noDataValue()){
							outDatas().get(va)[1] += 1;
						}else if(v == 0 || v_V == 0){
							outDatas().get(va)[2] += 1;
						}else{
							mc = mapCouples[mapValues[v]][mapValues[v_V]];
							outDatas().get(va)[mc+3] += 1;
						}
					}
					
					// couple horizontal
					if((x > 0) && entityDatas()[y*width() + (x-1)] == va){
					
						v_H = (short) inDatas()[y*width() + (x-1)];
						
						if(v == noDataValue() || v_H == noDataValue()){
							outDatas().get(va)[1] += 1;
						}else if(v == 0 || v_H == 0){
							outDatas().get(va)[2] += 1;
						}else{
							mc = mapCouples[mapValues[v]][mapValues[v_H]];
							outDatas().get(va)[mc+3] += 1;
						}
					}
				}
				if(y == height()-1){
					lastEntityLine[x] = va;
					lastValueLine[x] = v;
				}
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapValues = null;
		mapCouples = null;
		lastEntityLine = null;
		lastValueLine = null;
	}
	
}
