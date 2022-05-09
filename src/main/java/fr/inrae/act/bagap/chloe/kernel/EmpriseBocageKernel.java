package fr.inrae.act.bagap.chloe.kernel;

public class EmpriseBocageKernel extends SlidingLandscapeMetricKernel {
	
	private float coeffH = 10; // le bocage à un effet jusque'a coeffH fois sa hauteur
	
	private float cellSize = 5; /// la taille du pixel en metre 
	
	public EmpriseBocageKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters){		
		super(windowSize, displacement, shape, coeff, noDataValue, unfilters);
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}

	public void processPixel(int x, int y, int localY) {
		
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			/*
			for(int i=0; i<imageOut()[0].length; i++){
				imageOut()[ind][i] = 0f;
			}
			*/
			
			//if(imageIn[(y * width) + x] != -1f) { // gestion des filtres a mettre en place 
				
			final int mid = windowSize() / 2;
			float h = imageIn()[((y) * width()) + (x)];
			float v;
			int ic;
			if(h != noDataValue() && h != 0){
				double R = h * coeffH;
				//System.out.println(x+" "+y+" --> "+h+" "+R);
				
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								
								double r = cellSize*Math.sqrt((dx*dx)+(dy*dy)); 
								if(r <= R){
									
									//System.out.println(r/R);
									//System.out.println(imageOut().length+" "+((y + dy) * width()) + (x + dx));
									//double d = imageOut()[((y + dy) * width()) + (x + dx)][2];
									//System.out.println("--> "+d);
									imageOut()[((y + dy) * width()) + (x + dx)][2] = Math.min(r/R, imageOut()[((y + dy) * width()) + (x + dx)][2]);
									//System.out.println(imageOut()[((y + dy) * width()) + (x + dx)][1]);
									/*
									ic = ((dy+mid) * windowSize()) + (dx+mid);
									v = (short) imageIn()[((y + dy) * width()) + (x + dx)];		
									if(v == noDataValue()){
										imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
									}else{
										imageOut()[((y + dy) * width()) + (x + dx)][1] = Math.min(r/R, imageOut()[((y + dy) * width()) + (x + dx)][1]);
									}
									*/
								}
							}
						}
					}
				}
			}
			
			/*
			int ic;
			float v;
			int mv;				
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							if(shape()[ic] == 1){
								v = (short) imageIn()[((y + dy) * width()) + (x + dx)];		
								if(v == noDataValue()){
									imageOut()[ind][0] = imageOut()[ind][0] + coeff()[ic];
								}else if(v == 0){
									imageOut()[ind][1] = imageOut()[ind][1] + coeff()[ic];
								}else{
									mv = mapValues[v];
									imageOut()[ind][mv+2] = imageOut()[ind][mv+2] + coeff()[ic];	
								}
							}
						}
					}
				}
			}
			*/
		}
	}
	
}
