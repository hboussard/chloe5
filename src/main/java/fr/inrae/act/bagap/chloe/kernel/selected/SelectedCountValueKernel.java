package fr.inrae.act.bagap.chloe.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class SelectedCountValueKernel extends SelectedLandscapeMetricKernel {

	private int[] mapValues;
	
	public SelectedCountValueKernel(int windowSize, Set<Pixel> pixels, short[] shape, float[] coeff, int noDataValue, int[] values){		
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
			
			for(int i=0; i<outDatas()[0].length; i++){
				outDatas()[ind][i] = 0f;
			}
			
			outDatas()[ind][2] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
			
			
			final int mid = windowSize() / 2;
			int ic;
			int v;
			int mv;				
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							if(shape()[ic] == 1){
								v = (int) inDatas()[((y + dy) * width()) + (x + dx)];		
								if(v == noDataValue()){
									outDatas()[ind][0] += coeff()[ic];
								}else if(v == 0){
									outDatas()[ind][1] += coeff()[ic];
								}else{
									mv = mapValues[v];
									outDatas()[ind][mv+3] += coeff()[ic];	
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
		mapValues = null;
	}

}
