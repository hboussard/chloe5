package fr.inrae.act.bagap.chloe.window.kernel.sliding.functional;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;

public class SlidingFunctionalCountValueKernel extends SlidingFunctionalKernel {

	private int[] mapValues;
	
	public SlidingFunctionalCountValueKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters, double cellSize, DistanceFunction function, double radius){		
		super(windowSize, displacement, null, noDataValue, unfilters, cellSize, function, radius);
		
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
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				
				float[] resistance = generateResistance(x, y, mid);
				
				float[] distance = calculateDistance(resistance);
				
				generateCoeff(distance);
				
				int ic;
				int v;
				int mv;	
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
									v = (int) inDatas()[((y + dy) * width()) + (x + dx)];
									nb += coeff;
									if(v == noDataValue()){
										nb_nodata += coeff;
									}else if(v == 0){
										nb_zero += coeff;
									}else{
										mv = mapValues[v];
										outDatas()[ind][mv+5] += coeff;	
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = nb_zero;
				
			} else{
					
				outDatas()[ind][0] = 0; // filtre pas ok 
				
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapValues = null;
	}

}
