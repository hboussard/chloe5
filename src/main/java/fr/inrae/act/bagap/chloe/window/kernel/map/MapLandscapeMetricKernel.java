package fr.inrae.act.bagap.chloe.window.kernel.map;

import fr.inrae.act.bagap.chloe.window.kernel.LandscapeMetricKernel;

public abstract class MapLandscapeMetricKernel implements LandscapeMetricKernel {

	private final int noDataValue;
	
	private float[] inDatas;
	
	private float[] outDatas;
	
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
	
	public void setOutDatas(float[] outDatas){
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
	
	public float[] outDatas(){
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
