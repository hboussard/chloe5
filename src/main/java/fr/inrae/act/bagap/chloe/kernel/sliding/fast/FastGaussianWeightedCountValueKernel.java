package fr.inrae.act.bagap.chloe.kernel.sliding.fast;

public class FastGaussianWeightedCountValueKernel extends FastGaussianWeightedKernel {

	public FastGaussianWeightedCountValueKernel(int windowSize, int displacement, int noDataValue, int[] values, int[] unfilters){
		super(windowSize, displacement, noDataValue, values, unfilters);
		setNValuesTot(values.length+4);
	}
	
	@Override
	protected void processVerticalPixel(int x, int line) {
		
		int y = theY() + line + bufferROIYMin();
		int i, dy, ic, v, mv;
		
		for(i=0; i<nValuesTot(); i++) {
			buf()[x][i] = 0;
		}
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				ic = abs(dy);
				v = (int) inDatas()[((y + dy) * width()) + x];
							
				if(v == noDataValue()){
					mv = 1;
				}else if(v==0){
					mv = 2;
				}else{
					mv = mapValues()[v] + 4;
				}
				
				buf()[x][mv] += gauss()[ic];
			}
		}
	}
	
	@Override
	protected void processHorizontalPixel(int x, int line) {
		
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		outDatas()[ind][3] = (int) inDatas()[((theY() + line)* width()) + x*displacement()+bufferROIXMin()]; // valeur pixel central
		
		if(filter((int) inDatas()[((theY() + line)* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			int x_buf = x*displacement()+bufferROIXMin();
			float val;
			for(int value=1; value<nValuesTot(); value++) {
				if(value==3){
					continue;
				}
				val = 0;
				for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
					val += buf()[i][value] * gauss()[abs(i-x_buf)];
				}
				outDatas()[ind][value] = val;
			}
			
		}else{
			// filtre pas ok
			for(int value=0; value<nValuesTot(); value++) {
				outDatas()[ind][value] = 0;
			}
		}
	}
	
}
