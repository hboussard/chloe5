package fr.inrae.act.bagap.chloe.kernel;

import java.util.HashMap;
import java.util.Map;

import com.aparapi.Kernel;

import fr.inrae.act.bagap.chloe.util.Couple;

public class DistanceWeightedCountCoupleKernelOld extends SlidingLandscapeMetricKernel {
	
	private final float[] couples;
	
	private final Map<Float, Integer> mapCouples;
	
	public DistanceWeightedCountCoupleKernelOld(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, float[] couples){
		super(windowSize, displacement, shape, coeff, noDataValue);
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.couples = couples;
		mapCouples = new HashMap<Float, Integer>();
		for(int i=0; i<couples.length; i++){
			mapCouples.put(couples[i], i);
		}
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}

	public void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			for(int i=0; i<couples.length+2; i++){
				imageOut()[ind][i] = 0f;
			}
			
			//if(imageIn[(y * width) + x] != -1f) {
				
					
			final int mid = windowSize() / 2;
			int ic, ic_V, ic_H;
			short v, v_H, v_V;
			float c;
			boolean again;
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
										c = Couple.getCouple(v, v_V);
										if(c == noDataValue()){
											imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
										}else{
											if(c == 0){
												imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
											}else{
												/*
												mc = mapCouples.get(c);
												imageOut()[ind][mc+2] = imageOut()[ind][mc+2] + coeff()[ic];
												*/
												
												again = true;
												for(int i=0; again && i<couples.length; i++){
													if(c == couples[i]){
														imageOut()[ind][i+2] = imageOut()[ind][i+2] + coeff()[ic];
														again = false;
													}
												}
												
											}
										}
									}
								}
								
								if((dx > -mid) && ((x + dx) > 0)) {
									ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
									if(shape()[ic_H] == 1){
										v_H = (short) imageIn()[((y + dy) * width()) + (x + dx - 1)];
										c = Couple.getCouple(v, v_H);
										if(c == noDataValue()){
											imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
										}else{
											if(c == 0){
												imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
											}else{
												/*
												mc = mapCouples.get(c);
												imageOut()[ind][mc+2] = imageOut()[ind][mc+2] + coeff()[ic];
												*/
												
												again = true;
												for(int i=0; again && i<couples.length; i++){
													if(c == couples[i]){
														imageOut()[ind][i+2] = imageOut()[ind][i+2] + coeff()[ic];
														again = false;
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
	}

}
