package fr.inrae.act.bagap.chloe.kernel.entity;

import java.util.Map;

import fr.inrae.act.bagap.chloe.kernel.LandscapeMetricKernel;

public abstract class EntityLandscapeMetricKernel implements LandscapeMetricKernel {

	private int width;

	private int height;
	
	private final int noDataValue;

	private float[] inDatas, entityDatas;

	private Map<Integer, double[]> outDatas;
	
	public EntityLandscapeMetricKernel(int noDataValue){
		this.noDataValue = noDataValue;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setInDatas(float[] inDatas){
		this.inDatas = inDatas;
	}
	
	public void setEntityDatas(float[] entityDatas){
		this.entityDatas = entityDatas;
	}
	
	public void setOutDatas(Map<Integer, double[]> outDatas){
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
	
	public float[] entityDatas(){
		return entityDatas;
	}
	
	public Map<Integer, double[]> outDatas(){
		return outDatas;
	}
	
	public void init(){
		// do nothing
	}
	
	public void dispose(){
		inDatas = null;
		entityDatas = null;
		outDatas.clear();
		outDatas = null;
	}
	
	public abstract void applyEntityWindow();
	
}
