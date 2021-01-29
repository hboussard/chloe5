package fr.inrae.act.bagap.chloe.kernel;

import com.aparapi.Kernel;

public abstract class LandscapeMetricKernel extends Kernel {

	private float[][] imageOut;
	
	private int theY;
	
	private final int width, height;
	
	private final int internalROI; // in pixels
	
	public LandscapeMetricKernel(int width, int height, int internalROI){
		this.width = width;
		this.height = height;
		this.internalROI = internalROI;
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
		execute((width - 2*internalROI) * buffer);
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
	
	protected int internalROI(){
		return this.internalROI;
	}
	
}
