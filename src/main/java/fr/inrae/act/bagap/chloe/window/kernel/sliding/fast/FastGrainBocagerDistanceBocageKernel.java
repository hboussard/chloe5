package fr.inrae.act.bagap.chloe.window.kernel.sliding.fast;

public abstract class FastGrainBocagerDistanceBocageKernel extends DoubleFastKernel {

	private float cellSize; // = 5; // la taille du pixel en metre (IGN)
	
	private float minHauteur; // = 3; // 3 metres minimum 
	
	private float seuilMax; // = 30; // 30 metres maximum

	protected FastGrainBocagerDistanceBocageKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, float cellSize, float minHauteur, float seuilMax) {
		super(windowSize, displacement, noDataValue, unfilters);
		this.cellSize = cellSize;
		this.minHauteur = minHauteur;
		this.seuilMax = seuilMax;
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
		float coeff;
		float min = 1.0f;
		double r, R;
		float global_r = -1;
		float global_R = -1;
		for (dy = -rayon() +1; dy < rayon(); dy++) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				
				v = inDatas()[((y + dy) * width()) + x];
				coeff = coeff(dy);
				nb += coeff;
				if(v == noDataValue()){
					nb_nodata += coeff;
				}else if(v >= minHauteur){
					if(v > seuilMax){
						v = seuilMax;
					}
					
					r = cellSize * Math.abs(dy); // distance au centroid
					R = v * inDatas2()[((y + dy) * width()) + x];	
					
					if(r < R){
					//if(r > 0 && r < R){
						//if(r/R < min || (r/R == min && R > global_R)){
						if(r/R < min){
						//if(R > global_R){
							min = (float) (r/R);
							global_r = (float) r;
							global_R = (float) R;
						}
					}
				}
			}
		}
		buf()[x][2] = nb;
		buf()[x][3] = nb_nodata;
		buf()[x][4] = global_r;
		buf()[x][5] = global_R;
	}
	
	@Override
	protected void processHorizontalPixel(int x, int line) {
		int y = line / displacement();
		int ind = y * ((width()-1-bufferROIXMin()-bufferROIXMax())/displacement()+1) + x;
		
		if(!hasFilter() || filterValue((int) inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()])){ // gestion des filtres
			
			outDatas()[ind][0] = 1; // filtre ok 
			
			float hCentral = inDatas()[((theY() + line + bufferROIYMin())* width()) + x*displacement()+bufferROIXMin()]; // valeur pixel central
			
			outDatas()[ind][1] = hCentral;
			
			int x_buf = x*displacement()+bufferROIXMin();
			float min = 1.0f;
			float r, rprime;
			if(hCentral > 0){
				
				min = 0;
				
			}else{
				for(int i=max(x_buf-rayon()+1,0); i<min(x_buf+rayon(), width()); i++) {
					r = buf()[i][4];
					if(r != -1){
						rprime = (float) Math.sqrt((r*r) + Math.pow(cellSize * (i-x_buf), 2));
						min = Math.min(min, rprime / buf()[i][5]);
					}
				}
			}
			
			
			outDatas()[ind][6] = min;
			
		}else{
			outDatas()[ind][0] = 0; // filtre pas ok 
		}
	}
	
}
