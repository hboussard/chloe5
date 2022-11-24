package fr.inrae.act.bagap.chloe.kernel;

import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringTabQueenAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedPatchKernel extends SelectedLandscapeMetricKernel {

	private final int[] values;
	
	private double cellSize;
	
	@SuppressWarnings("deprecation")
	public SelectedPatchKernel(int windowSize, Set<Pixel> pixels, short[] shape, float[] coeff, int noDataValue, int[] values, double cellSize){		
		super(windowSize, pixels, shape, coeff, noDataValue);
		this.values = values;
		this.cellSize = cellSize;
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}

	public void processPixel(int x, int y, int localY) {
		
		Pixel p = new Pixel(x, y);
		if(pixels().contains(p)){
			
			//System.out.println(p);
			
			int ind = ((localY-bufferROIYMin())*(((width() - bufferROIXMin() - bufferROIXMax())-1)+1) + (x-bufferROIXMin()));
			
			for(int i=0; i<imageOut()[0].length; i++){
				imageOut()[ind][i] = 0f;
			}
			
			
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
			//ClusteringTabRookAnalysis ca = new ClusteringTabRookAnalysis(tabCover, windowSize(), windowSize(), values); 
			int[] tabCluster = (int[]) ca.allRun();
			
			ClusteringTabOutput co = new ClusteringTabOutput(tabCluster, cellSize);
			co.allRun();
			imageOut()[ind][0] = co.getNbPatch();
			imageOut()[ind][1] = co.getTotalSurface();
		}
	}

}