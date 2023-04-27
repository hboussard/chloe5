package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastCountValueKernel extends FastQualitativeKernel {

	public FastCountValueKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
		setNValuesTot(values.length+5);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, v, mv;
		
		for(i=0; i<nValuesTot(); i++) {
			buf()[x][i] = 0;
		}
		float coeff;
		float nb = 0;
		float nb_nodata = 0;
		float nb_zero = 0;
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = (int) inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);
				//buf()[x][2] += coeff; 
				nb += coeff;
				if(v == noDataValue()){
					//mv = 3;
					nb_nodata += coeff;
				}else if(v==0){
					//mv = 4;
					nb_zero += coeff;
				}else{
					mv = mapValues()[v] + 5;
					buf()[x][mv] += coeff;
				}
			}
		}
		
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = nb_zero;
		
	}
	
}
