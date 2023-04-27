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
	protected void processHorizontalPixel(int x, int line) {

		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		if(!hasFilter() || filterValue((int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok
			
			outDatas()[ind][1] = (int) inDatas()[((theY() + line + bufferROIYMin()) * width()) + x*displacement() + bufferROIXMin()]; // valeur pixel central
		
			int x_buf = x * displacement() + bufferROIXMin();
			float val;
			for(int value=2; value<nValuesTot(); value++) {
				val = 0;
				for(int i=max(x_buf-rayon()+1, 0); i<min(x_buf+rayon(), width()); i++) {
					val += buf()[i][value] * coeff(i-x_buf);
				}
				outDatas()[ind][value] = val;
			}
			
		}else{
			outDatas()[ind][0] = 0; // filtre pas ok		
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		mapValues = null;
	}

}
