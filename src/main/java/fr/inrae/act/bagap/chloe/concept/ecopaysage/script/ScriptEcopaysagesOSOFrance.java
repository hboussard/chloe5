package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysagesOSOFrance {

	public static void main(String[] args) {
		
		//int[] echelles = new int[] {1000};
		int[] echelles = new int[] {1000, 5000};
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
	}

	private static void ecolandscape(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18 ,19, 20, 21, 22, 23};
		int[] unfilters = new int[] {0};
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("D:/sig/oso_thiea/OCS_2021.tif");
		epManager.setCodes(codes);
		epManager.setUnfilters(unfilters);
		epManager.setNoDataValue(-1);
		epManager.setScales(scales);
		epManager.setFactor(15);
		epManager.setOutputFolder("D:/sig/oso_thiea/ecopaysage/");
		epManager.setClasses(new int[]{20,25,30});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}

	
}
