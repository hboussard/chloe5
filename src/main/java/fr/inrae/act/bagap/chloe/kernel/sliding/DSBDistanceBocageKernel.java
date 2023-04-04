package fr.inrae.act.bagap.chloe.kernel.sliding;

public class DSBDistanceBocageKernel extends DoubleSlidingLandscapeMetricKernel {

	private float cellSize = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur = 3; // 3 metres minimum 
	
	private float seuilMax = 30; // 30 metres maximum
	
	public DSBDistanceBocageKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}

	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			// phase d'initialisation de la structure de donnees
			outDatas()[ind][0] = 0.0f; // nodataValue
			outDatas()[ind][1] = 0.0f; // value
			outDatas()[ind][2] = 1.0f; // max
			outDatas()[ind][3] = 0.0f; // prop
			
			final int mid = windowSize() / 2;
			int ic;
			float v, c;
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
							if(shape()[ic] == 1){
								v = inDatas()[((y + dy) * width()) + (x + dx)];
								c = coeff()[ic];
								if(v == noDataValue()) {
									nb_nodata = nb_nodata + c;
								}else{
									nb = nb + c;
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
			
			outDatas()[ind][0] = nb_nodata;
			outDatas()[ind][1] = nb;
			outDatas()[ind][2] = min;
			outDatas()[ind][3] = prop;
			
			
			//System.out.println(nb_nodata+" "+nb+" "+sum+" "+square_sum+" "+min+" "+max);
		}
	}

}
