package fr.inrae.act.bagap.chloe.kernel.sliding;

public class DSBDetectionBocageKernel extends SlidingLandscapeMetricKernel {

	private float minHauteur = 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public DSBDetectionBocageKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}

	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			// phase d'initialisation de la structure de donnees
			outDatas()[ind][0] = 0.0f; // nb nodataValue
			outDatas()[ind][1] = 0.0f; // nb value
			outDatas()[ind][2] = 0.0f; // value
			
			final int mid = windowSize() / 2;
			
			float vCentral = inDatas()[y*width()+x];
			if(vCentral == noDataValue()) {
				outDatas()[ind][0] = 1; // nb nodataValue
				outDatas()[ind][1] = 0; // nb value
				outDatas()[ind][2] = noDataValue(); // value
			}else if(vCentral < minHauteur){
				outDatas()[ind][0] = 0; // nb nodataValue
				outDatas()[ind][1] = 1; // nb value
				outDatas()[ind][2] = 0; // value
			}else{
				int ic;
				float v, c;
				float nb_nodata = 0;
				float nb_value = 0;
				double value = 0;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								if(shape()[ic] == 1){
									v = inDatas()[((y + dy) * width()) + (x + dx)];
									c = coeff()[ic];
									if(v == noDataValue()) {
										nb_nodata += c;
									}else{
										nb_value += c;
										if(v >= minHauteur){
											value += c;										
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][0] = nb_nodata;
				outDatas()[ind][1] = nb_value;
				outDatas()[ind][2] = value;
			}
		}
	}

}
