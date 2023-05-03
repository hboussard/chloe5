package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.DoubleSlidingLandscapeMetricKernel;

public abstract class DoubleFastKernel extends FastKernel implements DoubleSlidingLandscapeMetricKernel {

	private float[] inDatas2;
	
	protected DoubleFastKernel(int windowSize, int displacement, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, noDataValue, unfilters);
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
