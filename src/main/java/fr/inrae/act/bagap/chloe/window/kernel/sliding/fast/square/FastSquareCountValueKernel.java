package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.FastCountValueKernel;

public class FastSquareCountValueKernel extends FastCountValueKernel implements FastSquareKernel {

	public FastSquareCountValueKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
	}
	
	@Override
	protected float coeff(int ind){
		return 1;
	}
	
}
