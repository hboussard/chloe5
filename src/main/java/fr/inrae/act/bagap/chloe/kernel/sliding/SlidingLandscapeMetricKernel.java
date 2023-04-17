package fr.inrae.act.bagap.chloe.kernel.sliding;

import com.aparapi.Kernel;

import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public abstract class SlidingLandscapeMetricKernel extends Kernel implements LandscapeMetricKernel {

	private final int windowSize;
	
	private final int displacement;
	
	private float[] coeff;
	
	private final int noDataValue;
	
	private float[] inDatas;
	
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

	public void setInDatas(float[] inDatas){
		this.inDatas = inDatas;
	}
	
	public void setTheY(int theY){
		this.theY = theY;
	}

	protected float[] inDatas(){
		return inDatas;
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
