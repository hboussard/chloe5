package fr.inrae.act.bagap.chloe.window.kernel.selected.grainbocager;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;

public class GrainBocagerSelectedDetectionBocageKernel extends SelectedLandscapeMetricKernel {

	private float minHauteur; // = 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public GrainBocagerSelectedDetectionBocageKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue, float minHauteur) {
		super(windowSize, pixels, coeff, noDataValue);
		this.minHauteur = minHauteur;
	}

	@Override
	protected void processPixel(Pixel p, int x, int y) {
		
		outDatas().get(p)[0] = 1; // filtre ok 
			
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
		
		final int mid = windowSize() / 2;
			
		float vCentral = inDatas()[y*width()+x];
		if(vCentral == noDataValue()) {
			outDatas().get(p)[2] = 0; // nb value
			outDatas().get(p)[3] = 1; // nb nodataValue
			outDatas().get(p)[4] = noDataValue(); // sum
		}else if(vCentral < minHauteur){
			outDatas().get(p)[2] = 1; // nb value
			outDatas().get(p)[3] = 0; // nb nodataValue
			outDatas().get(p)[4] = 0; // sum
		}else{
			int ic;
			float v, coeff;
			float nb_nodata = 0;
			float nb_value = 0;
			double sum = 0;
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							coeff = coeff()[ic];
							if(coeff > 0){
								v = inDatas()[((y + dy) * width()) + (x + dx)];
								nb_value += coeff;
								if(v == noDataValue()) {
									nb_nodata += coeff;
								}else if(v >= minHauteur){
									sum += coeff;										
								}
							}
						}
					}
				}
			}
				
			outDatas().get(p)[2] = nb_value;
			outDatas().get(p)[3] = nb_nodata;
			outDatas().get(p)[4] = sum;
		}
	}

}
