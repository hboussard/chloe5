package fr.inrae.act.bagap.chloe.kernel.sliding.functional;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;

public class SlidingFunctionalCountValueAndCoupleKernel extends SlidingFunctionalKernel {
	
	private int nbValues;
	
	private int[][] mapCouples;
	
	private int[] mapValues;
	
	public SlidingFunctionalCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters, double cellSize, DistanceFunction function, double radius){		
		super(windowSize, displacement, null, noDataValue, unfilters, cellSize, function, radius);
	
		this.nbValues = values.length;
		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
		mapCouples = new int[values.length][values.length];
		int index = 0;
		for(int v : values){
			mapCouples[mapValues[v]][mapValues[v]] = index;
			index++;
		}
		
		for(int v1 : values){
			for(int v2 : values){
				if(v1 < v2) {
					mapCouples[mapValues[v1]][mapValues[v2]] = index;
					mapCouples[mapValues[v2]][mapValues[v1]] = index;
					index++;
				}
			}
		}
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0f;
			}
			
			outDatas()[ind][3] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			if(filter((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				final int mid = windowSize() / 2;
				
				float[] image = generateImage(x, y, mid);
				
				float[] resistance = generateResistance(x, y, mid);
				
				float[] distance = calculateDistance(image, resistance);
				
				generateCoeff(distance);
				
				int ic, ic_V, ic_H;
				short v, v_H, v_V;
				int mv;
				float coeff;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (short) inDatas()[((y + dy) * width()) + (x + dx)];
									
									if(v == noDataValue()){
										outDatas()[ind][1] += coeff;
									}else if(v == 0){
										outDatas()[ind][2] += coeff;
									}else{
										mv = mapValues[v];
										outDatas()[ind][mv+4] += coeff;
									}
									
									if((dy > -mid) && ((y + dy) > 0)) {
										ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
										if(coeff()[ic_V] > 0){
											v_V = (short) inDatas()[((y + dy - 1) * width()) + (x + dx)];
											
											if(v == noDataValue() || v_V == noDataValue()){
												outDatas()[ind][nbValues+4] += coeff;
											}else if(v == 0 || v_V == 0){
												outDatas()[ind][nbValues+5] += coeff;
											}else{
												mv = mapCouples[mapValues[v]][mapValues[v_V]];
												outDatas()[ind][nbValues+mv+6] += coeff;
											}
										}
									}
									
									if((dx > -mid) && ((x + dx) > 0)) {
										ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
										if(coeff()[ic_H] > 0){
											v_H = (short) inDatas()[((y + dy) * width()) + (x + dx - 1)];
											
											if(v == noDataValue() || v_H == noDataValue()){
												outDatas()[ind][nbValues+4] += coeff;
											}else if(v == 0 || v_H == 0){
												outDatas()[ind][nbValues+5] += coeff;
											}else{
												mv = mapCouples[mapValues[v]][mapValues[v_H]];
												outDatas()[ind][nbValues+mv+6] += coeff;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
		mapValues = null;
	}
	
}
