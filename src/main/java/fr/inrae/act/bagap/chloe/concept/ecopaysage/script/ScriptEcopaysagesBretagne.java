package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysagesBretagne {

	public static void main(String[] args) {
		
		//int[] scales = new int[] {2500}; // m
		//ecolandscape(scales);
		
		/*
		int[] echelles = new int[] {500,1000,1500,2000,2500,3000};
		int[] scales;
		for(int e1 : echelles) {
			scales = new int[] {e1};
			ecolandscape(scales);
			for(int e2 : echelles) {
				if(e1 < e2) {
					scales = new int[] {e1, e2};
					ecolandscape(scales);
				}
			}	
		}*/
		
		int[] scales = new int[] {1000, 2500}; // m
		ecolandscape(scales);
		
	}
	
	private static void ecolandscape(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("D:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		epManager.setScales(scales);
		epManager.setOutputFolder("D:/data/sig/bretagne/ecopaysage/");
		epManager.setClasses(new int[]{10,11,12,13,14,15});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
