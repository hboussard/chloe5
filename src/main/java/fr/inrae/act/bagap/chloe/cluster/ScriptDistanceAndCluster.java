package fr.inrae.act.bagap.chloe.cluster;

import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.chess.TabRookClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptDistanceAndCluster {

	public static void main(String[] args) {
		
		script2();
	}
	
	private static void script3(){
		
		Coverage cov = CoverageManager.getCoverage("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/pf_2018_10m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		int[] codes =  new int[]{7,8};
		
		float[] dataDistance = new float[entete.width() * entete.height()];
		TabChamferDistanceAnalysis da = new TabChamferDistanceAnalysis(dataDistance, data, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), codes, entete.noDataValue());
		da.allRun();
		
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(data, dataDistance, entete.width(), entete.height(), codes, 10, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		
		CoverageManager.writeGeotiff("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/cluster/cluster_3.tif", dataCluster, entete);
		
		String csv = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/cluster/cluster_3.csv";
		CsvClusteringOutput csvCO = new CsvClusteringOutput(csv, dataCluster, data, codes, entete.cellsize(), entete.noDataValue());
		csvCO.allRun();
	}
	
	private static void script2(){
		
		Coverage cov = CoverageManager.getCoverage("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/pf_2018_10m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		int[] codes =  new int[]{7,8};
		
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), codes, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		
		CoverageManager.writeGeotiff("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/cluster/cluster_4.tif", dataCluster, entete);
	}
	
	private static void script1(){

		float[] tabCluster;
		
		Coverage cov = CoverageManager.getCoverage("H:/temp/test/ascii1.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		//TabRookClusteringAnalysis ca = new TabRookClusteringAnalysis(data, entete.width(), entete.height(), new int[]{7}, entete.noDataValue());
		//float[] tabCluster = (float[]) ca.allRun();
		
		//CoverageManager.writeGeotiff("H:/temp/test/clusterMais_rook.tif", tabCluster, entete);
		
		//TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), new int[]{7}, entete.noDataValue());
		//float[] tabCluster = (float[]) ca.allRun();
		
		//CoverageManager.writeGeotiff("H:/temp/test/clusterMais_queen.tif", tabCluster, entete);
		
		//Coverage covDistance = CoverageManager.getCoverage("H:/temp/test/distance_eucl_mais.asc");
		//float[] dataDistance = covDistance.getDatas();
		//covDistance.dispose();
		int[] codes =  new int[]{7};
		float[] dataDistance = new float[entete.width() * entete.height()];
		
		//TabChamferDistanceAnalysis da = new TabChamferDistanceAnalysis(dataDistance, data, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), codes);
		//da.allRun();
		
		//TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(data, dataDistance, entete.width(), entete.height(), codes, 150, entete.noDataValue());
		//tabCluster = (float[]) ca.allRun();
		//CoverageManager.writeGeotiff("H:/temp/test/clusterMais_eucl_150.tif", tabCluster, entete);
		
		//ca = new TabDistanceClusteringAnalysis(data, dataDistance, entete.width(), entete.height(), new int[]{7}, 200, entete.noDataValue());
		//tabCluster = (float[]) ca.allRun();
		//CoverageManager.writeGeotiff("H:/temp/test/clusterMais_eucl_200.tif", tabCluster, entete);
		
		//ca = new TabDistanceClusteringAnalysis(data, dataDistance, entete.width(), entete.height(), new int[]{7}, 300, entete.noDataValue());
		//tabCluster = (float[]) ca.allRun();
		//CoverageManager.writeGeotiff("H:/temp/test/clusterMais_eucl_300.tif", tabCluster, entete);
		
		Coverage covFriction = CoverageManager.getCoverage("H:/temp/test/friction.asc");
		float[] dataFriction = covFriction.getData();
		covFriction.dispose();
		
		TabRCMDistanceAnalysis da = new TabRCMDistanceAnalysis(dataDistance, data, dataFriction, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), codes);
		da.allRun();
		//CoverageManager.writeGeotiff("H:/temp/test/distance_friction.tif", dataDistance, entete);
	
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(data, dataDistance, entete.width(), entete.height(), codes, 150, entete.noDataValue());
		tabCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/temp/test/clusterMais_func_150.tif", tabCluster, entete);
	}

}
