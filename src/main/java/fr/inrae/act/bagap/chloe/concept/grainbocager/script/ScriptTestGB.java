package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptTestGB {

	public static void main(String[] args) {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		
		gbManager.setBocage("D:/grain_bocager/data/31/2022/31_2022_hauteur_boisement.tif");
		gbManager.setOutputFolder("D:/grain_bocager/formation/31/sortie2/");
		gbManager.setEnvelope("{580249.4521;580590.7311;6251992.1032;6252236.7309}");
		gbManager.setBufferArea(1000.0);
		gbManager.setOuputPrefix("test2");
	
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}

}
