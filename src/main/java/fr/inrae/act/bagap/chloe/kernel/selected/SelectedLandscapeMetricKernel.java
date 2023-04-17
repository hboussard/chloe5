package fr.inrae.act.bagap.chloe.kernel.selected;

import java.util.Map;
import java.util.Set;

import com.aparapi.Kernel;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public abstract class SelectedLandscapeMetricKernel extends Kernel implements LandscapeMetricKernel {

	private final int windowSize;
	
	private float[] coeff;
	
	private final int noDataValue;
	
	private float[] inDatas;
	
	private Map<Pixel, double[]> outDatas;
	
	private int localROIY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	private Set<Pixel> pixels;
	
	@SuppressWarnings("deprecation")
	protected SelectedLandscapeMetricKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.pixels = pixels;
		this.coeff = coeff;
		this.noDataValue = noDataValue;
	}
	
	public void applySelectedWindow(int buffer, int localROIY) {
		this.localROIY = localROIY;
		execute((width - bufferROIXMin - bufferROIXMax) * buffer);
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		
		Pixel p = new Pixel(getGlobalId(0) % width(), (localROIY+(getGlobalId(0) / width())));
		if(pixels().contains(p)){
			processPixel(p, x, y);
		}
	}
	
	protected void processPixel(Pixel p, int x, int y){
		// do nothing
	}

	public int windowSize(){
		return this.windowSize;
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
	
	protected float[] inDatas(){
		return inDatas;
	}
	
	public void setOutDatas(Map<Pixel, double[]> outDatas){
		this.outDatas = outDatas;
	}
	
	public Map<Pixel, double[]> outDatas(){
		return outDatas;
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
		coeff = null;
		inDatas = null;
		outDatas = null;
		pixels = null;
	}
	
}
