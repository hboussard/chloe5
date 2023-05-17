package fr.inrae.act.bagap.chloe.cluster.chess;

import java.util.HashMap;
import java.util.Map;

public class TabRookClusteringAnalysis extends TabChessClusteringAnalysis {
	
	public TabRookClusteringAnalysis(int[] inDatas, int width, int height, int[] interest, int noDataValue){		
		super(inDatas, width, height, interest, noDataValue);
	}
	
	public TabRookClusteringAnalysis(float[] inDatas, int width, int height, int[] interest, int noDataValue){	
		super(inDatas, width, height, interest, noDataValue);
	}
	
	@Override
	protected void doRun() {
		
		float[] tabCluster = new float[inDatas.length];
		
		float actual = 1;
		Map<Float, Float> sames = new HashMap<Float, Float>();
		
		float v, vi, vj;  
		float vci, vcj, vvci, vvcj;
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				
				v = (int) inDatas[j*width+i];
				if(v == noDataValue){
					nbNoDataValue++;
				}else if(interest.contains(v)){
					
					if(j>0 && i>0 && i<width-1){ // cas 1
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						
						vi = (int) inDatas[j*width + (i-1)];
						vj = (int) inDatas[(j-1)*width + i];
						
						if(vi == v && vi == vj && vci>0 && vcj>0){
							if(vci == vcj){
								tabCluster[j*width + i] = vcj;
							}else{
								tabCluster[j*width + i] = vcj;
																
								vvci = getSame(sames, vci);
								vvcj = getSame(sames, vcj);
								
								if(vvci < vvcj){
									sames.put(vvcj, vvci);
								}else if(vvcj < vvci){
									sames.put(vvci, vvcj);
								}
							}
						}else if(vi == v && vci>0){
							tabCluster[j*width + i] = vci;
						}else if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else{
							sames.put(actual, noDataValue);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(i>0 && j==0){ // cas 2
						vci = tabCluster[j*width + (i-1)];
						vi = (int) inDatas[j*width + (i-1)];
						if(vi == v && vci > 0){
							tabCluster[j*width + i] = vci;
						}else{
							sames.put(actual, noDataValue);
							tabCluster[j*width + i] = actual++;
						}
					}else if(j>0 && i==0){ // cas 3
						vcj = tabCluster[(j-1)*width + i];
						
						vj = (int) inDatas[(j-1)*width + i];
						
						if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else{
							sames.put(actual, noDataValue);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(j>0 && i==width-1){ // cas 4
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						
						vi = (int) inDatas[j*width + (i-1)];
						vj = (int) inDatas[(j-1)*width + i];
						
						if(vi == v && vi == vj && vci>0 && vcj>0){
							if(vci == vcj){
								tabCluster[j*width + i] = vcj;
							}else{
								tabCluster[j*width + i] = vcj;
																
								vvci = getSame(sames, vci);
								vvcj = getSame(sames, vcj);
								
								if(vvci < vvcj){
									sames.put(vvcj, vvci);
								}else if(vvcj < vvci){
									sames.put(vvci, vvcj);
								}
							}
						}else if(vi == v && vci>0){
							tabCluster[j*width + i] = vci;
						}else if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else{
							sames.put(actual, noDataValue);
							tabCluster[j*width + i] = actual++;
						}
							
					}else{ // cas 5
						sames.put(actual, noDataValue);
						tabCluster[j*width + i] = actual++;
					}
				}
			}
		}
		
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				v = tabCluster[j*width + i];
				if(v>0){
					tabCluster[j*width + i] = getSame(sames, v);
				}
			}
		}
		
		setResult(tabCluster);
	}

}
