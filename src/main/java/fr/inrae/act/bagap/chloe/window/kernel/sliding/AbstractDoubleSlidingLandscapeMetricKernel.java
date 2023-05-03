package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public abstract class AbstractDoubleSlidingLandscapeMetricKernel extends AbstractSlidingLandscapeMetricKernel implements DoubleSlidingLandscapeMetricKernel {

	private float[] inDatas2;
	
	protected AbstractDoubleSlidingLandscapeMetricKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
	}
	
	@Override
	public void setInDatas2(float[] inDatas2){
		this.inDatas2 = inDatas2;
	}
	
	@Override
	public float[] inDatas2(){
		return inDatas2;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		inDatas2 = null;
	}

}
