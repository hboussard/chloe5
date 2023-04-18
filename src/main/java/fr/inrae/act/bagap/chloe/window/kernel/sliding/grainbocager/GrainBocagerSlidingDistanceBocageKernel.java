package fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.DoubleSlidingLandscapeMetricKernel;

public class GrainBocagerSlidingDistanceBocageKernel extends DoubleSlidingLandscapeMetricKernel {

	private float cellSize = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur = 3; // 3 metres minimum 
	
	private float seuilMax = 30; // 30 metres maximum
	
	public GrainBocagerSlidingDistanceBocageKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
	}

	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			// phase d'initialisation de la structure de donnees
			outDatas()[ind][1] = 0.0f; // nodataValue
			outDatas()[ind][2] = 0.0f; // value
			outDatas()[ind][3] = 1.0f; // max
			outDatas()[ind][4] = 0.0f; // prop
			
			final int mid = windowSize() / 2;
			int ic;
			float v, coeff;
			float nb_nodata = 0;
			float nb = 0;
			float prop = 0;
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
								
								if(v == noDataValue()) {
									nb_nodata = nb_nodata + coeff;
								}else{
									nb = nb + coeff;
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
			
			outDatas()[ind][1] = nb_nodata;
			outDatas()[ind][2] = nb;
			outDatas()[ind][3] = min;
			outDatas()[ind][4] = prop;
			
		}
	}

}
