package fr.inrae.act.bagap.chloe.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedDistanceWeightedCountCoupleKernel extends SelectedLandscapeMetricKernel {
	
	private final int[][] mapCouples;
	
	private final int[] mapValues;
	
	public SelectedDistanceWeightedCountCoupleKernel(int windowSize, Set<Pixel> pixels, short[] shape, float[] coeff, int noDataValue, int[] values){
		super(windowSize, pixels, shape, coeff, noDataValue);
		
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
			int ic, ic_V, ic_H;
			short v, v_H, v_V;
			int mc;
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							if(shape()[ic] == 1) {
								v = (short) imageIn()[((y + dy) * width()) + (x + dx)];
								
								if((dy > -mid) && ((y + dy) > 0)) {
									ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
									if(shape()[ic_V] == 1){
										v_V = (short) imageIn()[((y + dy - 1) * width()) + (x + dx)];
										if(v == noDataValue() || v_V == noDataValue()){
											imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
										}else if(v == 0 || v_V == 0){
											imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
										}else{
											mc = mapCouples[mapValues[v]][mapValues[v_V]];
											imageOut()[ind][mc+2] = imageOut()[ind][mc+2] + coeff()[ic];
										}
									}
								}
									
								if((dx > -mid) && ((x + dx) > 0)) {
									ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
									if(shape()[ic_H] == 1){
										v_H = (short) imageIn()[((y + dy) * width()) + (x + dx - 1)];
										if(v == noDataValue() || v_H == noDataValue()){
											imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
										}else if(v == 0 || v_H == 0){
											imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
										}else{
											mc = mapCouples[mapValues[v]][mapValues[v_H]];
											imageOut()[ind][mc+2] = imageOut()[ind][mc+2] + coeff()[ic];
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
