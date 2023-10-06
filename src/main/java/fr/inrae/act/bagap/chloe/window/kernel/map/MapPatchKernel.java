package fr.inrae.act.bagap.chloe.window.kernel.map;

import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;

public class MapPatchKernel extends MapLandscapeMetricKernel {

	private int[] values;
	
	private double cellSize;
	
	public MapPatchKernel(int noDataValue, int[] values, double cellSize){		
		super(noDataValue);
		this.values = values;
		this.cellSize = cellSize;
	}
	
	@Override
	public void applyMapWindow(int theY) {
			
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(inDatas(), width(), height(), values, noDataValue());
		//TabRookClusteringAnalysis ca = new TabRookClusteringAnalysis(inDatas(), width(), height(), values, noDataValue());
		//int[] tabCluster = (int[]) ca.allRun();
		float[] tabCluster = (float[]) ca.allRun();
		
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, inDatas(), values, cellSize, noDataValue());
		cto.allRun();
		
		outDatas()[2] = inDatas().length;
		outDatas()[3] = ca.getNbNoDataValue();
		outDatas()[4] = cto.getNbPatch();
		outDatas()[5] = cto.getTotalSurface();
		outDatas()[6] = cto.getMaxSurface();
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+7] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+7+values.length] = cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+7+2*values.length] = cto.getMaxSurface(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
