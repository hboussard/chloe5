package fr.inrae.act.bagap.chloe.kernel.sliding;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;

public class SlidingPatchKernel extends SlidingLandscapeMetricKernel {

	private int[] values;
	
	private double cellSize;
	
	public SlidingPatchKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] values, double cellSize, int[] unfilters){		
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		this.values = values;
		this.cellSize = cellSize;
		//this.cellSize = 100.0; // pour rapporter en nombre de pixels
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0f;
			}
			
			if(filter((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
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
				
				outDatas()[ind][1] = cto.getNbPatch();
				outDatas()[ind][2] = cto.getTotalSurface();
				outDatas()[ind][3] = cto.getMaxSurface();
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+4] = cto.getNbPatch(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+4+values.length] = cto.getTotalSurface(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+4+2*values.length] = cto.getMaxSurface(values[i]);
				}
				
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
