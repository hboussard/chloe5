package fr.inrae.act.bagap.chloe.kernel.grid;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;

public class GridPatchKernel extends GridLandscapeMetricKernel {

	private int[] values;
	
	private double cellSize;
	
	public GridPatchKernel(int gridSize, int noDataValue, int[] values, double cellSize){		
		super(gridSize, noDataValue);
		this.values = values;
		this.cellSize = cellSize;
	}
	
	@Override
	protected void processGrid(int x, int theY) {
		
		for(int i=0; i<outDatas()[0].length; i++){
			outDatas()[x][i] = 0f;
		}
		
		outDatas()[x][0] = 1; // filtre ok
			
		int v;	
		int[] tabCover = new int[gridSize()*gridSize()];
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (int) inDatas()[((theY+y)*width()) + (x*gridSize() + lx)];		
						
						tabCover[y*gridSize() + lx] = v;
					}
				}
			}
		}
		
		ClusteringTabQueenAnalysis ca = new ClusteringTabQueenAnalysis(tabCover, gridSize(), gridSize(), values);
		int[] tabCluster = (int[]) ca.allRun();
		
		ClusteringTabOutput cto = new ClusteringTabOutput(tabCluster, tabCover, values, cellSize);
		cto.allRun();
		
		outDatas()[x][1] = cto.getNbPatch();
		outDatas()[x][2] = cto.getTotalSurface();
		outDatas()[x][3] = cto.getMaxSurface();
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+4] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+4+values.length] = cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+4+2*values.length] = cto.getMaxSurface(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
