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
		float c;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = inDatas()[((y + dy) * width()) + x];
				c = coeff(dy);
				if(v == noDataValue()){
					nb_nodata += c;
				}else{
					nb += c;
					sum += v*c;
					square_sum += v*c * v*c;
					min = Math.min(min, v*c);
					max = Math.max(max, v*c);
				}
			}
		}
		
		buf()[x][1] = nb_nodata;
		buf()[x][2] = nb;
		buf()[x][3] = sum;
		buf()[x][4] = (float) square_sum;
		buf()[x][5] = min;
		buf()[x][6] = max;
		
	}
	
	@Override
	protected void processHorizontalPixel(int x, int line) {
		
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		outDatas()[ind][7] = (int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()]; // valeur pixel central
		
		if(filter((int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			int x_buf = x*displacement()+bufferROIXMin();
			float val;
			for(int value=1; value<4; value++) {
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
			outDatas()[ind][5] = min;
			
			float max = Float.MIN_VALUE;
			for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
				max = Math.max(max, buf()[i][6] * coeff(i-x_buf));
			}
			outDatas()[ind][6] = max;
			
		}else{
			// filtre pas ok
			for(int value=0; value<nValuesTot(); value++) {
				outDatas()[ind][value] = 0;
			}
		}
	}
	
}
