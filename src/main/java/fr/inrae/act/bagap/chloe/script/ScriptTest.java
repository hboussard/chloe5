package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptTest {

	public static void main(String[] args){
		
		readLZW();
		read();
		
	}
	
	private static void convert(){
		Coverage cov = CoverageManager.getCoverage("D:/grain_bocager/data/01/2021/01_2021_distance_influence.tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		
		CoverageManager.write("D:/temp/grain_bocager/distance.tif", data, entete);
	}
	
	private static void read(){
		
		long begin = System.currentTimeMillis();
		
		Coverage cov = CoverageManager.getCoverage("D:/grain_bocager/data/01/2021/01_2021_distance_influence.tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void readLZW(){
		
		long begin = System.currentTimeMillis();
		
		Coverage cov = CoverageManager.getCoverage("D:/temp/grain_bocager/distance.tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
