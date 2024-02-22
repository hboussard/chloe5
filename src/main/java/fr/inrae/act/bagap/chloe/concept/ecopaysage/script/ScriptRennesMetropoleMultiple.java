package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptRennesMetropoleMultiple {

	public static void main(String[] args) {
		
		int[] scales = new int[] {500, 2000}; // m
		ecolandscape(scales);
		
	}
	
	private static void ecolandscape(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		epManager.setScales(scales);
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie4/");
		epManager.setClasses(new int[]{2,3,4,5,6,7,8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
