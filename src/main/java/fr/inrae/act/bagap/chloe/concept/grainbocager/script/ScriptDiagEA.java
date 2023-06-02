package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.farm.DiagnosticGrainBocagerExploitationBuilder;

public class ScriptDiagEA {

	public static void main(String[] args) {
		
		scriptExploitation("035177652", 5);
		scriptExploitation("035177652", 10);
		scriptExploitation("035177652", 50);
		
	}

	private static void scriptExploitation(String exploitation, int outCellSize) {

		long begin = System.currentTimeMillis();
		
		DiagnosticGrainBocagerExploitationBuilder builder = new DiagnosticGrainBocagerExploitationBuilder();
		
		builder.setOutputPath("G:/FDCCA/diag_ea/data/test/output_"+outCellSize+"m/");
		builder.setBocage("H:/temp/tile_coverage/mean/");
		builder.setParcellaire("G:/FDCCA/diag_ea/data/test/data/exploitation.shp");
		builder.setZoneBocageExploitation("G:/FDCCA/diag_ea/data/test/data/bocage_exploitation.shp");
		builder.setReplantationBocagere("G:/FDCCA/diag_ea/data/test/data/replantations.shp");
		builder.setCodeEA(exploitation);
		builder.addScenario("existant");
		builder.addScenario("scenario1");
		builder.setOutCellSize(outCellSize);
		DiagnosticGrainBocagerExploitation diagEA = builder.build();
		
		diagEA.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}

}
