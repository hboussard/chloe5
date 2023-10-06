package fr.inrae.act.bagap.chloe.window.kernel.sliding.slope;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.AbstractSlidingLandscapeMetricKernel;

public class SlidingSlopeKernel extends AbstractSlidingLandscapeMetricKernel {
	
	private double cellSize;
	
	public SlidingSlopeKernel(int noDataValue, double cellSize){
		super(3, 1, null, noDataValue, null);
		this.cellSize = cellSize;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				float vc = (float) outDatas()[ind][1]; 
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0;
				} 
				
				if(vc == noDataValue()){
					outDatas()[ind][0] = 0; // filtre pas ok 
				}else{
					
					final int mid = 1;
					float v;
					float slopeDirection = 0;
					double slopeIntensity = 90;
					double min = 90;
					
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									v = inDatas()[((y + dy) * width()) + (x + dx)];
									
									if(v != noDataValue()) {
										
										if(v < vc){
											double diffHauteur = Math.abs(vc - v);
											double diffDistance = dy==0||dx==0?cellSize:cellSize*Math.sqrt(2);
											
											double tangente = diffDistance/diffHauteur;
											slopeIntensity = Math.toDegrees(Math.atan(tangente));
											
											if(slopeIntensity < min){
											
												min = slopeIntensity;
												slopeDirection = (dx+2) + (dy+1)*3;
												if(slopeDirection > 4){
													slopeDirection--;
												}
											}
										}
									}
								}
							}
						}
					}
					
					outDatas()[ind][4] = slopeDirection;
					outDatas()[ind][5] = slopeIntensity;
					
				}
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
			}
		}
	}

	
}