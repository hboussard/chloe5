package fr.inrae.act.bagap.chloe.cluster.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.Analysis;

public abstract class TabChessClusteringAnalysis extends Analysis{

	protected float noDataValue;
	
	protected int nbNoDataValue;
	
	protected float[] inDatas;
	
	protected int height, width;
	
	protected Set<Float> interest;
	
	/*
	public TabChessClusteringAnalysis(int[] inDatas, int width, int height, int[] interest, int noDataValue){		
		this.inDatas = inDatas;
		this.height = height;
		this.width = width;
		this.interest = new HashSet<Float>();
		for(int v : interest){
			this.interest.add((float) v);
		}
		this.noDataValue = noDataValue;
	}
	*/
	
	public TabChessClusteringAnalysis(float[] inDatas, int width, int height, int[] interest, int noDataValue){	
		this.inDatas = inDatas;
		/*
		this.inDatas = new int[inDatas.length];
		int ind = 0;
		for(float tc : inDatas){
			this.inDatas[ind++] = (int) tc;
		}
		*/
		this.height = height;
		this.width = width;
		this.interest = new HashSet<Float>();
		for(int v : interest){
			this.interest.add((float) v);
		}
		this.noDataValue = noDataValue;
	}
	
	protected float getSame(Map<Float, Float> sames, float v){
		float nv = sames.get(v);
		if(nv == noDataValue){
			return v;
		}else{
			return getSame(sames, nv);
		}
	}

	@Override
	protected void doInit() {
		nbNoDataValue = 0;
	}

	@Override
	protected void doClose() {
		// do nothing
	}
	
	public int getNbNoDataValue(){
		return nbNoDataValue;
	}
	
	protected float[] lisseNumerotation(float[] data, int noDataValue){
		float[] lisse = new float[data.length];
		Map<Float, Integer> values = new HashMap<Float, Integer>();
		int index = 1;
		float v;
		for(int i=0; i<data.length; i++){
			v = data[i];
			if(v != noDataValue && v != 0){
				if(!values.containsKey(v)){
					values.put(v, index++);
				}
				lisse[i] = values.get(v);
			}else{
				lisse[i] = v;
			}
		}
		return lisse;
	}

}
