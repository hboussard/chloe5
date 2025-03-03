package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationManager;

public class ScriptDiagEA {

	public static void main(String[] args) {
		
		//scriptExploitation("035177652", 5);
		//scriptExploitation("035177652", 10);
		//scriptExploitation("035177652", 50);
		
	}
	
	private static void scriptExploitation(String exploitation, int outCellSize) {

		long begin = System.currentTimeMillis();
		
		DiagnosticGrainBocagerExploitationManager manager = new DiagnosticGrainBocagerExploitationManager();
		
		manager.setOutputFolder("G:/FDCCA/diag_ea/data/test/output10/");
		manager.setBocage("H:/temp/tile_coverage/mean/");
		manager.setParcellaire("G:/FDCCA/diag_ea/data/test/data/exploitation.shp");
		//builder.setZoneBocageExploitation("G:/FDCCA/diag_ea/data/test/data/bocage_exploitation.shp");
		manager.setCodeEA(exploitation);
		manager.setGrainBocagerCellSize(outCellSize);
		DiagnosticGrainBocagerExploitation diagEA = manager.build();
		
		diagEA.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}

}
