package fr.inrae.act.bagap.chloe.kernel.map;

import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public abstract class MapLandscapeMetricKernel implements LandscapeMetricKernel {

	private final int noDataValue;
	
	private float[] inDatas;
	
	private double[] outDatas;
	
	private int width, height;
	
	public MapLandscapeMetricKernel(int noDataValue){
		this.noDataValue = noDataValue;
	}
	
	public abstract void applyMapWindow(int theY);
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setInDatas(float[] inDatas){
		this.inDatas = inDatas;
	}
	
	public void setOutDatas(double[] outDatas){
		this.outDatas = outDatas;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}
	
	public int noDataValue(){
		return noDataValue;
	}
	
	public float[] inDatas(){
		return inDatas;
	}
	
	public double[] outDatas(){
		return outDatas;
	}
	
	public void init(){
		// do nothing
	}
	
	public void dispose(){
		inDatas = null;
		outDatas = null;
	}
}
