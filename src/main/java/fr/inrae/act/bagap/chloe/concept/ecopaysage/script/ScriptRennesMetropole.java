package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;
import java.io.IOException;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class ScriptRennesMetropole {

	public static void main(String[] args) {
		
		//int scale = 500; // m
		//calculMetrics(radius);
		//normalizeDataPerGroup(radius);
		//clustering(radius);
		//mapping(radius);
		
		int scale = 3000; // m
		ecolandscape(scale);
		
		
	}

	private static void calculMetrics(int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("calcul_metrics");
		epManager.setInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		//epManager.setCodes(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13}); // code d'occupation du sol
		//epManager.setScale(scale);
		//epManager.setMetricsFile("E:/rennes_metropole/ecopaysage/essaie2/analyse/csv_metrics_"+scale+"m.csv");
		epManager.addMetricsFile(scale, "E:/rennes_metropole/ecopaysage/essaie2/analyse/csv_metrics_"+scale+"m.csv");
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
		
	}
	
	private static void standardizationPerGroup(int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("standardization");
		epManager.setCodes(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13}); // code d'occupation du sol
		epManager.addMetricsFile(scale, "E:/rennes_metropole/ecopaysage/essaie2/analyse/csv_metrics_"+scale+"m.csv");
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie2/");
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
		
	}
	
	private static void clustering(int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("clustering");
		epManager.setCodes(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13}); // code d'occupation du sol
		epManager.addMetricsFile(scale, "E:/rennes_metropole/ecopaysage/essaie2/analyse/csv_metrics_"+scale+"m.csv");
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie2/");
		epManager.setClasses(new int[]{2,3,4,5,6,7,8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
		
	}
	
	private static void mapping(int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setCodes(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13}); // code d'occupation du sol
		epManager.addMetricsFile(scale, "E:/rennes_metropole/ecopaysage/essaie2/analyse/csv_metrics_"+scale+"m.csv");
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie2/");
		epManager.setClasses(new int[]{2,3,4,5,6,7,8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
		
	}
	
	private static void ecolandscape(int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		epManager.setScale(scale);
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/scale_"+scale+"m/");
		epManager.setClasses(new int[]{2,3,4,5,6,7,8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscape(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		epManager.setScales(scales);
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/scales/");
		epManager.setClasses(new int[]{2,3,4,5,6,7,8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
