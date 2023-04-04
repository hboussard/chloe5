package fr.inrae.act.bagap.chloe.kernel.entity;

import java.util.Map;

public abstract class EntityLandscapeMetricKernel {

	private int roiWidth;

	private int roiHeight;
	
	private final int noDataValue;

	private float[] inDatas, entityDatas;

	private Map<Integer, double[]> outDatas;
	
	public EntityLandscapeMetricKernel(int noDataValue){
		this.noDataValue = noDataValue;
	}
	
	public void setRoiWidth(int roiWidth){
		this.roiWidth = roiWidth;
	}
	
	public void setRoiHeight(int roiHeight){
		this.roiHeight = roiHeight;
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
	
	public int roiWidth(){
		return roiWidth;
	}
	
	public int roiHeight(){
		return roiHeight;
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
	
	public abstract void applyEntityWindow();
	
}
