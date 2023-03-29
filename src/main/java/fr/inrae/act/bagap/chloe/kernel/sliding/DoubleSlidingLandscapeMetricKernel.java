package fr.inrae.act.bagap.chloe.kernel.sliding;

public abstract class DoubleSlidingLandscapeMetricKernel extends SlidingLandscapeMetricKernel{

	private float[] imageIn2;
	
	protected DoubleSlidingLandscapeMetricKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}
	
	public void setImageIn2(float[] imageIn2){
		this.imageIn2 = imageIn2;
	}
	
	public float[] imageIn2(){
		return imageIn2;
	}

}
