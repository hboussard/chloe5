package fr.inrae.act.bagap.chloe.kernel;

public class AreaCountCoupleKernel extends AreaLandscapeMetricKernel{

	private final int[][] mapCouples;
	
	private final int[] mapValues;

	public AreaCountCoupleKernel(int noDataValue, int[] values){
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
	public void applyAreaWindow(){
		
		int mc;
		short v, v_H, v_V;
		for(int y=0; y<roiHeight(); y++){
			for(int x=0; x<roiWidth(); x++){
				int va = (int) inAreaDatas()[y*roiWidth() + x];
				if(va != 0 && va != noDataValue()){
					
					v = (short) inDatas()[y*roiWidth() + x];
					
					// couple vertical
					if((y > 0) && inAreaDatas()[(y-1)*roiWidth() + x] == va){
					
						v_V = (short) inDatas()[(y-1)*roiWidth() + x];
						
						if(v == noDataValue() || v_V == noDataValue()){
							outDatas().get(va)[0] = outDatas().get(va)[0] + 1;
						}else if(v == 0 || v_V == 0){
							outDatas().get(va)[1] = outDatas().get(va)[1] + 1;
						}else{
							mc = mapCouples[mapValues[v]][mapValues[v_V]];
							outDatas().get(va)[mc+2] = outDatas().get(va)[mc+2] + 1;
						}
					}
					
					// couple horizontal
					if((x > 0) && inAreaDatas()[y*roiWidth() + (x-1)] == va){
					
						v_H = (short) inDatas()[y*roiWidth() + (x-1)];
						
						if(v == noDataValue() || v_H == noDataValue()){
							outDatas().get(va)[0] = outDatas().get(va)[0] + 1;
						}else if(v == 0 || v_H == 0){
							outDatas().get(va)[1] = outDatas().get(va)[1] + 1;
						}else{
							mc = mapCouples[mapValues[v]][mapValues[v_H]];
							outDatas().get(va)[mc+2] = outDatas().get(va)[mc+2] + 1;
						}
					}
				}
			}
		}
	}
	
}