package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedCountValueKernel extends SelectedLandscapeMetricKernel {

	private int[] mapValues;
	
	public SelectedCountValueKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue, int[] values){		
		super(windowSize, pixels, coeff, noDataValue);
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
	protected void processPixel(Pixel p, int x, int y) {
			
		outDatas().get(p)[0] = 1; // filtre ok
		
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
		
		final int mid = windowSize() / 2;
		int ic;
		int v;
		int mv;			
		float coeff;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						coeff = coeff()[ic];
						if(coeff > 0){
							v = (int) inDatas()[((y + dy) * width()) + (x + dx)];
							outDatas().get(p)[2] += coeff;
							if(v == noDataValue()){
								outDatas().get(p)[3] += coeff;
							}else if(v == 0){
								outDatas().get(p)[4] += coeff;
							}else{
								mv = mapValues[v];
								outDatas().get(p)[mv+5] += coeff;	
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
		mapValues = null;
	}

}
