package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcolandscapesCelia {
	
	private static String path = "C:/Data/projet/motiver/Celia/data/";
	
	public static void main(String[] args) {
	
		ecolandscape();
	
	}
	
	private static void ecolandscape() {
		
		EcoPaysageManager epManager = new EcoPaysageManager("membership");
		//epManager.setForce(true);
		epManager.addInputRaster(path+"crop_inventory_rc_2015_AOI_mod.tif");
		epManager.setCodes(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});
		epManager.setScales(new int[]{4000});
		epManager.setClasses(new int[]{7});
		epManager.setDisplacement(3);
		epManager.setUnfilters(new int[]{-1});
		epManager.setOutputFolder(path+"ecopaysages2/");
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
