package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public abstract class LandscapeMetricKernel extends Kernel {

	private final int windowSize;
	
	private final int displacement;
	
	private final short[] shape;
	
	private final float[] coeff;
	
	private final int noDataValue;
	
	private float imageIn[];
	
	private float[][] imageOut;
	
	private int theY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	protected LandscapeMetricKernel(int windowSize, int displacement, short[] shape, float[] coeff, int noDataValue){
		this.windowSize = windowSize;
		this.displacement = displacement;
		this.shape = shape;
		this.coeff = coeff;
		this.noDataValue = noDataValue;
	}
	
	public int windowSize(){
		return this.windowSize;
	}
	
	protected int displacement(){
		return this.displacement;
	}
	
	protected short[] shape(){
		return this.shape;
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
	
	protected float[] imageIn(){
		return imageIn;
	}
	
	public void setImageOut(float[][] imageOut){
		this.imageOut = imageOut;
	}
	
	protected float[][] imageOut(){
		return imageOut;
	}
	
	public void applySlidingWindow(int theY, int buffer) {
		this.theY = theY;
		//execute(width * buffer);
		execute((width - bufferROIXMin - bufferROIXMax) * buffer);
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
