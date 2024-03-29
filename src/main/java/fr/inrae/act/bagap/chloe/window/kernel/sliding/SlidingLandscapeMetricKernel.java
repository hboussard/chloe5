package fr.inrae.act.bagap.chloe.window.kernel.sliding;

import com.aparapi.Kernel;

import fr.inrae.act.bagap.chloe.window.kernel.LandscapeMetricKernel;

public abstract class SlidingLandscapeMetricKernel extends Kernel implements LandscapeMetricKernel {

	private final int windowSize;
	
	private final int displacement;
	
	private float[] coeff;
	
	private final int noDataValue;
	
	private float[][] inDatas;
	
	private double[][] outDatas;
	
	private int theY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	private int[] unfilters;
	
	@SuppressWarnings("deprecation")
	protected SlidingLandscapeMetricKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.displacement = displacement;
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
	
	protected void processPixel(int x, int y, int localY){
		// do nothing
	}
	
	protected boolean hasFilter(){
		return unfilters != null;
	}
	
	protected boolean filterValue(int f){
		for(int uf : unfilters){
			if(uf == f){
				return false;
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
	
	public void setTheY(int theY){
		this.theY = theY;
	}

	/*
	@Override
	public void setInDatas(float[] inDatas){
		if(this.inDatas == null) {
			this.inDatas = new float[][] {inDatas};
		}else {
			this.inDatas[0] = inDatas;
		}
	}
	*/
	
	public void setInDatas(float[][] inDatas) {
		this.inDatas = inDatas;
	}
	
	protected float[] inDatas(){
		return this.inDatas[0];
	}
	
	protected float[] inDatas(int index){
		return this.inDatas[index-1];
	}

	public void setOutDatas(double[][] outDatas){
		this.outDatas = outDatas;
	}
	
	public double[][] outDatas(){
		return outDatas;
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
	
	@Override
	public void dispose(){
		super.dispose();
		coeff = null;
		inDatas = null;
		outDatas = null;
		unfilters = null;
	}
		
}
