package fr.inrae.act.bagap.chloe.window.kernel.grid;

public class GridQuantitativeKernel extends GridLandscapeMetricKernel {
	
	public GridQuantitativeKernel(int gridSize, int noDataValue){		
		super(gridSize, noDataValue);
	}
	
	@Override
	protected void processGrid(int x, int theY) {
		
		outDatas()[x][0] = 1; // filtre ok
			
		short v;
		float nb_nodata = 0;
		float nb = 0;
		float sum = 0;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;			
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) inDatas()[((theY+y)*width()) + (x*gridSize() + lx)];	
						nb += 1;
						if(v == noDataValue()) {
							nb_nodata += 1;
						}else{
							sum += v;
							square_sum += v * v;
							min = Math.min(min, v);
							max = Math.max(max, v);
						}
					}
				}
			}
		}
		
		outDatas()[x][2] = nb;
		outDatas()[x][3] = nb_nodata;
		outDatas()[x][4] = sum;
		outDatas()[x][5] = square_sum;
		outDatas()[x][6] = min;
		outDatas()[x][7] = max;
	}

}
