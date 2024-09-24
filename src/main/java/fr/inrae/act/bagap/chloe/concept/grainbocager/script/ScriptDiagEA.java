package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationBuilder;

public class ScriptDiagEA {

	public static void main(String[] args) {
		
		//scriptExploitation("035177652", 5);
		//scriptExploitation("035177652", 10);
		//scriptExploitation("035177652", 50);
		
		/*
		System.out.println(f(0));
		System.out.println(f(0.1));
		System.out.println(f(0.2));
		System.out.println(f(0.3));
		System.out.println(f(0.4));
		System.out.println(f(0.5));
		System.out.println(f(0.6));
		System.out.println(f(0.7));
		System.out.println(f(0.8));
		System.out.println(f(0.9));
		System.out.println(f(1));
		*/
	}
	
	public static double f(double v){
		return Math.log10(9*v+1);
	}
	/*
	private static void scriptExploitation(String exploitation, int outCellSize) {

		long begin = System.currentTimeMillis();
		
		DiagnosticGrainBocagerExploitationBuilder builder = new DiagnosticGrainBocagerExploitationBuilder();
		
		builder.setOutputPath("G:/FDCCA/diag_ea/data/test/output10/");
		builder.setBocage("H:/temp/tile_coverage/mean/");
		builder.setParcellaire("G:/FDCCA/diag_ea/data/test/data/exploitation.shp");
		builder.setZoneBocageExploitation("G:/FDCCA/diag_ea/data/test/data/bocage_exploitation.shp");
		builder.setCodeEA(exploitation);
		builder.setOutCellSize(outCellSize);
		DiagnosticGrainBocagerExploitation diagEA = builder.build();
		
		diagEA.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}*/

}
