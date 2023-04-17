package fr.inrae.act.bagap.chloe.kernel.selected.grainbocager;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.kernel.selected.DoubleSelectedLandscapeMetricKernel;

public class GrainBocagerSelectedDistanceBocageKernel extends DoubleSelectedLandscapeMetricKernel{
	
	private float cellSize = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur = 3; // 3 metres minimum 
	
	private float seuilMax = 30; // 30 metres maximum
	
	public GrainBocagerSelectedDistanceBocageKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue) {
		super(windowSize, pixels, coeff, noDataValue);
	}

	@Override
	protected void processPixel(Pixel p, int x, int y) {
				
	
		outDatas().get(p)[0] = 1; // filtre ok
			
		// phase d'initialisation de la structure de donnees
		outDatas().get(p)[1] = 0.0f; // nodataValue
		outDatas().get(p)[2] = 0.0f; // value
		outDatas().get(p)[3] = 1.0f; // max
		outDatas().get(p)[4] = 0.0f; // prop
			
		final int mid = windowSize() / 2;
		int ic;
		float v, coeff;
		float nb_nodata = 0;
		float nb = 0;
		float prop = 0;
		double min = 1.0;
		double r, R;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						coeff = coeff()[ic];
						if(coeff > 0){
							v = inDatas()[((y + dy) * width()) + (x + dx)];
							
							if(v == noDataValue()) {
								nb_nodata = nb_nodata + coeff;
							}else{
								nb = nb + coeff;
								if(v >= minHauteur){
										
									if(v > seuilMax){
										v = seuilMax;
									}
										
									r = cellSize*Math.sqrt((dx*dx)+(dy*dy));
									R = v * inDatas2()[((y + dy) * width()) + (x + dx)];	
									
									if(r < R){
										min = Math.min(min, r/R);
									}
								}
							}
						}
					}
				}
			}
		}
			
		outDatas().get(p)[1] = nb_nodata;
		outDatas().get(p)[2] = nb;
		outDatas().get(p)[3] = min;
		outDatas().get(p)[4] = prop;
			
	}

}
