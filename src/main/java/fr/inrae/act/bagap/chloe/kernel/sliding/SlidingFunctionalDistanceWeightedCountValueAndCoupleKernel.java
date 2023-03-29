package fr.inrae.act.bagap.chloe.kernel.sliding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.matrix.ArrayRCMDistanceAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class SlidingFunctionalDistanceWeightedCountValueAndCoupleKernel extends DoubleSlidingLandscapeMetricKernel {
	
	private final int nbValues;
	
	private final int[][] mapCouples;
	
	private final int[] mapValues;
	
	private double cellSize;
	
	private double radius;
	
	private DistanceFunction function;
	
	public SlidingFunctionalDistanceWeightedCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters, double cellSize, DistanceFunction function, double radius){		
		super(windowSize, displacement, null, null, noDataValue, unfilters);
		this.cellSize = cellSize;
		this.radius = radius;
		this.function = function;
		this.nbValues = values.length;
		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
		mapCouples = new int[values.length][values.length];
		int index = 0;
		for(int v : values){
			mapCouples[mapValues[v]][mapValues[v]] = index;
			index++;
		}
		
		for(int v1 : values){
			for(int v2 : values){
				if(v1 < v2) {
					mapCouples[mapValues[v1]][mapValues[v2]] = index;
					mapCouples[mapValues[v2]][mapValues[v1]] = index;
					index++;
				}
			}
		}
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<imageOut()[0].length; i++){
				imageOut()[ind][i] = 0f;
			}
			
			imageOut()[ind][2] = imageIn()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			float[] image, resistance, distance;
			
			if(filter((int) imageIn()[(y * width()) + x])){ // gestion des filtres
				
				final int mid = windowSize() / 2;
				
				image = generateImage(x, y, mid);
				
				resistance = generateResistance(x, y, mid);
				
				distance = calculateDistance(image, resistance);
				
				generateCoeff(distance);
				
				generateShape(distance);
				
				int ic, ic_V, ic_H;
				short v, v_H, v_V;
				int mv;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								if(shape()[ic] == 1) {
									v = (short) imageIn()[((y + dy) * width()) + (x + dx)];
									
									if(v == noDataValue()){
										imageOut()[ind][0] += coeff()[ic];
									}else{
										if(v == 0){
											imageOut()[ind][1] += coeff()[ic];
										}else{
											mv = mapValues[v];
											imageOut()[ind][mv+3] += coeff()[ic];
										}
									}
									
									if((dy > -mid) && ((y + dy) > 0)) {
										ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
										if(shape()[ic_V] == 1){
											v_V = (short) imageIn()[((y + dy - 1) * width()) + (x + dx)];
											
											if(v == noDataValue() || v_V == noDataValue()){
												imageOut()[ind][nbValues+3] += coeff()[ic];
											}else{
												if(v == 0 || v_V == 0){
													imageOut()[ind][nbValues+4] += coeff()[ic];
												}else{
													mv = mapCouples[mapValues[v]][mapValues[v_V]];
													imageOut()[ind][nbValues+mv+5] += coeff()[ic];
												}
											}
										}
									}
									
									if((dx > -mid) && ((x + dx) > 0)) {
										ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
										if(shape()[ic_H] == 1){
											v_H = (short) imageIn()[((y + dy) * width()) + (x + dx - 1)];
											
											if(v == noDataValue() || v_H == noDataValue()){
												imageOut()[ind][nbValues+3] += coeff()[ic];
											}else{
												if(v == 0 || v_H == 0){
													imageOut()[ind][nbValues+4] += coeff()[ic];
												}else{
													mv = mapCouples[mapValues[v]][mapValues[v_H]];
													imageOut()[ind][nbValues+mv+5] += coeff()[ic];
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void generateCoeff(float[] distance) {
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

	private void generateShape(float[] distance) {
		short[] shape = new short[windowSize()*windowSize()];
		for(int ind=0; ind<distance.length; ind++){
			if(distance[ind] <= radius){
				shape[ind] = 1;
			}else{
				shape[ind] = 0;
			}
		}
		
		setShape(shape);
	}

	private float[] generateImage(int x, int y, int mid){
		
		float[] image = new float[windowSize()*windowSize()];
		Arrays.fill(image, Raster.getNoDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						image[ic] = imageIn()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return image;
	}
	
	private float[] generateResistance(int x, int y, int mid){
		
		float[] resistance = new float[windowSize()*windowSize()];
		Arrays.fill(resistance, Raster.getNoDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						resistance[ic] = imageIn2()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return resistance;
	}
	
	private float[] calculateDistance(float[] image, float[] resistance) {
		
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


}