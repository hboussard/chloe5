package fr.inrae.act.bagap.chloe.window.kernel.grid;

import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenDiscreteClusteringAnalysis;

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
		float[] tabCover = new float[gridSize()*gridSize()];
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
		
		TabQueenDiscreteClusteringAnalysis ca = new TabQueenDiscreteClusteringAnalysis(tabCover, gridSize(), gridSize(), values, noDataValue());
		float[] tabCluster = (float[]) ca.allRun();
		
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, tabCover, values, cellSize, noDataValue());
		cto.allRun();
		
		outDatas()[x][4] = cto.getNbPatch();
		outDatas()[x][5] = (float) cto.getTotalSurface();
		outDatas()[x][6] = (float) cto.getMaxSurface();
		outDatas()[x][7] = (float) cto.getTotalSurfaceCarre();
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+8] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+8+values.length] = (float) cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+8+2*values.length] = (float) cto.getMaxSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[x][i+8+3*values.length] = (float) cto.getTotalSurfaceCarre(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
