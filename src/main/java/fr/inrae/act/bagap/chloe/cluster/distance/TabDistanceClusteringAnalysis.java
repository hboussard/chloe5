package fr.inrae.act.bagap.chloe.cluster.distance;

import java.util.HashSet;
import java.util.Set;
import fr.inrae.act.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;

public class TabDistanceClusteringAnalysis extends Analysis {

	private float[] inDatas, distanceDatas;
	
	private double distance;
	
	private int height, width;
	
	private Set<Float> interest;
	
	private int noDataValue;
	
	public TabDistanceClusteringAnalysis(float[] inDatas, float[] distanceDatas, int width, int height, int[] interest, double distance, int noDataValue){
		this.inDatas = inDatas;
		this.distanceDatas = distanceDatas;
		this.distance = distance;
		this.width = width;
		this.height = height;
		this.interest = new HashSet<Float>();
		for(int v : interest){
			this.interest.add((float) v);
		}
		this.noDataValue = noDataValue;
	}

	@Override
	protected void doRun() {
		setResult(overlay(queenClustering(classification())));
	}
	
	private float[] classification() {
		//System.out.println("classification "+name);
		
		float[] outDatas = new float[width * height];
		
		Pixel2PixelTabCalculation classif = new Pixel2PixelTabCalculation(outDatas, distanceDatas){
			@Override
			protected float doTreat(float[] values) {
				double vd = values[0];
				if(vd == noDataValue){
					return noDataValue;
				}
				if(vd >= 0 && vd <= (distance/2)){
					return 1;
				}
				return 0;
			}
		};
		classif.run();
		
		//CoverageManager.write("C:/Data/projet/gmb/data/temp_classif.tif", outDatas, entete);
			
		return outDatas;
		
	}

	private float[] queenClustering(float[] classifDatas) {
		//System.out.println("cluster "+name);
		
		TabQueenClusteringAnalysis qa = new TabQueenClusteringAnalysis(classifDatas, width, height, new int[]{1}, noDataValue);
		float[] tabCluster = (float[]) qa.allRun();
		
		//CoverageManager.write("C:/Data/projet/gmb/data/temp_cluster.tif", tabCluster, entete);
		
		return tabCluster;
	}
	
	private float[] overlay(float[] queenDatas){
		//System.out.println("overlay "+name);
		
		float[] outDatas = new float[width * height];
		
		Pixel2PixelTabCalculation overl = new Pixel2PixelTabCalculation(outDatas, inDatas, queenDatas){
			@Override
			protected float doTreat(float[] values) {
				float v = values[0];
				if(v == noDataValue){
					return noDataValue;
				}
				if(interest.contains(v)){
					return values[1];
				}
				return 0;
			}
		};
		overl.run();
		
		return outDatas;
	}

	@Override
	protected void doInit() {
		// do nothing
	}

	@Override
	protected void doClose() {
		inDatas = null;
		distanceDatas = null;
	}
	
	
}
