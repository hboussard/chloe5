package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastGrainBocagerDetectionBocageKernel extends FastKernel {

	private float minHauteur; //= 3; // hauteur en metres minimum de l'element boise pour etre pris en compte
	
	protected FastGrainBocagerDetectionBocageKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, float minHauteur) {
		super(windowSize, displacement, noDataValue, unfilters);
		this.minHauteur = minHauteur;
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
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);
				nb += coeff;
				if(v == noDataValue()){
					nb_nodata += coeff;
				}else if(v >= minHauteur){
					//sum += v*coeff;
					sum += coeff;
				}
			}
		}
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = sum;
	}
	
	@Override
	protected void processHorizontalPixel(int x, int line) {
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		if(!hasFilter() || filterValue((int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			outDatas()[ind][1] = inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()]; // valeur pixel central
			
			float vCentral = (float) outDatas()[ind][1];
			if(vCentral == noDataValue()) {
				outDatas()[ind][2] = 0; // nb value
				outDatas()[ind][3] = 1; // nb nodataValue
				outDatas()[ind][4] = noDataValue(); // sum
			}else if(vCentral < minHauteur){
				outDatas()[ind][2] = 1; // nb value
				outDatas()[ind][3] = 0; // nb nodataValue
				outDatas()[ind][4] = 0; // sum
			}else{
				int x_buf = x*displacement()+bufferROIXMin();
				float val;
				for(int value=2; value<=4; value++) {
					val = 0;
					for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
						val += buf()[i][value] * coeff(i-x_buf);
					}
					outDatas()[ind][value] = val;
				}
			}
		}else{
			outDatas()[ind][0] = 0; // filtre pas ok 
		}
	}

}
