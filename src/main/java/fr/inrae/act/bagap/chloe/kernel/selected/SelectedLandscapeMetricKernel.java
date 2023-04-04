package fr.inrae.act.bagap.chloe.kernel.selected;

import java.util.Set;

import com.aparapi.Kernel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public abstract class SelectedLandscapeMetricKernel extends Kernel implements LandscapeMetricKernel {

	private final int windowSize;
	
	private short[] shape;
	
	private float[] coeff;
	
	private final int noDataValue;
	
	private float[] inDatas;
	
	private double[][] outDatas;
	
	private int theY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	private Set<Pixel> pixels;
	
	@SuppressWarnings("deprecation")
	protected SelectedLandscapeMetricKernel(int windowSize, Set<Pixel> pixels, short[] shape, float[] coeff, int noDataValue){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.pixels = pixels;
		this.shape = shape;
		this.coeff = coeff;
		this.noDataValue = noDataValue;
	}
	
	public int windowSize(){
		return this.windowSize;
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

	public void setInDatas(float[] inDatas){
		this.inDatas = inDatas;
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
	
	public void applySelectedWindow(int theY, int buffer) {
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

	public Set<Pixel> pixels(){
		return pixels;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		shape = null;
		coeff = null;
		inDatas = null;
		outDatas = null;
		pixels = null;
	}
}
