package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class ScriptDistance {

	public static void main(String[] args) {
		
		distance();
	}

	private static void distance() {
	
		String path = "C:\\Data\\temp\\lena\\test/";
		
		Coverage covHabitat = CoverageManager.getCoverage(path+"habitat_clean.tif");
		EnteteRaster entete = covHabitat.getEntete();
		float[] dataHabitat = covHabitat.getData();
		covHabitat.dispose();
		

		Coverage covPermeabilite = CoverageManager.getCoverage("C:/Data/projet/DIRO/data/permeabilites/permeabilite_window_17.tif");
		float[] dataPermeabilite = covPermeabilite.getData();
		covPermeabilite.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write(path+"continuite_distance.tif", data, entete);
		
	}
	
}
