package fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;

public class GrainBocagerSlidingDetectionBocageKernel extends SlidingLandscapeMetricKernel {

	private float minHauteur = 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public GrainBocagerSlidingDetectionBocageKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
	}

	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			// phase d'initialisation de la structure de donnees
			outDatas()[ind][1] = 0.0f; // nb nodataValue
			outDatas()[ind][2] = 0.0f; // nb value
			outDatas()[ind][3] = 0.0f; // value
			
			final int mid = windowSize() / 2;
			
			float vCentral = inDatas()[y*width()+x];
			if(vCentral == noDataValue()) {
				outDatas()[ind][1] = 1; // nb nodataValue
				outDatas()[ind][2] = 0; // nb value
				outDatas()[ind][3] = noDataValue(); // value
			}else if(vCentral < minHauteur){
				outDatas()[ind][1] = 0; // nb nodataValue
				outDatas()[ind][2] = 1; // nb value
				outDatas()[ind][3] = 0; // value
			}else{
				int ic;
				float v, coeff;
				float nb_nodata = 0;
				float nb_value = 0;
				double value = 0;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = inDatas()[((y + dy) * width()) + (x + dx)];
									
									if(v == noDataValue()) {
										nb_nodata += coeff;
									}else{
										nb_value += coeff;
										if(v >= minHauteur){
											value += coeff;										
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][1] = nb_nodata;
				outDatas()[ind][2] = nb_value;
				outDatas()[ind][3] = value;
			}
		}
	}

}
