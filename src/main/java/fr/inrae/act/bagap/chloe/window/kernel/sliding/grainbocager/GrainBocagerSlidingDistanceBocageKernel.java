package fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.AbstractDoubleSlidingLandscapeMetricKernel;

public class GrainBocagerSlidingDistanceBocageKernel extends AbstractDoubleSlidingLandscapeMetricKernel {

	private float cellSize; // = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur; // = 3; // 3 metres minimum 
	
	private float seuilMax; // = 30; // 30 metres maximum
	
	public GrainBocagerSlidingDistanceBocageKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters, float cellSize, float minHauteur, float seuilMax) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		this.cellSize = cellSize;
		this.minHauteur = minHauteur;
		this.seuilMax = seuilMax;
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
				float v, coeff;
				float nb_nodata = 0;
				float nb = 0;
				double min = 1.0;
				double r, R;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = inDatas()[((y + dy) * width()) + (x + dx)];
									nb += coeff;
									
									if(v == noDataValue()) {
										nb_nodata += coeff;
									}else{
										if(v >= minHauteur){
											
											if(v > seuilMax){
												v = seuilMax;
											}
											
											r = cellSize*Math.sqrt((dx*dx)+(dy*dy));
											R = v * inDatas2()[((y + dy) * width()) + (x + dx)];	
											
											if(r < R){
												min = Math.min(min, r/R);
											}
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][6] = min;
				
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
			}
		}
	}

}
