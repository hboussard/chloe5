package fr.inrae.act.bagap.chloe.window.kernel.sliding;

public abstract class AbstractTripleSlidingLandscapeMetricKernel extends AbstractDoubleSlidingLandscapeMetricKernel implements TripleSlidingLandscapeMetricKernel {

	private float[] inDatas3;
	
	protected AbstractTripleSlidingLandscapeMetricKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, coeff, noDataValue, unfilters);
	}
	
	@Override
	public void setInDatas3(float[] inDatas3){
		this.inDatas3 = inDatas3;
	}
	
	@Override
	public float[] inDatas3(){
		return inDatas3;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		inDatas3 = null;
	}

}
