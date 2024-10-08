package fr.inrae.act.bagap.chloe.window.kernel.entity;

public class EntityCountValueKernel extends EntityLandscapeMetricKernel{

	private int[] mapValues;

	public EntityCountValueKernel(int noDataValue, int[] values){
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
	}	
	
	@Override
	public void applyEntityWindow(){
		int mv, va;
		short v;
		
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				va = (int) entityDatas()[y*width() + x];
				if(va != 0 && va != noDataValue()){
					
					v = (short) inDatas()[y*width() + x];
					outDatas().get(va)[2] += 1;
					
					if(v == noDataValue()){
						outDatas().get(va)[3] += 1;
					}else if(v == 0){
						outDatas().get(va)[4] += 1;
					}else{
						mv = mapValues[v];
						outDatas().get(va)[mv+5] += 1;
					}
				}
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapValues = null;
	}
	
}
