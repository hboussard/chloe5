package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.awt.Rectangle;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptMotiver3 {

	private static String pathMotiver = "E:/projet/motiver/dynamic/";
	
	public static void main(String[] args) {
		
		//retile(2000);
		//retile(2010);
		//retile(2019);
		//ecolandscape(2000);
		//ecolandscape(2010);
		//ecolandscape(2019);
		//ecolandscape();
	}

	private static void retile(int year) {
		
		Coverage covOS = CoverageManager.getCoverage(pathMotiver+"occsol/clc_france_"+year+"_sea28.tif");
		EnteteRaster entete = covOS.getEntete();		
		Rectangle ROI = new Rectangle(10000, 10000, 1000, 1000);
		EnteteRaster localEntete = EnteteRaster.getEntete(entete, ROI);
		float[] dataOS = covOS.getData(ROI);
		covOS.dispose();
		
		CoverageManager.write(pathMotiver+"occsol2/mini_clc_france_"+year+"_sea28.tif", dataOS, localEntete);
	}
	
	private static void ecolandscape() {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster(pathMotiver+"occsol2/mini_clc_france_2000_sea28.tif");
		epManager.addInputRaster(pathMotiver+"occsol2/mini_clc_france_2010_sea28.tif");
		epManager.addInputRaster(pathMotiver+"occsol2/mini_clc_france_2019_sea28.tif");
		epManager.setScale(300);
		epManager.setDisplacement(7);
		epManager.setOutputFolder(pathMotiver+"ecolandscapes/dynamic/");
		epManager.setClasses(new int[] {15});
		epManager.setUnfilters(new int[] {255});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28});
		epManager.setCompositionMetrics();
		epManager.setConfigurationMetrics();
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscape(int year) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster(pathMotiver+"occsol2/mini_clc_france_"+year+"_sea28.tif");
		epManager.setScale(300);
		epManager.setDisplacement(7);
		epManager.setOutputFolder(pathMotiver+"ecolandscapes/"+year+"/");
		epManager.setClasses(new int[] {15});
		epManager.setUnfilters(new int[] {255});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28});
		epManager.setCompositionMetrics();
		epManager.setConfigurationMetrics();
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
