package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SelectedCountCoupleKernel extends SelectedLandscapeMetricKernel {
	
	private int[][] mapCouples;
	
	private int[] mapValues;
	
	public SelectedCountCoupleKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, int[] values, String windowsPath){
		super(windowSize, pixels, coeff, entete, windowsPath);
		
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
	protected void processPixel(Pixel p, int x, int y) {
		
		outDatas().get(p)[0] = 1; // filtre ok
		
		outDatas().get(p)[1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
		final int mid = windowSize() / 2;
		int ic, ic_V, ic_H;
		short v, v_H, v_V;
		int mc;
		float coeff;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						coeff = coeff()[ic];
						if(coeff > 0){
							v = (short) inDatas()[((y + dy) * width()) + (x + dx)];
							outDatas().get(p)[2] += coeff;
							if(v == noDataValue()){
								outDatas().get(p)[3] += coeff;
							}
							if((dy > -mid) && ((y + dy) > 0)) {
								ic_V = ((dy+mid-1) * windowSize()) + (dx+mid);
								if(coeff()[ic_V] > 0){
									v_V = (short) inDatas()[((y + dy - 1) * width()) + (x + dx)];
									outDatas().get(p)[4] += coeff;
									if(v == noDataValue() || v_V == noDataValue()){
										outDatas().get(p)[5] += coeff;
									}else if(v == 0 || v_V == 0){
										outDatas().get(p)[6] += coeff;
									}else{
										mc = mapCouples[mapValues[v]][mapValues[v_V]];
										outDatas().get(p)[mc+7] += coeff;
									}
								}
							}
									
							if((dx > -mid) && ((x + dx) > 0)) {
								ic_H = ((dy+mid) * windowSize()) + (dx+mid-1);
								if(coeff()[ic_H] > 0){
									v_H = (short) inDatas()[((y + dy) * width()) + (x + dx - 1)];
									outDatas().get(p)[4] += coeff;
									if(v == noDataValue() || v_H == noDataValue()){
										outDatas().get(p)[5] += coeff;
									}else if(v == 0 || v_H == 0){
										outDatas().get(p)[6] += coeff;
									}else{
										mc = mapCouples[mapValues[v]][mapValues[v_H]];
										outDatas().get(p)[mc+7] += coeff;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapCouples = null;
		mapValues = null;
	}
	
}
