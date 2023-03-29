package fr.inrae.act.bagap.chloe.kernel.sliding;

import com.aparapi.Kernel;

public abstract class SlidingLandscapeMetricKernel extends Kernel {

	private final int windowSize;
	
	private final int displacement;
	
	private short[] shape;
	
	private float[] coeff;
	
	private final int noDataValue;
	
	private float imageIn[];
	
	private double[][] imageOut;
	
	private int theY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	private int[] unfilters;
	
	@SuppressWarnings("deprecation")
	protected SlidingLandscapeMetricKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue, int[] unfilters){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.displacement = displacement;
		this.shape = shape;
		this.coeff = coeff;
		this.noDataValue = noDataValue;
		this.unfilters = unfilters;
	}
	
	public void applySlidingWindow(int theY, int buffer) {
		this.theY = theY;
		execute((width - bufferROIXMin - bufferROIXMax) * buffer);
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		processPixel(x, theY() + y, y);
	}
	
	protected void processPixel(int x, int y, int localY) {
		// do nothing
	}

	protected boolean filter(int f){
		if(unfilters != null){
			for(int uf : unfilters){
				if(uf == f){
					return false;
				}
			}
		}
		return true;
	}
	
	public int windowSize(){
		return this.windowSize;
	}
	
	protected int displacement(){
		return this.displacement;
	}
	
	protected void setShape(short[] shape){
		this.shape = shape;
	}
	
	protected short[] shape(){
		return this.shape;
	}
	
	protected void setCoeff(float[] coeff){
		this.coeff = coeff;
	}

	protected float[] coeff(){
		return this.coeff;
	}
	
	protected int noDataValue(){
		return noDataValue;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setBufferROIXMin(int bufferROIXMin) {
		this.bufferROIXMin = bufferROIXMin;
	}

	public void setBufferROIXMax(int bufferROIXMax) {
		this.bufferROIXMax = bufferROIXMax;
	}

	public void setBufferROIYMin(int bufferROIYMin) {
		this.bufferROIYMin = bufferROIYMin;
	}

	public void setBufferROIYMax(int bufferROIYMax) {
		this.bufferROIYMax = bufferROIYMax;
	}

	public void setImageIn(float[] imageIn){
		this.imageIn = imageIn;
	}
	
	public void setTheY(int theY){
		this.theY = theY;
	}

	protected float[] imageIn(){
		return imageIn;
	}
	
	public void setImageOut(double[][] imageOut){
		this.imageOut = imageOut;
	}
	
	public double[][] imageOut(){
		return imageOut;
	}
	
	protected int theY(){
		return this.theY;
	}
	
	protected int width(){
		return this.width;
	}
	
	protected int height(){
		return this.height;
	}
	
	public int bufferROIXMin(){
		return this.bufferROIXMin;
	}
	
	public int bufferROIXMax(){
		return this.bufferROIXMax;
	}
	
	public int bufferROIYMin(){
		return this.bufferROIYMin;
	}
	
	public int bufferROIYMax(){
		return this.bufferROIYMax;
	}
}