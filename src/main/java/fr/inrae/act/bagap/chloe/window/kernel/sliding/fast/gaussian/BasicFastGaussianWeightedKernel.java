package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.FastQualitativeKernel;

public class BasicFastGaussianWeightedKernel extends FastQualitativeKernel {
	
	private float[] gauss;
	
	protected BasicFastGaussianWeightedKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters) {
		super(windowSize, displacement, noDataValue, values, unfilters);

		this.gauss = new float[rayon()];
		
		float sigma = rayon()/sqrt(log(2)); // de telle sorte que le diametre correspondant en threshold = sqrt(2)*FWHM
		for(int i=0; i<rayon(); i++) 
			gauss[i] = (float) Math.exp(-i*i/(2*sigma*sigma));
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
