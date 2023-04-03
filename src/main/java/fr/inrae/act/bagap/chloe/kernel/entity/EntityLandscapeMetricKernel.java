package fr.inrae.act.bagap.chloe.kernel.entity;

import java.util.Map;

public abstract class EntityLandscapeMetricKernel {

	private int roiWidth;

	private int roiHeight;
	
	private final int noDataValue;

	private float[] inDatas, inAreaDatas;

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
	
	public void setInAreaDatas(float[] inAreaDatas){
		this.inAreaDatas = inAreaDatas;
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
	
	public float[] inAreaDatas(){
		return inAreaDatas;
	}
	
	public Map<Integer, double[]> outDatas(){
		return outDatas;
	}
	
	public abstract void applyAreaWindow();
	
}
