package fr.inrae.act.bagap.chloe.window.kernel.sliding;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;

public class SlidingPatchKernel extends AbstractSlidingLandscapeMetricKernel {

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
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				int ic;
				int v;
				float coeff;
				float nb = 0;
				float nb_nodata = 0;
				int[] tabCover = new int[windowSize()*windowSize()];
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (int) inDatas()[((y + dy) * width()) + (x + dx)];
									//outDatas()[ind][2] += coeff;
									nb += coeff;
									if(v == noDataValue()){
										//outDatas()[ind][3] += coeff;
										nb_nodata += coeff;
									}
									tabCover[(dy+mid)*windowSize() + (dx+mid)] = v;	
								}
							}
						}
					}
				}
				
				ClusteringTabQueenAnalysis ca = new ClusteringTabQueenAnalysis(tabCover, windowSize(), windowSize(), values, noDataValue());
				int[] tabCluster = (int[]) ca.allRun();
				
				ClusteringTabOutput cto = new ClusteringTabOutput(tabCluster, tabCover, values, cellSize);
				cto.allRun();
				
				outDatas()[ind][2] = nb;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = cto.getNbPatch();
				outDatas()[ind][5] = cto.getTotalSurface();
				outDatas()[ind][6] = cto.getMaxSurface();
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+7] = cto.getNbPatch(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+7+values.length] = cto.getTotalSurface(values[i]);
				}
				
				for(int i=0; i<values.length; i++){
					outDatas()[ind][i+7+2*values.length] = cto.getMaxSurface(values[i]);
				}
				
			}else{
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
