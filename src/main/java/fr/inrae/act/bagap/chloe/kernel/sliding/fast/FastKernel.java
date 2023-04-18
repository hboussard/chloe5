package fr.inrae.act.bagap.chloe.kernel.sliding.fast;

import fr.inrae.act.bagap.chloe.kernel.sliding.SlidingLandscapeMetricKernel;

public abstract class FastKernel extends SlidingLandscapeMetricKernel {
	
	private float[][] buf;
	
	private int rayon;
	
	private int nValuesTot;
	
	protected FastKernel(int windowSize, int displacement, int noDataValue, int[] unfilters) {
		super(windowSize, displacement, null, noDataValue, unfilters);
		this.rayon = windowSize()/2+1;
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.buf = new float[width][nValuesTot];
	}
	
	@Override
	public void applySlidingWindow(int theY, int buffer) {
		this.setTheY(theY); // indique position de la premiere ligne du buffer dans imageIn
		final int nlines = ((buffer-1) - (displacement()-theY()%displacement())%displacement() )/displacement()+1;
		execute(width(), 2*nlines);
	}
	
	@Override
	public void run() {
		final int x = getGlobalId(0);
		final int pass = getPassId();
		final int vertical = pass % 2;
		final int line = (displacement() - theY()%displacement())%displacement() + (pass/2)*displacement();
		if(vertical == 0){
			processVerticalPixel(x, line);
		}else if(x < (width() - bufferROIXMin() - bufferROIXMax()-1) / displacement() + 1){
			processHorizontalPixel(x, line);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		buf = null;
	}

	/**
	 * parcours vertical de l'image centre sur ligne theY+line et stocke dans buf
	 * @param x : x coordinate
	 * @param line : line number
	 */
	protected abstract void processVerticalPixel(int x, int line);
	
	/**
	 * parcours horizontal de buf: position dans imageOut : (x, y=line/dep)
	 * @param x : x coordinate
	 * @param line : line number
	 */
	protected abstract void processHorizontalPixel(int x, int line);
	
	/**
	 * recuperation du coeff de ponderation locale
	 * @param ind : local
	 * @return le coeff de ponderation
	 */
	protected abstract float coeff(int ind);

	protected void setNValuesTot(int nValuesTot) {
		this.nValuesTot = nValuesTot;
	}
	
	protected int nValuesTot(){
		return this.nValuesTot;
	}
	
	protected int rayon(){
		return this.rayon;
	}
	
	protected float[][] buf(){
		return this.buf;
	}
	
}
