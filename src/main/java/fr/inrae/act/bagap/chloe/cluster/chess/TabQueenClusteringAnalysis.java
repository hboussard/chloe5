package fr.inrae.act.bagap.chloe.cluster.chess;

import java.util.HashMap;
import java.util.Map;

public class TabQueenClusteringAnalysis extends TabChessClusteringAnalysis {

	public TabQueenClusteringAnalysis(int[] inDatas, int width, int height, int[] interest, int noDataValue){		
		super(inDatas, width, height, interest, noDataValue);
	}
	
	public TabQueenClusteringAnalysis(float[] inDatas, int width, int height, int[] interest, int noDataValue){	
		super(inDatas, width, height, interest, noDataValue);
	}
	
	@Override
	protected void doRun() {
		
		float[] tabCluster = new float[inDatas.length];
		
		float actual = 1;
		Map<Float, Float> sames = new HashMap<Float, Float>();
		
		float v, vi, vj, vg, vd;  
		float vci, vcj, vcg, vcd, vvci, vvcj, vvcd, vvcg;
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				
				v = (int) inDatas[j*width+i];
				if(v == noDataValue){
					
					nbNoDataValue++;
					tabCluster[j*width+i] = noDataValue;
					
				}else if(interest.contains(v)){
					
					if(j>0 && i>0 && i<width-1){ // cas 1
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						vcg = tabCluster[(j-1)*width + (i-1)];
						vcd = tabCluster[(j-1)*width + (i+1)];
						
						vi = (int) inDatas[j*width + (i-1)];
						vj = (int) inDatas[(j-1)*width + i];
						vg = (int) inDatas[(j-1)*width + (i-1)];
						vd = (int) inDatas[(j-1)*width + (i+1)];
						
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
						}else if(vi == v && vi == vd && vci>0 && vcd>0){
							if(vci == vcd){
								tabCluster[j*width + i] = vcd;
							}else{
								tabCluster[j*width + i] = vcd;
																
								vvci = getSame(sames, vci);
								vvcd = getSame(sames, vcd);
								
								if(vvci < vvcd){
									sames.put(vvcd, vvci);
								}else if(vvcd < vvci){
									sames.put(vvci, vvcd);
								}
							}
							
						}else if(vg == v && vg == vd && vcg>0 && vcd>0){
							if(vcg == vcd){
								tabCluster[j*width + i] = vcg;
							}else{
								tabCluster[j*width + i] = vcg;
																
								vvcg = getSame(sames, vcg);
								vvcd = getSame(sames, vcd);
								
								if(vvcg < vvcd){
									sames.put(vvcd, vvcg);
								}else if(vvcd < vvcg){
									sames.put(vvcg, vvcd);
								}
							}
						}else if(vi == v && vci>0){
							tabCluster[j*width + i] = vci;
						}else if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else if(vg == v && vcg>0){
							tabCluster[j*width + i] = vcg;
						}else if(vd == v && vcd>0){
							tabCluster[j*width + i] = vcd;
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
						vcd = tabCluster[(j-1)*width + (i+1)];
						
						vj = (int) inDatas[(j-1)*width + i];
						vd = (int) inDatas[(j-1)*width + (i+1)];
						
						if(vj == v && vcj>0){
							tabCluster[j*width + i] = vcj;
						}else if(vd == v && vcd >0){
							tabCluster[j*width + i] = vcd;
						}else{
							sames.put(actual, noDataValue);
							tabCluster[j*width + i] = actual++;
						}
						
					}else if(j>0 && i==width-1){ // cas 4
						vci = tabCluster[j*width + (i-1)];
						vcj = tabCluster[(j-1)*width + i];
						vcg = tabCluster[(j-1)*width + (i-1)];
						
						vi = (int) inDatas[j*width + (i-1)];
						vj = (int) inDatas[(j-1)*width + i];
						vg = (int) inDatas[(j-1)*width + (i-1)];
						
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
						}else if(vg == v && vcg>0){
							tabCluster[j*width + i] = vcg;
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
