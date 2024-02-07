package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public interface TripleSlidingLandscapeMetricKernel extends DoubleSlidingLandscapeMetricKernel {

	void setInDatas3(float[] inDatas3);
	
	float[] inDatas3();
	
}
