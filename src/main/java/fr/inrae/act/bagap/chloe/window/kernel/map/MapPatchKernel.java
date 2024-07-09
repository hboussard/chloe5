package fr.inrae.act.bagap.chloe.window.kernel.map;

import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenDiscreteClusteringAnalysis;

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
			
		TabQueenDiscreteClusteringAnalysis ca = new TabQueenDiscreteClusteringAnalysis(inDatas(), width(), height(), values, noDataValue());
		//TabRookClusteringAnalysis ca = new TabRookClusteringAnalysis(inDatas(), width(), height(), values, noDataValue());
		//int[] tabCluster = (int[]) ca.allRun();
		float[] tabCluster = (float[]) ca.allRun();
		
		TabClusteringOutput cto = new TabClusteringOutput(tabCluster, inDatas(), values, cellSize, noDataValue());
		cto.allRun();
		
		outDatas()[2] = inDatas().length;
		outDatas()[3] = ca.getNbNoDataValue();
		outDatas()[4] = cto.getNbPatch();
		outDatas()[5] = (float) cto.getTotalSurface();
		outDatas()[6] = (float) cto.getMaxSurface();
		outDatas()[7] = (float) cto.getTotalSurfaceCarre();
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+8] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+8+values.length] = (float) cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+8+2*values.length] = (float) cto.getMaxSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+8+3*values.length] = (float) cto.getTotalSurfaceCarre(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
