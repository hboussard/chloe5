package fr.inrae.act.bagap.chloe.script;

import java.util.prefs.Preferences;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptTest {

	public static void main(String[] args){
		
		int v = 4/4;
		
		System.out.println(v);
		
		/*
		for(float v = 0; v<=1; v+=0.1) {
			
			System.out.println(v+" "+Math.log(v));
		}
		
		System.out.println(0+" "+Math.log(0)+" "+0*Math.log(0));
		System.out.println(0.1+" "+Math.log(0.1)+" "+0.1*Math.log(0.1));
		System.out.println(0.2+" "+Math.log(0.2)+" "+0.2*Math.log(0.2));
		System.out.println(0.3+" "+Math.log(0.3)+" "+0.3*Math.log(0.3));
		System.out.println(0.4+" "+Math.log(0.4)+" "+0.4*Math.log(0.4));
		System.out.println(0.5+" "+Math.log(0.5)+" "+0.5*Math.log(0.5));
		System.out.println(0.6+" "+Math.log(0.6)+" "+0.6*Math.log(0.6));
		System.out.println(0.7+" "+Math.log(0.7)+" "+0.7*Math.log(0.7));
		System.out.println(0.8+" "+Math.log(0.8)+" "+0.8*Math.log(0.8));
		System.out.println(0.9+" "+Math.log(0.9)+" "+0.9*Math.log(0.9));
		System.out.println(1+" "+Math.log(1)+" "+1*Math.log(1));
		*/
		
		//readLZW();
		//read();
		
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
