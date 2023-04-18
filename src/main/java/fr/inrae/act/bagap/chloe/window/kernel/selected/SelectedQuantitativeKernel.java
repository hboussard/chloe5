package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedQuantitativeKernel extends SelectedLandscapeMetricKernel {
	
	private final float threshold;
	
	public SelectedQuantitativeKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue){
		this(windowSize, pixels, coeff, noDataValue, -1);
	}
		
	public SelectedQuantitativeKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue, float threshold){
		super(windowSize, pixels, coeff, noDataValue);
		this.threshold = threshold;
	}
	
	@Override
	protected void processPixel(Pixel p, int x, int y) {
		
	
		// phase d'initialisation de la structure de donnees
		for(int i=0; i<outDatas().get(p).length; i++){
			outDatas().get(p)[i] = 0.0f;
		}
			
		outDatas().get(p)[7] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
		outDatas().get(p)[0] = 1; // filtre ok
			
			
		final int mid = windowSize() / 2;
		int ic;
		float v, coeff;
		float nb_nodata = 0;
		float nb = 0;
		float sum = 0;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
					
		if(threshold != -1){
					
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							coeff = coeff()[ic];
							if(coeff > 0){
								v = inDatas()[((y + dy) * width()) + (x + dx)];
								
								if(v == noDataValue()) {
									nb_nodata += coeff;
								}else{
									nb += coeff;
									if(v > threshold){
										sum += threshold*coeff;
										square_sum += threshold*coeff * threshold*coeff;
									}else{
										sum += v*coeff;
										square_sum += v*coeff * v*coeff;
										min = Math.min(min, v*coeff);
										max = Math.max(max, v*coeff);
									}
								}
							}
						}
					}
				}
			}
		}else{
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							coeff = coeff()[ic];
							if(coeff > 0){
								v = inDatas()[((y + dy) * width()) + (x + dx)];
								
								if(v == noDataValue()) {
									nb_nodata += coeff;
								}else{
									nb += coeff;
									sum += v*coeff;
									square_sum += v*coeff * v*coeff;
									min = Math.min(min, v*coeff);
									max = Math.max(max, v*coeff);
								}
							}
						}
					}
				}
			}
		}
				
		outDatas().get(p)[1] = nb_nodata;
		outDatas().get(p)[2] = nb;
		outDatas().get(p)[3] = sum;
		outDatas().get(p)[4] = square_sum;
		outDatas().get(p)[5] = min;
		outDatas().get(p)[6] = max;
			
	}

	
}
