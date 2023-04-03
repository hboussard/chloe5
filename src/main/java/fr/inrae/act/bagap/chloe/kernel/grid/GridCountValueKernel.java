package fr.inrae.act.bagap.chloe.kernel.grid;

public class GridCountValueKernel extends GridLandscapeMetricKernel {

	private final int[] mapValues;
	
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
		
		for(int i=0; i<imageOut()[0].length; i++){
			imageOut()[x][i] = 0f;
		}
			
		short v;
		int mv;				
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) imageIn()[((theY+y)*width()) + (x*gridSize() + lx)];		
						if(v == noDataValue()){
							imageOut()[x][0] += 1;
						}else if(v == 0){
							imageOut()[x][1] += 1;
						}else{
							mv = mapValues[v];
							imageOut()[x][mv+3] += 1;	
						}
					}
				}
			}
		}
	}

}
