package fr.inrae.act.bagap.chloe.kernel;

public class AreaCountCoupleKernelOld extends AreaLandscapeMetricKernel{

	private final float[] couples;

	public AreaCountCoupleKernelOld(int noDataValue, float[] couples){
		super(noDataValue);
		this.couples = couples;
	}	
	
	@Override
	public void applyAreaWindow(){
		
		boolean again;
		float c;
		for(int y=0; y<roiHeight(); y++){
			for(int x=0; x<roiWidth(); x++){
				int va = (int) inAreaDatas()[y*roiWidth() + x];
				if(va != 0 && va != noDataValue()){
					
					short v = (short) inDatas()[y*roiWidth() + x];
					
					// couple vertical
					if((y > 0) && inAreaDatas()[(y-1)*roiWidth() + x] == va){
					
						c = getCouple(v, (short) inDatas()[(y-1)*roiWidth() + x]);
						
						if(c == noDataValue()){
							outDatas().get(va)[0] = outDatas().get(va)[0] + 1;
						}else if(c == 0){
							outDatas().get(va)[1] = outDatas().get(va)[1] + 1;
						}else{
							again = true;
							for(int i=0; again && i<couples.length; i++){
								if(c == couples[i]){
									outDatas().get(va)[i+2] = outDatas().get(va)[i+2] + 1;
									again = false;
								}
							}
						}
					}
					
					// couple horizontal
					if((x > 0) && inAreaDatas()[y*roiWidth() + (x-1)] == va){
					
						c = getCouple(v, (short) inDatas()[y*roiWidth() + (x-1)]);
						
						if(c == noDataValue()){
							outDatas().get(va)[0] = outDatas().get(va)[0] + 1;
						}else if(c == 0){
							outDatas().get(va)[1] = outDatas().get(va)[1] + 1;
						}else{
							again = true;
							for(int i=0; again && i<couples.length; i++){
								if(c == couples[i]){
									outDatas().get(va)[i+2] = outDatas().get(va)[i+2] + 1;
									again = false;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private float getCouple(short v1, short v2) {
		
		if(v1 == -1 || v2 == -1) {
			return -1;
		}
		if(v1 == 0 || v2 == 0) {
			return 0;
		}
		if(v1 < v2) {
			return (float) (v2 * 0.0001 + v1);
		}else {
			return (float) (v1 * 0.0001 + v2);
		}
	}
	
}
