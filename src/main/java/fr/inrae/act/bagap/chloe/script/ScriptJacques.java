package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptJacques {

	public static void main(String[] args) {
		
		//scriptCompile();
		
	}
	
	private static void scriptCompile(){
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/22_2018_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/22_2018_5m/mnhc_22_2018_5m.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/29_2018_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/29_2018_5m/mnhc_29_2018_5m.tif", data, entete);
		*/
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/56_2019_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/56_2019_5m/mnhc_56_2019_5m.tif", data, entete);
		
	}
	
	
}
