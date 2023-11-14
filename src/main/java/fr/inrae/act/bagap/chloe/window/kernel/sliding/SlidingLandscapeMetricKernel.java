package fr.inrae.act.bagap.chloe.window.kernel.sliding;

import fr.inrae.act.bagap.chloe.window.kernel.LandscapeMetricKernel;

public interface SlidingLandscapeMetricKernel extends LandscapeMetricKernel {

	void applySlidingWindow(int theY, int buffer);
	
	int windowSize();
	
	void setWidth(int width);
	
	void setHeight(int height);

	void setBufferROIXMin(int bufferROIXMin);

	void setBufferROIXMax(int bufferROIXMax);

	void setBufferROIYMin(int bufferROIYMin);

	void setBufferROIYMax(int bufferROIYMax);

	void setInDatas(float[] inDatas);
	
	void setTheY(int theY);

	void setOutDatas(float[][] outDatas);
	
	float[][] outDatas();
	
	int bufferROIXMin();
	
	int bufferROIXMax();
	
	int bufferROIYMin();
	
	int bufferROIYMax();
	
}
