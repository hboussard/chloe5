package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SelectedBasicKernel extends SelectedLandscapeMetricKernel {
	
	public SelectedBasicKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, String windowsPath){
		super(windowSize, pixels, coeff, entete, windowsPath);
	}
	
	@Override
	protected void processPixel(Pixel p, int x, int y) {
			
		outDatas().get(p)[0] = 1; // filtre ok
		
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
		
		final int mid = windowSize() / 2;
		int ic;
		int v;			
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
	}

}
