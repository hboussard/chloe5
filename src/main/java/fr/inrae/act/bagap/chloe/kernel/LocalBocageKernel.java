package fr.inrae.act.bagap.chloe.kernel;

public class LocalBocageKernel extends SlidingLandscapeMetricKernel {

	private float minHauteur = 5; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public LocalBocageKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}

	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}
	
	public void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			// phase d'initialisation de la structure de données
			imageOut()[ind][0] = 0.0f; // nb nodataValue
			imageOut()[ind][1] = 0.0f; // nb value
			imageOut()[ind][2] = 0.0f; // value
			
			final int mid = windowSize() / 2;
			
			float vCentral = imageIn()[y*width()+x];
			if(vCentral == noDataValue()) {
				imageOut()[ind][0] = 1; // nb nodataValue
				imageOut()[ind][1] = 0; // nb value
				imageOut()[ind][2] = noDataValue(); // value
			}else if(vCentral < minHauteur){
				imageOut()[ind][0] = 0; // nb nodataValue
				imageOut()[ind][1] = 1; // nb value
				imageOut()[ind][2] = 0; // value
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
									v = imageIn()[((y + dy) * width()) + (x + dx)];
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
				
				imageOut()[ind][0] = nb_nodata;
				imageOut()[ind][1] = nb_value;
				imageOut()[ind][2] = value;
			}
		}
	}

}
