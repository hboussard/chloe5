package fr.inrae.act.bagap.chloe.window.kernel.map;

public class MapCountValueKernel extends MapLandscapeMetricKernel {

	private int[] mapValues;
	
	public MapCountValueKernel(int noDataValue, int[] values){		
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
	public void applyMapWindow(int theY) {
			
		short v;
		int mv;				
		for(int y=0; y<height(); y++) {
			for(int x=0; x<width(); x++) {
					
				v = (short) inDatas()[((theY+y)*width()) + x];			
				outDatas()[2] += 1;
				
				if(v == noDataValue()){
					outDatas()[3] += 1;
				}else if(v == 0){
					outDatas()[4] += 1;
				}else{
					mv = mapValues[v];
					outDatas()[mv+5] += 1;	
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
