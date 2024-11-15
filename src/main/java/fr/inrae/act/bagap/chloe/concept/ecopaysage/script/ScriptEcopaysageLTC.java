package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysageLTC {

	public static void main(String[] args) {
		ecolandscape();
	}
	
	private static void ecolandscape() {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setInputRaster("C:/Data/projet/motiver/data/LTC_2019_ebr_mer_22_clean.tif");
		epManager.setCodes(new int[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23}); // code d'occupation du sol
		epManager.setXYFile("C:/Data/projet/motiver/data/xy.csv");
		epManager.setScales(new int[]{1000});
		epManager.setOutputFolder("C:/Data/projet/motiver/data/ecopaysage/essaie5/");
		//epManager.setClasses(new int[]{3, 4, 5, 6, 7, 8, 9});
		epManager.setClasses(new int[]{7});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
