package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastQuantitativeKernel extends FastKernel {
	
	public FastQuantitativeKernel(int windowSize, int displacement, int noDataValue, int[] unfilters){
		super(windowSize, displacement, noDataValue, unfilters);
		setNValuesTot(8);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy;
		float v;
		
		for(i=0; i<nValuesTot(); i++) {
			buf()[x][i] = 0;
		}
		float nb_nodata = 0;
		float nb = 0;
		float sum = 0;
		float coeff;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);
				nb += coeff;
				if(v == noDataValue()){
					nb_nodata += coeff;
				}else{
					
					sum += v*coeff;
					square_sum += v*coeff * v*coeff;
					min = Math.min(min, v*coeff);
					max = Math.max(max, v*coeff);
				}
			}
		}
		
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = sum;
		buf()[x][5] = (float) square_sum;
		buf()[x][6] = min;
		buf()[x][7] = max;
		
	}
	
	@Override
	protected void processHorizontalPixel(int x, int line) {
		
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		if(!hasFilter() || filterValue((int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			outDatas()[ind][1] = (int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()]; // valeur pixel central
			
			int x_buf = x*displacement()+bufferROIXMin();
			float val;
			for(int value=2; value<5; value++) {
				val = 0;
				for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
					val += buf()[i][value] * coeff(i-x_buf);
				}
				outDatas()[ind][value] = val;
			}
			
			float min = Float.MAX_VALUE;
			for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
				min = Math.min(min, buf()[i][5] * coeff(i-x_buf));
			}
			outDatas()[ind][6] = min;
			
			float max = Float.MIN_VALUE;
			for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
				max = Math.max(max, buf()[i][6] * coeff(i-x_buf));
			}
			outDatas()[ind][7] = max;
			
		}else{
			outDatas()[ind][0] = 0; // filtre pas ok 
		}
	}
	
}
