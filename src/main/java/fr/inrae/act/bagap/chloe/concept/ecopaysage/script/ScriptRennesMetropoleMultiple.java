package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptRennesMetropoleMultiple {

	public static void main(String[] args) {
		
		int[] scales = new int[] {3000}; // m
		ecolandscape(scales);
		
	}
	
	private static void ecolandscape(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("gradient");
		epManager.addInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		epManager.setScales(scales);
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie6/");
		epManager.setClasses(new int[]{8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
