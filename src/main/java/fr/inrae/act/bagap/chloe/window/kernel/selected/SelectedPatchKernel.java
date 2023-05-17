package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;

public class SelectedPatchKernel extends SelectedLandscapeMetricKernel {

	private int[] values;
	
	private double cellSize;
	
	public SelectedPatchKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue, int[] values, double cellSize){		
		super(windowSize, pixels, coeff, noDataValue);
		this.values = values;
		this.cellSize = cellSize;
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
		int[] tabCover = new int[windowSize()*windowSize()];
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
			
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(tabCover, windowSize(), windowSize(), values, noDataValue());
		int[] tabCluster = (int[]) ca.allRun();
			
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, tabCover, values, cellSize);
		cto.allRun();
			
		outDatas().get(p)[4] = cto.getNbPatch();
		outDatas().get(p)[5] = cto.getTotalSurface();
		outDatas().get(p)[6] = cto.getMaxSurface();
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+7] = cto.getNbPatch(values[i]);
		}
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+7+values.length] = cto.getTotalSurface(values[i]);
		}
			
		for(int i=0; i<values.length; i++){
			outDatas().get(p)[i+7+2*values.length] = cto.getMaxSurface(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}