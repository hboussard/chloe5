package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastQualitativeKernel extends FastKernel {

	private int[] mapValues;
	
	protected FastQualitativeKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters) {
		super(windowSize, displacement, noDataValue, unfilters);

		int maxV = 0;
		for(int v : values){
			maxV = Math.max(v, maxV);
		}
		maxV++;
		mapValues = new int[maxV];
		for(int i=0; i<values.length; i++){
			mapValues[values[i]] = i;
		}
	}
	
	protected int[] mapValues(){
		return this.mapValues;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapValues = null;
	}

}
