package fr.inrae.act.bagap.chloe.kernel.area;

public class AreaQuantitativeKernel extends AreaLandscapeMetricKernel{
	
	public AreaQuantitativeKernel(int noDataValue){
		super(noDataValue);
	}	
	
	@Override
	public void applyAreaWindow(){
		
		for(int y=0; y<roiHeight(); y++){
			for(int x=0; x<roiWidth(); x++){
				int va = (int) inAreaDatas()[y*roiWidth() + x];
				if(va != 0 && va != noDataValue()){
					float v = inDatas()[y*roiWidth() + x];
					
					if(v == noDataValue()) {
						outDatas().get(va)[0] += 1;
					}else{
						outDatas().get(va)[1] += 1;
						outDatas().get(va)[2] += v;
						outDatas().get(va)[3] += v*v;
						outDatas().get(va)[4] = Math.min(outDatas().get(va)[4], v);
						outDatas().get(va)[5] = Math.max(outDatas().get(va)[5], v);
					}
					outDatas().get(va)[6] = -1;
				}
			}
		}
	}
	
}
