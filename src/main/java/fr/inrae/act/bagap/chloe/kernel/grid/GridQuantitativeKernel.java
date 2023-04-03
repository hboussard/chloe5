package fr.inrae.act.bagap.chloe.kernel.grid;

public class GridQuantitativeKernel extends GridLandscapeMetricKernel {
	
	public GridQuantitativeKernel(int gridSize, int noDataValue){		
		super(gridSize, noDataValue);
	}
	
	@Override
	protected void processGrid(int x, int theY) {
		
		for(int i=0; i<imageOut()[0].length; i++){
			imageOut()[x][i] = 0f;
		}
			
		short v;
		float nb_nodata = 0;
		float nb = 0;
		float sum = 0;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		int mv;				
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (short) imageIn()[((theY+y)*width()) + (x*gridSize() + lx)];	
						
						if(v == noDataValue()) {
							nb_nodata += 1;
						}else{
							nb += 1;
							sum += v;
							square_sum += v * v;
							min = Math.min(min, v);
							max = Math.max(max, v);
						}
					}
				}
			}
		}
		
		imageOut()[x][0] = nb_nodata;
		imageOut()[x][1] = nb;
		imageOut()[x][2] = sum;
		imageOut()[x][3] = square_sum;
		imageOut()[x][4] = min;
		imageOut()[x][5] = max;
	}

}
