package fr.inrae.act.bagap.chloe.window.kernel.selected.grainbocager;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class GrainBocagerSelectedDistanceBocageKernel extends SelectedLandscapeMetricKernel{
	
	//private float cellSize; // = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur; // = 3; // 3 metres minimum 
	
	private float seuilMax; // = 30; // 30 metres maximum
	
	public GrainBocagerSelectedDistanceBocageKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, String windowsPath, float minHauteur, float seuilMax) {
		super(windowSize, pixels, coeff, entete, windowsPath);
		this.minHauteur = minHauteur;
		this.seuilMax = seuilMax;
	}

	@Override
	protected void processPixel(Pixel p, int x, int y) {	
	
		outDatas().get(p)[0] = 1; // filtre ok
		
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
		final int mid = windowSize() / 2;
		int ic;
		float v, coeff;
		float nb_nodata = 0;
		float nb = 0;
		float min = 1.0f;
		double r, R;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						coeff = coeff()[ic];
						if(coeff > 0){
							v = inDatas()[((y + dy) * width()) + (x + dx)];
							nb += coeff;
							if(v == noDataValue()) {
								nb_nodata += coeff;
							}else{
								if(v >= minHauteur){
									if(v > seuilMax){
										v = seuilMax;
									}
									r = cellSize()*Math.sqrt((dx*dx)+(dy*dy));
									R = v * inDatas(2)[((y + dy) * width()) + (x + dx)];
									if(r < R){
										min = (float) Math.min(min, r/R);
									}
								}
							}
						}
					}
				}
			}
		}
			
		outDatas().get(p)[2] = nb;
		outDatas().get(p)[3] = nb_nodata;
		outDatas().get(p)[6] = min;
			
	}

}
