package fr.inrae.act.bagap.chloe.cluster;

import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.chess.TabRookClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptDistanceAndCluster {

	public static void main(String[] args) {
		
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
