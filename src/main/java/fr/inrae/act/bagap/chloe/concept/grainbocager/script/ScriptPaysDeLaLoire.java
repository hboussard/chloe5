package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.raster.Tile;

public class ScriptPaysDeLaLoire {

	public static void main(String[] args){
		
		//scriptRecuperationHauteurBoisement();
		scriptDetectionTypeBoisement();
		//scriptCalculDistanceInfluenceBoisement();
		//scriptCalculGrainBocager();
		//scriptEnsembleCalculGrainBocager();
	}

	
	private static void scriptDetectionTypeBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setHauteurBoisement("G:/FRC_Pays_de_la_Loire/data/mnhc/");
		gbManager.setOutputPath("G:/FRC_Pays_de_la_Loire/data/grain_bocager/");
		gbManager.setTypeBoisement("G:/FRC_Pays_de_la_Loire/data/grain_bocager/TypeBoisement/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
