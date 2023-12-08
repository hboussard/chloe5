package fr.inrae.act.bagap.chloe.window.kernel.selected.functional;

import java.util.Arrays;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.selected.DoubleSelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.EnteteRaster;

public abstract class SelectedFunctionalKernel extends DoubleSelectedLandscapeMetricKernel {
	
	private double cellSize;
	
	private double radius;
	
	private DistanceFunction function;
	
	protected SelectedFunctionalKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, DistanceFunction function, double radius, String windowsPath){	
		super(windowSize, pixels, coeff, entete, windowsPath);
		this.cellSize = entete.cellsize();
		this.radius = radius;
		this.function = function;
	}

	protected double radius(){
		return radius;
	}

	protected float[] generateImage(int x, int y, int mid){
		
		float[] image = new float[windowSize()*windowSize()];
		Arrays.fill(image, Raster.getNoDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						image[ic] = inDatas()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return image;
	}
	
	protected float[] generateResistance(int x, int y, int mid){
		
		float[] resistance = new float[windowSize()*windowSize()];
		Arrays.fill(resistance, Raster.getNoDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						resistance[ic] = inDatas2()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return resistance;
	}
	
	protected float[] calculateDistance(float[] resistance) {
		
		float[] distance = new float[windowSize()*windowSize()];
		
		TabRCMDistanceAnalysis rcm = new TabRCMDistanceAnalysis(distance, resistance, windowSize(), windowSize(), (float) cellSize, noDataValue(), radius);
		rcm.allRun();
		
		return distance;
	}
	
	protected void generateCoeff(float[] distance) {
		float[] coeff = new float[windowSize()*windowSize()];
		for(int ind=0; ind<distance.length; ind++){
			float dist = distance[ind];
			if(dist >= 0 && dist <= radius){
				if(function == null){
					coeff[ind] = 1;
				}else{
					coeff[ind] = (float) function.interprete(distance[ind]);
				}
			}else{
				coeff[ind] = 0;
			}
		}
		
		setCoeff(coeff);
	}

	
	@Override
	public void dispose(){
		super.dispose();
		function = null;
	}

}
