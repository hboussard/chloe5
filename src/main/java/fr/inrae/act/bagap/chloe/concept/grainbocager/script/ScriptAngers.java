package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptAngers {

	public static void main(String[] args) {
		scriptAngersMetropoleTif();
		scriptAngersMetropoleAscii();
	}
	
	private static void scriptAngersMetropoleTif() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/temp/angers/data/communes_angers_metropole.shp");
		gbManager.setBufferArea(1000);
		gbManager.setBocage("H:/IGN/data/49_2020_5m/mean/");
		gbManager.setHauteurBoisement("H:/temp/angers/data/angers_metropole_hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/temp/angers/data/angers_metropole_type_boisement.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptAngersMetropoleAscii() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/temp/angers/data/communes_angers_metropole.shp");
		gbManager.setBufferArea(1000);
		gbManager.setBocage("H:/IGN/data/49_2020_5m/mean/");
		gbManager.setHauteurBoisement("H:/temp/angers/data/angers_metropole_hauteur_boisement.asc");
		gbManager.setTypeBoisement("H:/temp/angers/data/angers_metropole_type_boisement.asc");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}

}
