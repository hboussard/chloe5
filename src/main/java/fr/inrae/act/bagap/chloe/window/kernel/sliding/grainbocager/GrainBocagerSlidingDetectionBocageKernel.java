package fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.AbstractSlidingLandscapeMetricKernel;

public class GrainBocagerSlidingDetectionBocageKernel extends AbstractSlidingLandscapeMetricKernel {

	private float minHauteur; //= 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public GrainBocagerSlidingDetectionBocageKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters, float minHauteur) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		this.minHauteur = minHauteur;
	}

	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				// phase d'initialisation de la structure de donnees
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0;
				}
				
				final int mid = windowSize() / 2;
				
				float vCentral = inDatas()[y*width()+x];
				if(vCentral == noDataValue()) {
					outDatas()[ind][2] = 0; // nb value
					outDatas()[ind][3] = 1; // nb nodataValue
					outDatas()[ind][4] = noDataValue(); // sum
				}else if(vCentral < minHauteur){
					outDatas()[ind][2] = 1; // nb value
					outDatas()[ind][3] = 0; // nb nodataValue
					outDatas()[ind][4] = 0; // sum
				}else{
					int ic;
					float v, coeff;
					float nb_nodata = 0;
					float nb_value = 0;
					double sum = 0;
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									ic = ((dy+mid) * windowSize()) + (dx+mid);
									coeff = coeff()[ic];
									if(coeff > 0){
										v = inDatas()[((y + dy) * width()) + (x + dx)];
										nb_value += coeff;
										if(v == noDataValue()) {
											nb_nodata += coeff;
										}else if(v >= minHauteur){
											sum += coeff;										
										}
									}
								}
							}
						}
					}
					outDatas()[ind][2] = nb_value;
					outDatas()[ind][3] = nb_nodata;
					outDatas()[ind][4] = sum;
				}
				
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
			}
		}
	}

}
