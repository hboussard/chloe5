package fr.inrae.act.bagap.chloe.cluster.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.TileCoverage;

public class TileQueenClusteringAnalysis extends Analysis {

	private String outputFolder;
	
	private TileCoverage inCoverage;
	
	private int noDataValue;
	
	private int[] interest;
	
	public TileQueenClusteringAnalysis(String outputFolder, TileCoverage inCoverage, int[] interest, int noDataValue){
		this.outputFolder = outputFolder;
		this.inCoverage = inCoverage;
		this.interest = interest;
		this.noDataValue = noDataValue;
	}
	
	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRun() {
		
		Coverage localCoverage;
		Coverage localClusterCoverage;
		float[] localData;
		float[] localClusterData;
		EnteteRaster localEntete;
		
		TabQueenClusteringAnalysis clustering;
		
		System.out.println("calcul des clusters par tuile (independamment)");
		// calcul des clusters par tuile (independamment)
		int ncols = inCoverage.ncols();
		int nrows = inCoverage.nrows();
		int width = inCoverage.tileWidth();
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				localCoverage = inCoverage.getCoverage(i, j);
				if(localCoverage != null){
					localData = localCoverage.getData();
					localEntete = localCoverage.getEntete();
					//localCoverage.dispose();
					
					clustering = new TabQueenClusteringAnalysis(localData, width, width, interest, noDataValue);
					localClusterData = (float[]) clustering.allRun();
					
					CoverageManager.writeGeotiff(outputFolder+"cluster_"+((int) localEntete.minx()/1000)+"_"+((int) localEntete.maxy()/1000)+".tif", localClusterData, localEntete);
				}
			}
		}
		
		
		TileCoverage covInitCluster = (TileCoverage) CoverageManager.getCoverage(outputFolder);
		
		System.out.println("calcul des correspondances");
		// calcul des correspondances
		Map<Integer, Map<Integer, Map<Integer, Set<Integer>>>> correspondances = new HashMap<Integer, Map<Integer, Map<Integer, Set<Integer>>>>();
		
		int v, vo;
		int numTile;
		Map<Integer, Set<Integer>> cluster, clustero;
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				localClusterCoverage = covInitCluster.getCoverage(i, j);
				if(localClusterCoverage != null){
					//localClusterCoverage = localCoverage.getEntete();
					//localCoverage.dispose();
					
					numTile = j*ncols + i;
					
					//localClusterCoverage = CoverageManager.getCoverage(outputFolder+"cluster_"+((int) localEntete.minx()/1000)+"_"+((int) localEntete.maxy()/1000)+".tif");
					localClusterData = localClusterCoverage.getData();
					//localClusterCoverage.dispose();
					
					
					correspondances.put(numTile, new HashMap<Integer, Map<Integer, Set<Integer>>>());
					for(float vc : localClusterData){
						if(vc > 0){
							if(!correspondances.get(numTile).containsKey((int) vc)){
								correspondances.get(numTile).put((int) vc, new HashMap<Integer, Set<Integer>>());
								correspondances.get(numTile).get((int) vc).put(numTile, new HashSet<Integer>());
								correspondances.get(numTile).get((int) vc).get(numTile).add((int) vc);
							}
						}
					}
					
					// gestion de la tuile nord-ouest (specific queen)
					v = (int) localClusterData[0];
					if(v > 0){
						localCoverage = covInitCluster.getCoverage(i-1, j-1);
						if(localCoverage != null){
							localData = localCoverage.getData();
							//localCoverage.dispose();
							vo = (int) localData[(width*width)-1];
							if(vo > 0){
								
								cluster = correspondances.get(j*ncols + i).get(v);
								clustero = correspondances.get((j-1)*ncols + (i-1)).get(vo);
								
								if(cluster != clustero){
									for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
										if(!cluster.containsKey(entry.getKey())){
											cluster.put(entry.getKey(), new HashSet<Integer>());
										}
										cluster.get(entry.getKey()).addAll(entry.getValue());
									}
									
									for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
										for(int vvo : entry.getValue()){
											correspondances.get(entry.getKey()).put(vvo, cluster);
										}	
									}
								}
							}
						}
					}
					
					// gestion de la tuile nord
					localCoverage = covInitCluster.getCoverage(i, j-1);
					if(localCoverage != null){
						
						//System.out.println("test de correspondance de la tuile ("+i+","+j+") avec la tuile ("+i+","+(j-1)+")");
						
						localData = localCoverage.getData();
						//localCoverage.dispose();
						
						for(int x=0; x<width; x++){
							v = (int) localClusterData[x];
							if(v > 0){
								if(x > 0){
									vo = (int) localData[(width-1)*width + (x-1)];
									if(vo > 0){
										
										cluster = correspondances.get(j*ncols + i).get(v);
										clustero = correspondances.get((j-1)*ncols + i).get(vo);
										
										if(cluster != clustero){
											for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
												if(!cluster.containsKey(entry.getKey())){
													cluster.put(entry.getKey(), new HashSet<Integer>());
												}
												cluster.get(entry.getKey()).addAll(entry.getValue());
											}
											
											for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
												for(int vvo : entry.getValue()){
													correspondances.get(entry.getKey()).put(vvo, cluster);
												}	
											}
										}
									}
								}
								
								vo = (int) localData[(width-1)*width + x];
								if(vo > 0){
									
									//System.out.println("tuile de ref, valeur "+v+" en "+x+" contre "+vo+" en "+((width-1)*width + x));
									
									cluster = correspondances.get(j*ncols + i).get(v);
									clustero = correspondances.get((j-1)*ncols + i).get(vo);
									
									if(cluster != clustero){
										for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
											if(!cluster.containsKey(entry.getKey())){
												cluster.put(entry.getKey(), new HashSet<Integer>());
											}
											cluster.get(entry.getKey()).addAll(entry.getValue());
										}
										
										for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
											for(int vvo : entry.getValue()){
												correspondances.get(entry.getKey()).put(vvo, cluster);
											}	
										}
									}
								}
								
								if(x<(width-1)){
									vo = (int) localData[(width-1)*width + (x+1)];
									if(vo > 0){
										
										cluster = correspondances.get(j*ncols + i).get(v);
										clustero = correspondances.get((j-1)*ncols + i).get(vo);
										
										if(cluster != clustero){
											for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
												if(!cluster.containsKey(entry.getKey())){
													cluster.put(entry.getKey(), new HashSet<Integer>());
												}
												cluster.get(entry.getKey()).addAll(entry.getValue());
											}
											
											for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
												for(int vvo : entry.getValue()){
													correspondances.get(entry.getKey()).put(vvo, cluster);
												}	
											}
										}
									}
								}
							}
						}
					}
					
					// gestion de la tuile nord-est (specific queen)
					v = (int) localClusterData[width-1];
					if(v > 0){
						localCoverage = covInitCluster.getCoverage(i+1, j-1);
						if(localCoverage != null){
							localData = localCoverage.getData();
							//localCoverage.dispose();
							vo = (int) localData[(width-1)*width];
							if(vo > 0){
								
								cluster = correspondances.get(j*ncols + i).get(v);
								clustero = correspondances.get((j-1)*ncols + (i+1)).get(vo);
								
								if(cluster != clustero){
									for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
										if(!cluster.containsKey(entry.getKey())){
											cluster.put(entry.getKey(), new HashSet<Integer>());
										}
										cluster.get(entry.getKey()).addAll(entry.getValue());
									}
									
									for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
										for(int vvo : entry.getValue()){
											correspondances.get(entry.getKey()).put(vvo, cluster);
										}	
									}
								}
							}
						}
					}
					
					// gestion de la tuile ouest
					localCoverage = covInitCluster.getCoverage(i-1, j);
					if(localCoverage != null){
						localData = localCoverage.getData();
						//localCoverage.dispose();
						
						for(int y=0; y<width*width; y+=width){
							v = (int) localClusterData[y];
							if(v > 0){
								if(y > 0){
									vo = (int) localData[y-width + (width-1)];
									if(vo > 0){
										
										cluster = correspondances.get(j*ncols + i).get(v);
										clustero = correspondances.get(j*ncols + (i-1)).get(vo);
										
										if(cluster != clustero){
											for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
												if(!cluster.containsKey(entry.getKey())){
													cluster.put(entry.getKey(), new HashSet<Integer>());
												}
												cluster.get(entry.getKey()).addAll(entry.getValue());
											}
											
											for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
												for(int vvo : entry.getValue()){
													correspondances.get(entry.getKey()).put(vvo, cluster);
												}	
											}
										}
									}
								}
								
								vo = (int) localData[y + (width-1)];
								if(vo > 0){
									
									cluster = correspondances.get(j*ncols + i).get(v);
									clustero = correspondances.get(j*ncols + (i-1)).get(vo);
									
									if(cluster != clustero){
										for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
											if(!cluster.containsKey(entry.getKey())){
												cluster.put(entry.getKey(), new HashSet<Integer>());
											}
											cluster.get(entry.getKey()).addAll(entry.getValue());
										}
										
										for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
											for(int vvo : entry.getValue()){
												correspondances.get(entry.getKey()).put(vvo, cluster);
											}	
										}
									}
								}
								
								if(y<((width-1)*width)){
									vo = (int) localData[y+width + (width-1)];
									if(vo > 0){
										
										cluster = correspondances.get(j*ncols + i).get(v);
										clustero = correspondances.get(j*ncols + (i-1)).get(vo);
										
										if(cluster != clustero){
											for(Entry<Integer, Set<Integer>> entry : clustero.entrySet()){
												if(!cluster.containsKey(entry.getKey())){
													cluster.put(entry.getKey(), new HashSet<Integer>());
												}
												cluster.get(entry.getKey()).addAll(entry.getValue());
											}
											
											for(Entry<Integer, Set<Integer>> entry : cluster.entrySet()){
												for(int vvo : entry.getValue()){
													correspondances.get(entry.getKey()).put(vvo, cluster);
												}	
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println("mise en place de la numerotation globale");
		// mise en place de la numerotation globale
		//Map<Integer, Map<Integer, Map<Integer, Set<Integer>>>> correspondances = new HashMap<Integer, Map<Integer, Map<Integer, Set<Integer>>>>();
		
		Set<Map<Integer, Set<Integer>>> singleClusters = new HashSet<Map<Integer, Set<Integer>>>();
		for(int nt : correspondances.keySet()){
			for(int nc : correspondances.get(nt).keySet()){
				cluster = correspondances.get(nt).get(nc);
				singleClusters.add(cluster);
			}
		}
		Map<Integer, Map<Float, Float>> numerotation = new HashMap<Integer, Map<Float, Float>>();
		for(int nt : correspondances.keySet()){
			numerotation.put(nt, new HashMap<Float, Float>());
		}
		correspondances = null;
		int index = 1;
		for(Map<Integer, Set<Integer>> singleCluster : singleClusters){
			for(Entry<Integer, Set<Integer>> entry : singleCluster.entrySet()){
				for(int ev : entry.getValue()){
					numerotation.get(entry.getKey()).put((float) ev, (float) index);
				}
			}
			index++;
		}
		singleClusters = null;
		
		System.out.println("nombre de clusters totaux = "+(index+1));
		
		System.out.println("reaffectation des identifications");
		// reaffectation des identifications
		SearchAndReplacePixel2PixelTabCalculation pptcc;
		for(int j=0; j<nrows; j++){
			for(int i=0; i<ncols; i++){
				localClusterCoverage = covInitCluster.getCoverage(i, j);
				if(localClusterCoverage != null){
					localEntete = localClusterCoverage.getEntete();
					//localCoverage.dispose();
					
					numTile = j*ncols + i;
					
					localClusterData = localClusterCoverage.getData();
					//localClusterCoverage.dispose();
					
					localData = new float[width*width];
					
					pptcc = new SearchAndReplacePixel2PixelTabCalculation(localData, localClusterData, numerotation.get(numTile));
					pptcc.run();
					
					CoverageManager.writeGeotiff(outputFolder+"cluster_"+((int) localEntete.minx()/1000)+"_"+((int) localEntete.maxy()/1000)+".tif", localData, localEntete);
				}
			}
		}
		
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}

}
