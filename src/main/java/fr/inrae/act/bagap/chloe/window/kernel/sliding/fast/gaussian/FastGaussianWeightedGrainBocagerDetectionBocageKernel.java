package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.FastGrainBocagerDetectionBocageKernel;

public class FastGaussianWeightedGrainBocagerDetectionBocageKernel extends FastGrainBocagerDetectionBocageKernel implements FastGaussianWeightedKernel {

	private float[] gauss;
	
	public FastGaussianWeightedGrainBocagerDetectionBocageKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, float minHauteur) {
		super(windowSize, displacement, noDataValue, unfilters, minHauteur);

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

	@Override
	protected float coeff(int ind) {
		return gauss[Math.abs(ind)];
	}

}
