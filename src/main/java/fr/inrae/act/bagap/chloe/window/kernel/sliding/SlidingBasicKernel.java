package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public class SlidingBasicKernel extends SlidingLandscapeMetricKernel {
	
	public SlidingBasicKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters){		
		super(windowSize, displacement, coeff, noDataValue, unfilters);
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0;
				}
				
				final int mid = windowSize() / 2;
				int ic;
				short v;
				float coeff;
				float nb = 0;
				float nb_nodata = 0;
				float nb_zero = 0;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (short) inDatas()[((y + dy) * width()) + (x + dx)];
									nb += coeff;
									if(v == noDataValue()){
										nb_nodata += coeff;
									}else if(v == 0){
										nb_zero += coeff;
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = nb_zero;
				
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}

}
