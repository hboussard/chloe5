package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.util.Util;

public class ScriptEcopaysageFranceOS2023 {
	
	public static void main(String[] args) {
		
		
		//cleanOSO2023();
		
		//int[] echelles = new int[] {500};
		int[] echelles = new int[] {500, 3000};
		//ecolandscape(echelles);
		int[] scales;
		for(int e1 : echelles) {
			scales = new int[] {e1};
			//ecolandscapeTest3(scales);
			
			for(int e2 : echelles) {
				if(e1 < e2) {
					scales = new int[] {e1, e2};
					ecolandscapeOSBocage2(scales);
				}
			}	
		}
		
	}
	
	private static void cleanOSO2023() {
		String path = "D:/sig/france/OS_2023/";
		Util.createAccess("D:/sig/france/OS_2023_clean/");
		File folder = new File(path);
		for(String file : folder.list()) {
			if(file.endsWith(".tif")) {
				System.out.println(file);
				Coverage cov = CoverageManager.getCoverage(path+file);
				float[] data = cov.getData();
				EnteteRaster entete = cov.getEntete();
				entete.setNoDataValue(-1);
				cov.dispose();
				for(int i=0; i<data.length; i++) {
					if(data[i] == 255) {
						data[i] = -1;
					}
				}
				CoverageManager.write("D:/sig/france/OS_2023_clean/"+file, data, entete);
			}
		}
	}

	private static void ecolandscape(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18 ,19, 20, 21, 22, 23};
		int[] unfilters = new int[] {-1};
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.addInputRaster("D:/sig/france/OS_2023_clean/");
		epManager.setCodes(codes);
		epManager.setUnfilters(unfilters);
		//epManager.setNoDataValue(-1);
		epManager.setScales(scales);
		epManager.setFactor(15);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/france/ecopaysages/");
		//epManager.setClasses(new int[]{5, 10 ,15, 20, 25, 30});
		epManager.setClasses(new int[]{30});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeTest(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18 ,19, 20, 21, 22, 23};
		int[] unfilters = new int[] {-1};
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster("D:/sig/france/test/");
		epManager.setCodes(codes);
		epManager.setUnfilters(unfilters);
		epManager.setScales(scales);
		epManager.setFactor(5);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/test/test9/");
		epManager.setClasses(new int[]{5});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeTest2(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 10, 13 ,14, 15, 16, 17, 18 ,19, 20, 21, 22, 23};
		int[] unfilters = new int[] {-1};
		String importanceFile = "C:/Data/data/sig/test/test11/importance.txt"; 
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster("D:/sig/france/test/");
		epManager.setCodes(codes);
		epManager.setCodeImportanceFile(importanceFile);
		epManager.setUnfilters(unfilters);
		epManager.setScales(scales);
		epManager.setFactor(1);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/test/test11/");
		epManager.setClasses(new int[]{5, 6, 7});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeTest3(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 10};
		int[] unfilters = new int[] {-1, 0};
		//String importanceFile = "C:/Data/data/sig/test/test11/importance.txt"; 
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster("C:/Data/data/sig/france/OS_Bocage/test/");
		epManager.setCodes(codes);
		//epManager.setCodeImportanceFile(importanceFile);
		epManager.setUnfilters(unfilters);
		epManager.setScales(scales);
		epManager.setFactor(1);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/france/OS_Bocage/test/ecopaysages/test3/");
		epManager.setClasses(new int[]{5, 6, 7, 8, 9, 10});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeOSBocage(int[] scales) {
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 10};
		int[] unfilters = new int[] {-1, 0};
		String importanceFile = "C:/Data/data/sig/france/OS_Bocage/ecopaysages/essaie2/metric_importance.txt"; 
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster("C:/Data/data/sig/france/OS_Bocage/raster_5m_tuile_10x10/");
		epManager.setCodes(codes);
		epManager.setMetricImportanceFile(importanceFile);
		epManager.setUnfilters(unfilters);
		epManager.setScales(scales);
		epManager.setFactor(1);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/france/OS_Bocage/ecopaysages/essaie2/");
		//epManager.setClasses(new int[]{5, 6, 7, 8, 9, 10, 15, 20});
		epManager.setClasses(new int[]{16, 17, 18, 19});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeOSBocage2(int[] scales) {
		
		int[] codes = new int[] {1, 2, 4, 5, 6, 7, 10};
		int[] unfilters = new int[] {-1, 0};
		String importanceFile = "C:/Data/data/sig/france/OS_Bocage2/ecopaysages/essaie1/metric_importance.txt"; 
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster("C:/Data/data/sig/france/OS_Bocage2/raster_5m_tuile_10x10/");
		epManager.setCodes(codes);
		epManager.setMetricImportanceFile(importanceFile);
		epManager.setUnfilters(unfilters);
		epManager.setScales(scales);
		epManager.setFactor(1);
		epManager.setDisplacement(40);
		epManager.setOutputFolder("C:/Data/data/sig/france/OS_Bocage2/ecopaysages/essaie1/");
		epManager.setClasses(new int[]{5, 6, 7, 8, 9, 10, 15, 16, 17, 18, 19, 20});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
}
