package fr.inrae.act.bagap.chloe.window.kernel.grid;

public class GridCountValueKernel extends GridLandscapeMetricKernel {

	private int[] mapValues;
	
	public GridCountValueKernel(int gridSize, int noDataValue, int[] values){		
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
	}
	
	@Override
	protected void processGrid(int x, int theY) {
		
		for(int i=0; i<outDatas()[0].length; i++){
			outDatas()[x][i] = 0f;
		}
		
		outDatas()[x][0] = 1; // filtre ok
			
		short v;
		int mv;				
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) inDatas()[((theY+y)*width()) + (x*gridSize() + lx)];		
						if(v == noDataValue()){
							outDatas()[x][1] += 1;
						}else if(v == 0){
							outDatas()[x][2] += 1;
						}else{
							mv = mapValues[v];
							outDatas()[x][mv+4] += 1;	
						}
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
