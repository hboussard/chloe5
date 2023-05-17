package fr.inrae.act.bagap.chloe.window.kernel.grid;

import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;

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
		
		outDatas()[x][0] = 1; // filtre ok
		
		for(int i=1; i<outDatas()[0].length; i++){
			outDatas()[x][i] = 0f;
		}
			
		int v;	
		int[] tabCover = new int[gridSize()*gridSize()];
		for(int y=0; y<gridSize(); y++) {
			if((theY+y) < height()){
				for(int lx=0; lx<gridSize(); lx++) {
					if((x*gridSize() + lx) < width()){
						
						v = (int) inDatas()[((theY+y)*width()) + (x*gridSize() + lx)];		
						outDatas()[x][2] += 1;
						if(v == noDataValue()){
							outDatas()[x][3] += 1;
						}
						tabCover[y*gridSize() + lx] = v;
					}
				}
			}
		}
		
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(tabCover, gridSize(), gridSize(), values, noDataValue());
		int[] tabCluster = (int[]) ca.allRun();
		
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, tabCover, values, cellSize);
		cto.allRun();
		
		outDatas()[x][4] = cto.getNbPatch();
		outDatas()[x][5] = cto.getTotalSurface();
		outDatas()[x][6] = cto.getMaxSurface();
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+7] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+7+values.length] = cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+7+2*values.length] = cto.getMaxSurface(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
