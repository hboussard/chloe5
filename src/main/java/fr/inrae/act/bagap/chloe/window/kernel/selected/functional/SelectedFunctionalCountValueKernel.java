package fr.inrae.act.bagap.chloe.window.kernel.selected.functional;

import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedFunctionalCountValueKernel extends SelectedFunctionalKernel {

	private int[] mapValues;
	
	public SelectedFunctionalCountValueKernel(int windowSize, Set<Pixel> pixels, int noDataValue, int[] values, double cellSize, DistanceFunction function, double radius){		
		super(windowSize, pixels, null, noDataValue, cellSize, function, radius);
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
		
		for(int i=0; i<outDatas().get(p).length; i++){
			outDatas().get(p)[i] = 0f;
		}
		
		outDatas().get(p)[0] = 1; // filtre ok
			
		outDatas().get(p)[3] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
		final int mid = windowSize() / 2;
				
		float[] image = generateImage(x, y, mid);
				
		float[] resistance = generateResistance(x, y, mid);
				
		float[] distance = calculateDistance(image, resistance);
				
		generateCoeff(distance);
				
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
							if(v == noDataValue()){
								outDatas().get(p)[1] += coeff;
							}else if(v == 0){
								outDatas().get(p)[2] += coeff;
							}else{
								mv = mapValues[v];
								outDatas().get(p)[mv+4] += coeff;	
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
