package fr.inrae.act.bagap.chloe.kernel.sliding;

public class SlidingCountValueKernel extends SlidingLandscapeMetricKernel {

	private int[] mapValues;
	
	public SlidingCountValueKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] values, int[] unfilters){		
		super(windowSize, displacement, coeff, noDataValue, unfilters);
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
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0f;
			}
			
			outDatas()[ind][3] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			if(filter((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				final int mid = windowSize() / 2;
				int ic;
				short v;
				int mv;	
				float coeff;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (short) inDatas()[((y + dy) * width()) + (x + dx)];		
									if(v == noDataValue()){
										outDatas()[ind][1] += coeff;
									}else if(v == 0){
										outDatas()[ind][2] += coeff;
									}else{
										mv = mapValues[v];
										outDatas()[ind][mv+4] += coeff;	
									}
								}
							}
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
