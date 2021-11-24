package fr.inrae.act.bagap.chloe.kernel;

public class AreaCountValueKernel extends AreaLandscapeMetricKernel{

	private final int[] mapValues;

	public AreaCountValueKernel(int noDataValue, short[] values){
		super(noDataValue);
		int maxV = 0;
		for(short v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
	}	
	
	@Override
	public void applyAreaWindow(){
		int mv;
		short v;
		for(int y=0; y<roiHeight(); y++){
			for(int x=0; x<roiWidth(); x++){
				int va = (int) inAreaDatas()[y*roiWidth() + x];
				if(va != 0 && va != noDataValue()){
					v = (short) inDatas()[y*roiWidth() + x];
					if(v == noDataValue()){
						outDatas().get(va)[0] = outDatas().get(va)[0] + 1;
					}else if(v == 0){
						outDatas().get(va)[1] = outDatas().get(va)[1] + 1;
					}else{
						mv = mapValues[v];
						outDatas().get(va)[mv+2] = outDatas().get(va)[mv+2] + 1;
					}
				}
			}
		}
	}
	
}
