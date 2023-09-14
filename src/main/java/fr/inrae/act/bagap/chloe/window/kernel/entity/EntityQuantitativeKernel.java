package fr.inrae.act.bagap.chloe.window.kernel.entity;

public class EntityQuantitativeKernel extends EntityLandscapeMetricKernel{
	
	public EntityQuantitativeKernel(int noDataValue){
		super(noDataValue);
	}	
	
	@Override
	public void init(){
		for(int ent : outDatas().keySet()){
			outDatas().get(ent)[6] = Float.MAX_VALUE; // init minimum
		}
	}
	
	@Override
	public void applyEntityWindow(){
		
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				int va = (int) entityDatas()[y*width() + x];
				if(va != 0 && va != noDataValue()){
					float v = inDatas()[y*width() + x];
					outDatas().get(va)[2] += 1;
					if(v == noDataValue()) {
						outDatas().get(va)[3] += 1;
					}else{
						outDatas().get(va)[4] += v;
						outDatas().get(va)[5] += v*v;
						outDatas().get(va)[6] = Math.min(outDatas().get(va)[6], v);
						outDatas().get(va)[7] = Math.max(outDatas().get(va)[7], v);
					}
				}
			}
		}	
				
	}
	
}
