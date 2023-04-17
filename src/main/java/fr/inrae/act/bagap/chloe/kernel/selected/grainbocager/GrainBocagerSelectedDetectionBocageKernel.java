package fr.inrae.act.bagap.chloe.kernel.selected.grainbocager;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;

public class GrainBocagerSelectedDetectionBocageKernel extends SelectedLandscapeMetricKernel {

	private float minHauteur = 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	public GrainBocagerSelectedDetectionBocageKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue) {
		super(windowSize, pixels, coeff, noDataValue);
	}

	@Override
	protected void processPixel(Pixel p, int x, int y) {
		
		outDatas().get(p)[0] = 1; // filtre ok 
			
		// phase d'initialisation de la structure de donnees
		outDatas().get(p)[1] = 0.0f; // nb nodataValue
		outDatas().get(p)[2] = 0.0f; // nb value
		outDatas().get(p)[3] = 0.0f; // value
			
		final int mid = windowSize() / 2;
			
		float vCentral = inDatas()[y*width()+x];
		if(vCentral == noDataValue()) {
			outDatas().get(p)[1] = 1; // nb nodataValue
			outDatas().get(p)[2] = 0; // nb value
			outDatas().get(p)[3] = noDataValue(); // value
		}else if(vCentral < minHauteur){
			outDatas().get(p)[1] = 0; // nb nodataValue
			outDatas().get(p)[2] = 1; // nb value
			outDatas().get(p)[3] = 0; // value
		}else{
			int ic;
			float v, coeff;
			float nb_nodata = 0;
			float nb_value = 0;
			double value = 0;
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
									nb_value += coeff;
									if(v >= minHauteur){
										value += coeff;										
									}
								}
							}
						}
					}
				}
			}
				
			outDatas().get(p)[1] = nb_nodata;
			outDatas().get(p)[2] = nb_value;
			outDatas().get(p)[3] = value;
		}
	}

}
