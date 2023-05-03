package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square;

import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.FastGrainBocagerDistanceBocageKernel;

public class FastSquareGrainBocagerDistanceBocageKernel extends FastGrainBocagerDistanceBocageKernel {

	public FastSquareGrainBocagerDistanceBocageKernel(int windowSize, int displacement, int noDataValue,
			int[] unfilters, float cellSize, float minHauteur, float seuilMax) {
		super(windowSize, displacement, noDataValue, unfilters, cellSize, minHauteur, seuilMax);
	}
	
	@Override
	protected float coeff(int ind){
		return 1;
	}

}
