package fr.inrae.act.bagap.chloe.window.kernel.sliding.functional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.ArrayRCMDistanceAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.AbstractDoubleSlidingLandscapeMetricKernel;

public abstract class SlidingFunctionalKernel extends AbstractDoubleSlidingLandscapeMetricKernel {

	private double cellSize;
	
	private double radius;
	
	private DistanceFunction function;
	
	protected SlidingFunctionalKernel(int windowSize, int displacement, float[] coeff, int noDataValue,	int[] unfilters, double cellSize, DistanceFunction function, double radius){	
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		this.cellSize = cellSize;
		this.radius = radius;
		this.function = function;
	}

	protected void generateCoeff(float[] distance) {
		float[] coeff = new float[windowSize()*windowSize()];
		for(int ind=0; ind<distance.length; ind++){
			if(distance[ind] <= radius){
				coeff[ind] = (float) function.interprete(distance[ind]);
			}else{
				coeff[ind] = 0;
			}
		}
		
		setCoeff(coeff);
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
	
	protected float[] calculateDistance(float[] image, float[] resistance) {
		
		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Float, Set<Pixel>> waits = new TreeMap<Float, Set<Pixel>>();
		waits.put(0f, new HashSet<Pixel>());
		
		float[] distance = new float[windowSize()*windowSize()];
		for(int ind=0; ind<image.length; ind++){
			if(image[ind] == Raster.getNoDataValue()){
				distance[ind] = -1;
			}else if(ind == ((windowSize()*windowSize())-1)/2){ // pixel central source de diffusion
				distance[ind] = 0;
				waits.get(0f).add(new Pixel((windowSize()-1)/2, (windowSize()-1)/2));
			}else{
				distance[ind] = -2; // to be computed
			}
		}
		
		ArrayRCMDistanceAnalysis rcm = new ArrayRCMDistanceAnalysis(distance, resistance, windowSize(), windowSize(), (float) cellSize, waits);
		rcm.allRun();
		
		return distance;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		function = null;
	}

}
