package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenDiscreteClusteringAnalysis;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class SelectedPatchKernel extends SelectedLandscapeMetricKernel {

	private int[] values;
	
	public SelectedPatchKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, int[] values, String windowsPath){		
		super(windowSize, pixels, coeff, entete, windowsPath);
		this.values = values;
	}
	
	@Override
	protected void processPixel(Pixel p, int x, int y) {
			
		outDatas().get(p)[0] = 1; // filtre ok
		
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
		
		for(int i=2; i<outDatas().get(p).length; i++){
			outDatas().get(p)[i] = 0f;
		}
		
		final int mid = windowSize() / 2;
		int ic;
		int v;
		float coeff;
		float[] tabCover = new float[windowSize()*windowSize()];
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
							}
							tabCover[(dy+mid)*windowSize() + (dx+mid)] = v;	
						}
					}
				}
			}
		}
			
		TabQueenDiscreteClusteringAnalysis ca = new TabQueenDiscreteClusteringAnalysis(tabCover, windowSize(), windowSize(), values, noDataValue());
		float[] tabCluster = (float[]) ca.allRun();
			
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, tabCover, values, cellSize(), noDataValue());
		cto.allRun();
			
		outDatas().get(p)[4] = cto.getNbPatch();
		outDatas().get(p)[5] = (float) cto.getTotalSurface();
		outDatas().get(p)[6] = (float) cto.getMaxSurface();
		outDatas().get(p)[7] = (float) cto.getTotalSurfaceCarre();
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+8] = cto.getNbPatch(values[i]);
		}
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+8+values.length] = (float) cto.getTotalSurface(values[i]);
		}
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+8+2*values.length] = (float) cto.getMaxSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+8+3*values.length] = (float) cto.getTotalSurfaceCarre(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}