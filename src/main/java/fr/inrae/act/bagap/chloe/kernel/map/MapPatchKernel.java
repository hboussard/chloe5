package fr.inrae.act.bagap.chloe.kernel.map;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;

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
			
		ClusteringTabQueenAnalysis ca = new ClusteringTabQueenAnalysis(inDatas(), width(), height(), values);
		int[] tabCluster = (int[]) ca.allRun();
		
		ClusteringTabOutput cto = new ClusteringTabOutput(tabCluster, inDatas(), values, cellSize);
		cto.allRun();
		
		outDatas()[0] = cto.getNbPatch();
		outDatas()[1] = cto.getTotalSurface();
		outDatas()[2] = cto.getMaxSurface();
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+3] = cto.getNbPatch(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+3+values.length] = cto.getTotalSurface(values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			outDatas()[i+3+2*values.length] = cto.getMaxSurface(values[i]);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		values = null;
	}

}
