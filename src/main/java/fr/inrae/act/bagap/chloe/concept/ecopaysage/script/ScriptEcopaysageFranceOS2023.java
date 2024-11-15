package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysageFranceOS2023 {
	
	public static void main(String[] args) {
		
		//int[] echelles = new int[] {1000};
		int[] echelles = new int[] {100};
		ecolandscape(echelles);
		/*
		int[] scales;
		for(int e1 : echelles) {
			//scales = new int[] {e1};
			//ecolandscape(scales);
			
			for(int e2 : echelles) {
				if(e1 < e2) {
					scales = new int[] {e1, e2};
					ecolandscape(scales);
				}
			}	
		}
		*/
	}

	private static void ecolandscape(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18 ,19, 20, 21, 22, 23};
		int[] unfilters = new int[] {255};
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("D:/sig/france/OS_2023/");
		epManager.setCodes(codes);
		epManager.setUnfilters(unfilters);
		epManager.setNoDataValue(-1);
		epManager.setScales(scales);
		epManager.setFactor(10);
		epManager.setOutputFolder("D:/sig/france/OS_2023/ecopaysage/");
		epManager.setClasses(new int[]{5, 10 ,15, 20, 25, 30, 35, 40, 45, 50});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
