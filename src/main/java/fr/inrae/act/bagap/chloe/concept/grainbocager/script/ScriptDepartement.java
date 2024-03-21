package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;

public class ScriptDepartement {

	public static void main(String[] args){
		
		scriptDepartement("hauteur/ille_et_vilaine/", "35", "2020");
		scriptDepartement("hauteur/mayenne/", "53", "2019");
		
	}
		
	private static void scriptDepartement(String outputPath, String codeDept, String annee) {
		
		Util.createAccess("H:/IGN/"+outputPath);
		
		GrainBocagerManager gbManager = new GrainBocagerManager("recuperation_hauteur_boisement");
		
		gbManager.setBocage("H:/IGN/data/"+codeDept+"_"+annee+"_5m/mean/");
		gbManager.setWoodHeight("H:/IGN/"+outputPath+"hauteur_boisement_"+codeDept+"_"+annee+"_5m.asc");
	
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
