package fr.inrae.act.bagap.chloe.kernel.sliding;

public abstract class DoubleSlidingLandscapeMetricKernel extends SlidingLandscapeMetricKernel{

	private float[] inDatas2;
	
	protected DoubleSlidingLandscapeMetricKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}
	
	public void setInDatas2(float[] inDatas2){
		this.inDatas2 = inDatas2;
	}
	
	public float[] inDatas2(){
		return inDatas2;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		inDatas2 = null;
	}

}
