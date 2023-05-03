package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.FastQualitativeKernel;

public class BasicFastGaussianWeightedKernel extends FastQualitativeKernel {
	
	private float[] gauss;
	
	protected BasicFastGaussianWeightedKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters) {
		super(windowSize, displacement, noDataValue, values, unfilters);

		this.gauss = new float[rayon()];
		
		float r=(rayon()-1)/2.f; // sigma = r/sqrt(2), rayon = 2*sqrt(2)*sigma
		for(int i=0; i<rayon(); i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i] = (float) Math.exp(-d*d);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		gauss = null;
	}
	
	protected float[] gauss(){
		return this.gauss;
	}

	@Override
	protected void processVerticalPixel(int x, int line) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected float coeff(int ind) {
		throw new UnsupportedOperationException();
	}
	
}
