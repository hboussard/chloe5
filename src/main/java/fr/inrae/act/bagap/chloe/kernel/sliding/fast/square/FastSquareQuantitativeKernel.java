package fr.inrae.act.bagap.chloe.kernel.sliding.fast.square;

import fr.inrae.act.bagap.chloe.kernel.sliding.fast.FastQuantitativeKernel;

public class FastSquareQuantitativeKernel extends FastQuantitativeKernel implements FastSquareKernel {

	public FastSquareQuantitativeKernel(int windowSize, int displacement, int noDataValue, int[] unfilters){
		super(windowSize, displacement, noDataValue, unfilters);
	}

	@Override
	protected float coeff(int ind) {
		return 1;
	}
	
}
