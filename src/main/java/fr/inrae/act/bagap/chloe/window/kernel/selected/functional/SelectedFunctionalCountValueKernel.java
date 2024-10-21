package fr.inrae.act.bagap.chloe.window.kernel.selected.functional;

import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.distance.DistanceFunction;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class SelectedFunctionalCountValueKernel extends SelectedFunctionalKernel {

	private int[] mapValues;
	
	public SelectedFunctionalCountValueKernel(int windowSize, Set<Pixel> pixels, EnteteRaster entete, int[] values, DistanceFunction function, double radius, String windowsPath){		
		super(windowSize, pixels, null, entete, function, radius, windowsPath);
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
						
		float[] resistance = generateResistance(x, y, mid);
				
		float[] distance = calculateDistance(resistance);
				
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
