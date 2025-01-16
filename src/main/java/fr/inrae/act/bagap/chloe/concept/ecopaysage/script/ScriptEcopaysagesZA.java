package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysagesZA {

	public static void main(String[] args) {
		ecolandscape();
	}
	
	private static void ecolandscape() {
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2016.tif");
		epManager.setScales(new int[]{1000});
		epManager.setOutputFolder("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecopaysage3/");
		epManager.setClasses(new int[]{3,4,5,6,7,8,9,10});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
