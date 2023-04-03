package fr.inrae.act.bagap.chloe.kernel.grid;

import com.aparapi.Kernel;

public abstract class GridLandscapeMetricKernel extends Kernel {

	private final int gridSize;
	
	private final int noDataValue;
	
	private float imageIn[];
	
	private double[][] imageOut;
	
	private int theY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	@SuppressWarnings("deprecation")
	protected GridLandscapeMetricKernel(int gridSize, int noDataValue){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.gridSize = gridSize;
		this.noDataValue = noDataValue;
	}
	
	public void applyGridWindow(int outWidth, int theY) {
		this.theY = theY;
		execute(outWidth);
	}
	
	@Override
	public void run() {
		processGrid(getGlobalId(0), theY());
	}
	
	protected void processGrid(int x, int theY) {
		// do nothing
	}
	
	public int gridSize(){
		return this.gridSize;
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
