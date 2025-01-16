package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysagesNDVITemporal {

	public static void main(String[] args) {
		
		int[] echelles = new int[] {100, 250, 500};
		//int[] echelles = new int[] {100, 500};
		int[] scales;
		for(int e1 : echelles) {
			scales = new int[] {e1};
			ecolandscapeTemporel(scales);
		}
	}
	
	private static void ecolandscapeTemporel(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6};
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		File folder = new File("C:/Data/temp/jacques/CARTES_CLASS_ndvi/CARTES_CLASS_ndvi_clean/");
		for(String file : folder.list()) {
			if(file.endsWith(".tif")) {
				epManager.addInputRaster("C:/Data/temp/jacques/CARTES_CLASS_ndvi/CARTES_CLASS_ndvi_clean/"+file);	
			}
		}
		epManager.setCodes(codes);
		epManager.setScales(scales);
		epManager.setFactor(1);
		epManager.setDisplacement(10);
		epManager.setOutputFolder("C:/Data/temp/jacques/CARTES_CLASS_ndvi/ecopaysages_temporels/");
		epManager.setClasses(new int[]{5, 6, 7, 8, 9, 10});
		//epManager.setClasses(new int[]{5});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
