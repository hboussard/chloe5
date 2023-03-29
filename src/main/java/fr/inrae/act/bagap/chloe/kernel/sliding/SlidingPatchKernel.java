package fr.inrae.act.bagap.chloe.kernel.sliding;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;

public class SlidingPatchKernel extends SlidingLandscapeMetricKernel {

	private final int[] values;
	
	private double cellSize;
	
	public SlidingPatchKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] values, double cellSize, int[] unfilters){		
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
		this.values = values;
		this.cellSize = cellSize;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<imageOut()[0].length; i++){
				imageOut()[ind][i] = 0f;
			}
			
			// gestion des filtres
			if(filter((int) imageIn()[(y * width()) + x])){
				final int mid = windowSize() / 2;
				int ic;
				int v;
				int[] tabCover = new int[windowSize()*windowSize()];
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								if(shape()[ic] == 1){
									v = (int) imageIn()[((y + dy) * width()) + (x + dx)];
									tabCover[(dy+mid)*windowSize() + (dx+mid)] = v;	
								}
							}
						}
					}
				}
				
				ClusteringTabQueenAnalysis ca = new ClusteringTabQueenAnalysis(tabCover, windowSize(), windowSize(), values);
				int[] tabCluster = (int[]) ca.allRun();
				
				ClusteringTabOutput cto = new ClusteringTabOutput(tabCluster, tabCover, values, cellSize);
				cto.allRun();
				
				imageOut()[ind][0] = cto.getNbPatch();
				imageOut()[ind][1] = cto.getTotalSurface();
				imageOut()[ind][2] = cto.getMaxSurface();
				
				for(int i=0; i<values.length; i++){
					imageOut()[ind][i+3] = cto.getNbPatch(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					imageOut()[ind][i+3+values.length] = cto.getTotalSurface(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					imageOut()[ind][i+3+2*values.length] = cto.getMaxSurface(values[i]);
				}
				
			}
		}
	}

}
