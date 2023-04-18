package fr.inrae.act.bagap.chloe.kernel.sliding.fast.square;

import fr.inrae.act.bagap.chloe.kernel.sliding.fast.FastCountValueAndCoupleKernel;

public class FastSquareCountValueAndCoupleKernel extends FastCountValueAndCoupleKernel implements FastSquareKernel {
	
	public FastSquareCountValueAndCoupleKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
	}

	@Override
	protected float coeff(int ind){
		return 1;
	}
	
}
