package fr.inrae.act.bagap.chloe.cluster;

import java.io.IOException;
import java.util.Set;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.chess.TabRookClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.util.analysis.ChloeUtilAnalysis;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ClusterAnalysis extends ChloeUtilAnalysis {

	private String outputRaster;
	
	private String outputCsv;
	
	private String inputRaster;
	
	private String inputDistance;
	
	private ClusterType clusterType;
	
	private int[] clusterSources;
	
	private double dMax;
	
	private float[] inData;
	
	private float[] distanceData;
	
	private float[] outData;
	
	private EnteteRaster entete;
	
	private CsvClusteringOutput outClustering;
	
	public ClusterAnalysis(String outputRaster, String outputCsv, String inputRaster, String inputDistance, ClusterType clusterType, Set<Integer> clusterSources, double dMax){
		this.outputRaster = outputRaster;
		this.outputCsv = outputCsv;
		this.inputRaster = inputRaster;
		this.inputDistance = inputDistance;
		this.clusterType = clusterType;
		this.clusterSources = new int[clusterSources.size()];
		int index = 0;
		for(Integer s : clusterSources){
			this.clusterSources[index++] = s;
		}
		this.dMax = dMax;
	}
	
	@Override
	protected void doInit() {
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		entete = cov.getEntete();
		inData = cov.getData();
		cov.dispose();
		
		if(clusterType == ClusterType.DISTANCE){
			Coverage covD = CoverageManager.getCoverage(inputDistance);
			distanceData = covD.getData();
			covD.dispose();
		}
		
		//outData = new float[entete.width()*entete.height()];
	}

	@Override
	protected void doRun() {

		if(clusterType == ClusterType.QUEEN){
			
			TabQueenClusteringAnalysis analysis = new TabQueenClusteringAnalysis(inData, entete.width(), entete.height(), clusterSources, entete.noDataValue());
			outData = (float[]) analysis.allRun();
			
		}else if(clusterType == ClusterType.ROOK){
			
			TabRookClusteringAnalysis analysis = new TabRookClusteringAnalysis(inData, entete.width(), entete.height(), clusterSources, entete.noDataValue());
			outData = (float[]) analysis.allRun();
			
		}else if(clusterType == ClusterType.DISTANCE){
			
			TabDistanceClusteringAnalysis analysis = new TabDistanceClusteringAnalysis(inData, distanceData, entete.width(), entete.height(), clusterSources, dMax, entete.noDataValue());
			outData = (float[]) analysis.allRun();
		}
		
	}

	@Override
	protected void doClose() {
		
		if(outputRaster != null){
			CoverageManager.write(outputRaster, outData, entete); // export sur fichier raster
		}
		
		if(outputCsv != null){
			outClustering = new CsvClusteringOutput(outputCsv, outData, inData, clusterSources, entete.cellsize(), entete.noDataValue());
			outClustering.allRun();
		}
		
		inputRaster = null;
		inputDistance = null;
		outputRaster = null;
		outputCsv = null;
		entete = null;
		clusterSources = null;
		inData = null;
		distanceData = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}
	
	private void writeCsvClustering(String outputCsv){
		try {
			CsvWriter cw = new CsvWriter(outputCsv);
			cw.setDelimiter(';');
			cw.write("");
			cw.endRecord();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
