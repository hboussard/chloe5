package fr.inrae.act.bagap.chloe.concept.occupationsol.script;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.GeoPackage2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptCapHaie {

	private static String pathCosia = "E:/data/cosia/";

	private static String pathRPG = "E:/data/rpg/";
	private static String pathRPGVect = pathRPG+"data/france/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01.7z/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01/RPG/1_DONNEES_LIVRAISON_2024/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01/RPG_Parcelles.gpkg";
	
	private static String pathTheia = "E:/data/theia/";
	
	private static String pathOccSol = "E:/data/caphaie/occsol/";
	
	private static String pathEcolandscape = "E:/data/caphaie/ecolandscape/";
	
	public static void main(String[] args) {
		
		//test();
		
		//scriptDepartements();
		
		//compileOccsolFrance();
		compileOccsolBretagne();
		
		//ecolandscapeFrance(10, 1000);
		//ecolandscapeFrance(15, 1000);
		//ecolandscapeFrance(20, 1000);
		//ecolandscapeFrance(10, 5000);
		//ecolandscapeFrance(15, 5000);
		//ecolandscapeFrance(20, 5000);
		
		//ecolandscapeFrance(25, 1000, 5000);
	}
	
	private static void scriptDepartements() {

		Map<String, String[]> departements = new TreeMap<String, String[]>();
		
		String codeDep, yearCosia, yearRPG, yearGB, yearTheia;
		
		yearRPG = "2024";
		yearTheia = "2023";
		/*
		// 01
		codeDep = "01"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 02
		codeDep = "02"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 03
		codeDep = "03"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 04 
		codeDep = "04"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 05 
		codeDep = "05"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 06 
		codeDep = "06"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 07
		codeDep = "07"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 08 
		codeDep = "08"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 09 
		codeDep = "09"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 10
		codeDep = "10"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 11
		codeDep = "11"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 12
		codeDep = "12"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 13
		codeDep = "13"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 14
		codeDep = "14"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 15
		codeDep = "15"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 16
		codeDep = "16"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 17 
		codeDep = "17"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 18 
		codeDep = "18"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 19
		codeDep = "19"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 2A
		codeDep = "2A"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 2B
		codeDep = "2B"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 21
		codeDep = "21"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 22
		codeDep = "22"; yearCosia = "2021"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 23
		codeDep = "23"; yearCosia = "2024"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 24
		codeDep = "24"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 25 
		codeDep = "25"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 26 
		codeDep = "26"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 27
		codeDep = "27"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 28 
		codeDep = "28"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 29
		codeDep = "29"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 30
		codeDep = "30"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 31 
		codeDep = "31"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 32 
		codeDep = "32"; yearCosia = "2025"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 33
		codeDep = "33"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 34
		codeDep = "34"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 35
		codeDep = "35"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 36 
		codeDep = "36"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 37 
		codeDep = "37"; yearCosia = "2021"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 38
		codeDep = "38"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 39
		codeDep = "39"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 40
		codeDep = "40"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 41
		codeDep = "41"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 42
		codeDep = "42"; yearCosia = "2025"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 43 
		codeDep = "43"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 44
		codeDep = "44"; yearCosia = "2025"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 45 
		codeDep = "45"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 46 
		codeDep = "46"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 47
		codeDep = "47"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 48
		codeDep = "48"; yearCosia = "2021"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 49
		codeDep = "49"; yearCosia = "2025"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 50 
		codeDep = "50"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 51
		codeDep = "51"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 52
		codeDep = "52"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 53 
		codeDep = "53"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 54
		codeDep = "54"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 55
		codeDep = "55"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 56
		codeDep = "56"; yearCosia = "2024"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 57
		codeDep = "57"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 58
		codeDep = "58"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 59 
		codeDep = "59"; yearCosia = "2025"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 60
		codeDep = "60"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 61 
		codeDep = "61"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 62
		codeDep = "62"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 63 
		codeDep = "63"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 64
		codeDep = "64"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 65
		codeDep = "65"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 66
		codeDep = "66"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 67 
		codeDep = "67"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 68 
		codeDep = "68"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 69
		codeDep = "69"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 70 
		codeDep = "70"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 71
		codeDep = "71"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 72 
		codeDep = "72"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 73
		codeDep = "73"; yearCosia = "2024"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 74 
		codeDep = "74"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 75
		codeDep = "75"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 76 
		codeDep = "76"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 77
		codeDep = "77"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 78 
		codeDep = "78"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 79 
		codeDep = "79"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 80
		codeDep = "80"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 81 
		codeDep = "81"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 82 
		codeDep = "82"; yearCosia = "2025"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 83 
		codeDep = "83"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 84
		codeDep = "84"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 85 
		codeDep = "85"; yearCosia = "2022"; yearGB = "2022";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 86
		codeDep = "86"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 87 
		codeDep = "87"; yearCosia = "2024"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 88
		codeDep = "88"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		
		// 89
		codeDep = "89"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 90
		codeDep = "90"; yearCosia = "2023"; yearGB = "2023";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 91 
		codeDep = "91"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 92
		codeDep = "92"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 93 
		codeDep = "93"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 94
		codeDep = "94"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
				
		// 95 
		codeDep = "95"; yearCosia = "2024"; yearGB = "2021";
		departements.put(codeDep, new String[] {yearCosia, yearRPG, yearGB, yearTheia});
		*/
		for(Entry<String, String[]> departement : departements.entrySet()) {
			
			codeDep = departement.getKey(); 
			yearCosia = departement.getValue()[0]; 
			yearGB = departement.getValue()[2];
			
			//rasteurizeDepartement(codeDep, yearCosia, yearRPG, yearGB, yearTheia);
			ecolandscapeDepartement(codeDep, yearCosia, yearRPG, yearGB, yearTheia, 1000, 10);
		}
		
		//rasteurizeDepartement(codeDep, yearCosia, yearRPG, yearGB, yearTheia);
		//ecolandscapeDepartement(codeDep, yearCosia, yearRPG, yearGB, yearTheia, 1000, 10);
	}

	private static void ecolandscapeFrance(int k, int... sizes) {
		
		String francePath = "E:/data/caphaie/occsol/raster/france/";
		
		String completeName = "";
		for(int s : sizes) {
			completeName += "_"+s+"m";
		}
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.setForce(true);
		epManager.addInputRaster(francePath);
		//epManager.setScales(new int[]{size});
		epManager.setScales(sizes);
		//epManager.setOutputFolder(pathEcolandscape+"ecolandscape_france_"+size+"m/");
		epManager.setOutputFolder(pathEcolandscape+"ecolandscape_config_france"+completeName+"/");
		epManager.setClasses(new int[]{k});
		epManager.setUnfilters(new int[] {-1});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38});
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void compileOccsolBretagne() {
		
		String depPath = "E:/data/caphaie/occsol/raster/departement/";
		File folder = new File(depPath);
		
		String francePath = "E:/data/caphaie/occsol/raster/bretagne/";
		Util.createAccess(francePath);
		
		String name;
		for(File fo : folder.listFiles()) {
			if(fo.isDirectory()) {
				
				String dep = fo.getName().split("_")[1].replace("D0", "");
				if(dep.equalsIgnoreCase("22") 
						|| dep.equalsIgnoreCase("29") 
						|| dep.equalsIgnoreCase("35")
						|| dep.equalsIgnoreCase("56")) {
					
					for(String f : fo.list()) {
						
						System.out.println(depPath+fo.getName()+"/"+f);
						
						String[] fs = f.replace(".tif", "").split("_");
						String minx = fs[2];
						String maxy = fs[3];
				
						name = "occsol_"+minx+"_"+maxy+".tif";
						
						if(new File(francePath+name).exists()) {
							
							Coverage cov1 = CoverageManager.getCoverage(depPath+fo.getName()+"/"+f);
							EnteteRaster entete = cov1.getEntete();
							float[] data1 = cov1.getData();
							cov1.dispose();
							
							Coverage cov2 = CoverageManager.getCoverage(francePath+name);
							float[] data2 = cov2.getData();
							cov2.dispose();
							

							Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data1, data1, data2) {
								@Override
								protected float doTreat(float[] v) {
									
									if(v[0] != -1) {
										
										if(v[0] != 38) { // ce n'est pas la mer 
											
											return v[0];
										}
										
										if(v[1] != -1) {
											
											return v[1];
										}
										
										return 38;
									}
									
									if(v[1] != -1) {
										
										return v[1];
									}
									
									return -1;

									
								}
							};
							pptc.run();
							
							CoverageManager.write(francePath+name, data1, entete);
							
						}else {
							
							Tool.copy(depPath+fo.getName()+"/"+f, francePath+name);
						}
					}
				}
			}
		}
		
	}
	
	private static void compileOccsolFrance() {
		
		String depPath = "E:/data/caphaie/occsol/raster/departement/";
		File folder = new File(depPath);
		
		String francePath = "E:/data/caphaie/occsol/raster/france/";
		Util.createAccess(francePath);
		
		String name;
		for(File fo : folder.listFiles()) {
			if(fo.isDirectory()) {
				
				for(String f : fo.list()) {
					
					System.out.println(depPath+fo.getName()+"/"+f);
					
					String[] fs = f.replace(".tif", "").split("_");
					String minx = fs[2];
					String maxy = fs[3];
			
					name = "occsol_"+minx+"_"+maxy+".tif";
					
					if(new File(francePath+name).exists()) {
						
						Coverage cov1 = CoverageManager.getCoverage(depPath+fo.getName()+"/"+f);
						EnteteRaster entete = cov1.getEntete();
						float[] data1 = cov1.getData();
						cov1.dispose();
						
						Coverage cov2 = CoverageManager.getCoverage(francePath+name);
						float[] data2 = cov2.getData();
						cov2.dispose();
						

						Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data1, data1, data2) {
							@Override
							protected float doTreat(float[] v) {
								
								if(v[0] != -1) {
									
									if(v[0] != 38) { // ce n'est pas la mer 
										
										return v[0];
									}
									
									if(v[1] != -1) {
										
										return v[1];
									}
									
									return 38;
								}
								
								if(v[1] != -1) {
									
									return v[1];
								}
								
								return -1;

								
							}
						};
						pptc.run();
						
						CoverageManager.write(francePath+name, data1, entete);
						
					}else {
						
						Tool.copy(depPath+fo.getName()+"/"+f, francePath+name);
					}
				}
			}
		}
		
	}
	
	private static void test() {
		
		//String path = "E:/data/caphaie/occsol/raster/departement/OCCSOL_D022_cosia_2021_rpg_2024_gb_2021_theia_2023/";
		//String path = "E:/data/caphaie/occsol/tile/departement/OCCSOL_D022_cosia_2021_rpg_2024_gb_2021_theia_2023/";
		String path = pathCosia+"test/departement/COSIA_D022_2021/";
		
		/*
		Coverage cov1 = CoverageManager.getCoverage(path+"occsol_D022_210_6860.tif");
		EnteteRaster entete1 = cov1.getEntete();		
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(path+"occsol_D022_210_6870.tif");
		EnteteRaster entete2 = cov2.getEntete();		
		cov2.dispose();
		
		System.out.println(entete1);
		System.out.println(entete2);
		*/
		/*
		Coverage cov3 = CoverageManager.getCoverage(path);
		EnteteRaster entete3 = cov3.getEntete();
		float[] data3 = cov3.getData(); 
		cov3.dispose();
	
		
		System.out.println();
		System.out.println(entete3);
		*/
		//CoverageManager.write("E:/data/caphaie/occsol/raster/departement/test.tif", data3, entete3);
		 
		/*
		String path = "E:/data/cosia/data/departement/COSIA_1-0__GPKG_LAMB93_D022_2021-01-01/COSIA_1-0__GPKG_LAMB93_D022_2021-01-01/";
		//String tile1  = "D022_2021_270_6790_vecto.gpkg";
		//String tile2  = "D022_2021_270_6800_vecto.gpkg";
		String tile3  = "D022_2021_280_6790_vecto.gpkg";
		//String tile4  = "D022_2021_280_6800_vecto.gpkg";
		
		String outputPath = "E:/data/cosia/test/";
		//GeoPackage2CoverageConverter.rasterize(outputPath+tile1.replace("_vecto.gpkg", ".tif"), path+tile1, "numero", -1, 5, -1, null);
		//GeoPackage2CoverageConverter.rasterize(outputPath+tile2.replace("_vecto.gpkg", ".tif"), path+tile2, "numero", -1, 5, -1, null);
		//GeoPackage2CoverageConverter.rasterize(outputPath+tile3.replace("_vecto.gpkg", ".tif"), path+tile3, "numero", -1, 5, -1, null);
		//GeoPackage2CoverageConverter.rasterize(outputPath+tile4.replace("_vecto.gpkg", ".tif"), path+tile4, "numero", -1, 5, -1, null);
		*/
		/*
		Coverage covRef = CoverageManager.getCoverage(pathCosia+"raster/departement/COSIA_D057_2022/cosia_D057_2022_930_6950.tif");
		String rasterGB = "D:/grain_bocager/data/57/2022/57_2022_type_boisement.tif";
		Coverage covGB = CoverageManager.getCoverage(rasterGB);
		
		//System.out.println(covRef.getEntete());
		//System.out.println(covRef.getEntete().getEnvelope());
		//System.out.println(covGB.getEntete());
		
		Rectangle rec = EnteteRaster.getROI(covGB.getEntete(), covRef.getEntete().getEnvelope());
		//System.out.println(rec);
		
		float[] data = covGB.getData(EnteteRaster.getROI(covGB.getEntete(), covRef.getEntete().getEnvelope()));
		
		covRef.dispose();
		covGB.dispose();
		*/
		
		String depPathCosia = pathCosia+"raster/departement/";
		File folder = new File(depPathCosia);
		for(File f : folder.listFiles()) {
			
			String dep = f.getName().split("_")[1].replace("D0", "");
			System.out.println(dep);
			
			
		}
		
		/*
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathRPGRaster = pathRPG+"raster/departement/RPG_D0"+codeDep+"_"+yearRPG+"/";
		Util.createAccess(depPathRPGRaster);
		
		Map<String, EnteteRaster> entetes = new HashMap<String, EnteteRaster>();
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("cosia", "rpg").replace(yearCosia, yearRPG);
			
				Coverage cov = CoverageManager.getCoverage(depPathCosiaRaster+f);
				EnteteRaster entete = cov.getEntete();
				//float[] data = cov.getData();
				cov.dispose();

				entetes.put(depPathRPGRaster+name, entete);
			}	
		}	
		
		GeoPackage2CoverageConverter.rasterize(pathRPGVect, "code_group", -1, entetes);
		*/
	}
	
	private static void ecolandscapeDepartement(String codeDep, String yearCosia, String yearRPG, String yearGB, String yearTheia, int size, int k) {
		
		String depPathOccSol = pathOccSol+"raster/departement/OCCSOL_D0"+codeDep+"_cosia_"+yearCosia+"_rpg_"+yearRPG+"_gb_"+yearGB+"_theia_"+yearTheia+"/";
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.setForce(true);
		epManager.addInputRaster(depPathOccSol);
		epManager.setScales(new int[]{size});
		epManager.setOutputFolder(pathEcolandscape+"ecolandscape_D0"+codeDep+"_cosia_"+yearCosia+"_rpg_"+yearRPG+"_gb_"+yearGB+"_theia_"+yearTheia+"_"+size+"m/");
		epManager.setClasses(new int[]{k});
		epManager.setUnfilters(new int[] {-1});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38});
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void rasteurizeDepartement(String codeDep, String yearCosia, String yearRPG, String yearGB, String yearTheia) {
		
		rasterisationCosia(codeDep, yearCosia);
		
		rasterisationRPG(codeDep, yearCosia, yearRPG);
		
		rasteurisationTheia(codeDep, yearCosia, yearTheia);
				
		rasteurisationElementsBoises(codeDep, yearCosia, yearGB);
				
		compilationOccSol(codeDep, yearCosia, yearRPG, yearGB, yearTheia);
	}
	
	private static void compilationOccSol(String codeDep, String yearCosia, String yearRPG, String yearGB, String yearTheia) {
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathRPGRaster = pathRPG+"raster/departement/RPG_D0"+codeDep+"_"+yearRPG+"/";
		String depPathElementsBoises = pathOccSol+"elements_boises/departement/elts_boises_D0"+codeDep+"_"+yearGB+"/";
		String depPathTheiaRaster = pathTheia+"raster/departement/THEIA_D0"+codeDep+"_"+yearTheia+"/";
		
		String depPathOccSol = pathOccSol+"raster/departement/OCCSOL_D0"+codeDep+"_cosia_"+yearCosia+"_rpg_"+yearRPG+"_gb_"+yearGB+"_theia_"+yearTheia+"/";
		Util.createAccess(depPathOccSol);
		
		String name, nameRPG, nameGB, nameTheia;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				
				name = f.replace("cosia", "occsol").replace("_"+yearCosia, "");
				nameRPG = f.replace("cosia", "rpg").replace(yearCosia, yearRPG);
				nameGB = f.replace("cosia", "type_boisement").replace(yearCosia, yearGB);
				nameTheia = f.replace("cosia", "theia").replace(yearCosia, yearTheia);
		
				Coverage covCosia = CoverageManager.getCoverage(depPathCosiaRaster+f);
				EnteteRaster entete = covCosia.getEntete();
				float[] dataCosia = covCosia.getData();
				covCosia.dispose();
				
				Coverage covRPG = CoverageManager.getCoverage(depPathRPGRaster+nameRPG);
				float[] dataRPG = covRPG.getData();
				covRPG.dispose();
				
				Coverage covGB = CoverageManager.getCoverage(depPathElementsBoises+nameGB);
				float[] dataGB = covGB.getData();
				covGB.dispose();
				
				Coverage covTheia = CoverageManager.getCoverage(depPathTheiaRaster+nameTheia);
				float[] dataTheia = covTheia.getData();
				covTheia.dispose();
				
				float[] data = new float[entete.width()*entete.height()];
				
				Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataCosia, dataRPG, dataGB, dataTheia) {
					@Override
					protected float doTreat(float[] v) {
						
						if(v[0] != -1) { // cosia existant
							
							if(v[0] == 1) { // batiment
								return 29;
							}
							if(v[0] == 2) { // zone permeable
								return 35;
							}
							if(v[0] == 3) { // zone impermeable
								return 34;
							}
							if(v[0] == 4) { // piscine
								return 31;
							}
							if(v[0] == 5) { // sol nu
								return 27;
							}
							if(v[0] == 6) { // surface eau
								return 33;
							}
							if(v[0] == 7) { // neige
								return 36;
							}
							if(v[0] == 10) { // broussaille
								return 26;
							}
							if(v[0] == 11) { // vigne
								return 21;
							}
							if(v[0] == 12) { // pelouse
								return 30;
							}
							if(v[0] == 15) { // serre
								return 32;
							}
							if(v[0] == 16) { // autre
								return 37;
							}
							
							if(v[0] == 8 || v[0] == 9) { // conifere ou feuillu
								
								// analyse GB
								if(v[2] == 1) { // arbre isole
									return 12;
								}
								if(v[2] == 10) { // haie
									return 10;
								}
								if(v[2] == 5) { // massif boise
									return 13;
								}
								
								return 26; // broussaille
							}
							
							if(v[0] == 13 || v[0] == 14) { // culture ou terre labouree
								
								if(v[1] != -1) { // rpg existant
									
									if(v[1] == 1) { // ble
										return 1;
									}
									if(v[1] == 2) { // mais
										return 2;
									}
									if(v[1] == 3) { // orge
										return 3;
									}
									if(v[1] == 4) { //autre cereale
										return 4;
									}
									if(v[1] == 5) { // colza
										return 5;
									}
									if(v[1] == 6) { // tournesol
										return 6;
									}
									if(v[1] == 7) { // autres oleagineux
										return 7;
									}
									if(v[1] == 8) { // protéagineux
										return 8;
									}
									if(v[1] == 9) { // plante a fibre
										return 9;
									}
									if(v[1] == 11) { // gel
										return 11;
									}
									if(v[1] == 14) { // riz
										return 14;
									}
									if(v[1] == 15) { // legumineuse a grain
										return 15;
									}
									if(v[1] == 16) { // fourrage
										return 16;
									}
									if(v[1] == 17) { // estive et lande
										return 17;
									}
									if(v[1] == 18) { // prairie permanente
										return 18;
									}
									if(v[1] == 19) { // prairie temporaire
										return 19;
									}
									if(v[1] == 20) { // verger
										return 20;
									}
									if(v[1] == 21) { // vigne
										return 21;
									}
									if(v[1] == 22) { // fruit a coque
										return 22;
									}
									if(v[1] == 23) { // olivier
										return 23;
									}
									if(v[1] == 24) { // autre culture industrielle
										return 24;
									}
									if(v[1] == 25) { // legume ou fleur
										return 25;
									}
									if(v[1] == 28) { // divers culture
										return 28;
									}
								}
								
								// analyse Theia
								
								if(v[3] == 13) { // prairie permanente
									return 18;
								}
								return 28; // divers culture
							}	
						}
						
						if(v[3] == 23) { // mer
							return 38;
						}
						
						return -1;
					}
				};
				pptc.run();
				
				CoverageManager.write(depPathOccSol+name, data, entete);
			}
		}
		
	}	
	
	private static void rasteurisationElementsBoises(String codeDep, String yearCosia, String yearGrainBocager) {
		
		decoupageGB(codeDep, yearCosia, yearGrainBocager);
		
		floutageGB(codeDep, yearGrainBocager);
		
		superposeBoisements(codeDep, yearCosia, yearGrainBocager);
	}
	
	private static void superposeBoisements(String codeDep, String yearCosia, String yearGB) {
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathSlidingGrainBocager = pathOccSol+"sliding_grain_bocager/departement/GB_D0"+codeDep+"_"+yearGB+"/";
		
		String depPathElementsBoises = pathOccSol+"elements_boises/departement/elts_boises_D0"+codeDep+"_"+yearGB+"/";
		Util.createAccess(depPathElementsBoises);
		
		String name, nameGB;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("cosia", "type_boisement").replace(yearCosia, yearGB);
		
				nameGB = f.replace("cosia", "type_boisement").replace(yearCosia, yearGB);
				
				Coverage covCosia = CoverageManager.getCoverage(depPathCosiaRaster+f);
				EnteteRaster entete = covCosia.getEntete();
				float[] dataCosia = covCosia.getData();
				covCosia.dispose();
				
				Coverage covGB = CoverageManager.getCoverage(depPathSlidingGrainBocager+nameGB);
				float[] dataGB = covGB.getData();
				covGB.dispose();
				
				float[] data = new float[entete.width()*entete.height()];
				
				Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataCosia, dataGB) {
					@Override
					protected float doTreat(float[] v) {
						
						if(v[0] == 8 || v[0] == 9) { // coniferes ou feuillus
							
							if(v[1] != -1) {
								
								return v[1];
							}
							return 0;
						}
						
						return -1;
					}
				};
				pptc.run();
				
				CoverageManager.write(depPathElementsBoises+name, data, entete);
			}
		}
	}
	
	private static void floutageGB(String codeDep, String yearGB) {
		
		String depPathGrainBocager = pathOccSol+"grain_bocager/departement/GB_D0"+codeDep+"_"+yearGB+"/";
		File folder = new File(depPathGrainBocager);
		
		String depPathSlidingGrainBocager = pathOccSol+"sliding_grain_bocager/departement/GB_D0"+codeDep+"_"+yearGB+"/";
		Util.createAccess(depPathSlidingGrainBocager);
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("sliding_type_boisement", "type_boisement");
		
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
				builder.addRasterFile(depPathGrainBocager+f);
				builder.addMetric("Majority");
				builder.addWindowSize(7);
				builder.addGeoTiffOutput("Majority", depPathSlidingGrainBocager+name);
				LandscapeMetricAnalysis analysis = builder.build();
				analysis.allRun();
			}
		}
	}
	
	private static void decoupageGB(String codeDep, String yearCosia, String yearGB) {
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathGrainBocager = pathOccSol+"grain_bocager/departement/GB_D0"+codeDep+"_"+yearGB+"/";
		Util.createAccess(depPathGrainBocager);
		
		String rasterGB = "D:/grain_bocager/data/"+codeDep+"/"+yearGB+"/"+codeDep+"_"+yearGB+"_type_boisement.tif";
		Coverage covGB = CoverageManager.getCoverage(rasterGB);
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("cosia", "type_boisement").replace(yearCosia, yearGB);
	
				Coverage covRef = CoverageManager.getCoverage(depPathCosiaRaster+f);
				
				float[] data = covGB.getData(EnteteRaster.getROI(covGB.getEntete(), covRef.getEntete().getEnvelope()));
				
				CoverageManager.write(depPathGrainBocager+name, data, covRef.getEntete());
			}
		}
		
		covGB.dispose();
	}
	
	private static void rasteurisationTheia(String codeDep, String yearCosia, String yearTheia) {
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathTheiaRaster = pathTheia+"raster/departement/THEIA_D0"+codeDep+"_"+yearTheia+"/";
		Util.createAccess(depPathTheiaRaster);
		
		String rasterTheia = "D:/sig/oso_thiea/OCS_2023.tif";
		Coverage covTheia = CoverageManager.getCoverage(rasterTheia);
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("cosia", "theia").replace(yearCosia, yearTheia);
				
				Coverage covRef = CoverageManager.getCoverage(depPathCosiaRaster+f);
				
				Coverage cov = Util.extendAndFill(covTheia, covRef, 2);
				
				covRef.dispose();
				
				CoverageManager.write(depPathTheiaRaster+name, cov.getData(), cov.getEntete());
			}
		}
		
		covTheia.dispose();
	}
	
	private static void rasterisationRPG(String codeDep, String yearCosia, String yearRPG) {
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+yearCosia+"/";
		File folder = new File(depPathCosiaRaster);
		
		String depPathRPGRaster = pathRPG+"raster/departement/RPG_D0"+codeDep+"_"+yearRPG+"/";
		Util.createAccess(depPathRPGRaster);
		
		Map<String, EnteteRaster> entetes = new HashMap<String, EnteteRaster>();
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				name = f.replace("cosia", "rpg").replace(yearCosia, yearRPG);
			
				Coverage cov = CoverageManager.getCoverage(depPathCosiaRaster+f);
				EnteteRaster entete = cov.getEntete();
				//float[] data = cov.getData();
				cov.dispose();

				entetes.put(depPathRPGRaster+name, entete);
			}	
		}	
		
		GeoPackage2CoverageConverter.rasterize(pathRPGVect, "code_group", -1, entetes);
	}
	
	private static void rasterisationCosia(String codeDep, String year) {
		
		String depPathCosiaVect = pathCosia+"data/departement/COSIA_1-0__GPKG_LAMB93_D0"+codeDep+"_"+year+"-01-01/COSIA_1-0__GPKG_LAMB93_D0"+codeDep+"_"+year+"-01-01/";
		File folder = new File(depPathCosiaVect);
		
		String depPathCosiaRaster = pathCosia+"raster/departement/COSIA_D0"+codeDep+"_"+year+"/";
		Util.createAccess(depPathCosiaRaster);
		
		int width = 2000;
		int height = 2000;
		float cellSize = 5.0f;
		int noDataValue = -1;
		
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".gpkg")) {
				System.out.println(f);
				name = f.replace("_vecto.gpkg", ".tif");
				
				String[] fs = f.split("_");
				double minx = Integer.parseInt(fs[2]) * 1000.0;
				double maxx = minx + (cellSize * width);
				double maxy = Integer.parseInt(fs[3]) * 1000.0;
				double miny = maxy - (cellSize * height);
				
				EnteteRaster entete = new EnteteRaster(width, height, minx, maxx, miny, maxy, cellSize, noDataValue);  
				GeoPackage2CoverageConverter.rasterize(depPathCosiaRaster+"cosia_"+name, depPathCosiaVect+f, "numero", -1, entete);
				
				//GeoPackage2CoverageConverter.rasterize(depPathCosiaRaster+"cosia_"+name, depPathCosiaVect+f, "numero", -1, 5, -1, null);
			}
		}
	}
	
	/*
	 * private static void cleanOccsol(String codeDep, String yearCosia, String yearRPG, String yearGB, String yearTheia) {
		
		String depPathOccSol = pathOccSol+"raster/departement/OCCSOL_D0"+codeDep+"_cosia_"+yearCosia+"_rpg_"+yearRPG+"_gb_"+yearGB+"_theia_"+yearTheia+"/";
		File folder = new File(depPathOccSol);
		
		String depPathTileOccSol = pathOccSol+"tile/departement/OCCSOL_D0"+codeDep+"_cosia_"+yearCosia+"_rpg_"+yearRPG+"_gb_"+yearGB+"_theia_"+yearTheia+"/";
		Util.createAccess(depPathTileOccSol);
		
		// ex : 2000 2000 210000.0 220000.0 6860000.0 6870000.0 5.0 -1 EPSG:2154
		//2000 2000 320000.0 330000.0 6830000.0 6840000.0 5.0 -1 EPSG:2154
		//2000 2001 320000.0 330000.0 6840000.0 6850005.0 5.0 -1 EPSG:2154
		//2000 2000 330000.0 340000.0 6810001.699999997 6820001.699999997 5.0 -1 EPSG:2154
		
		int width = 2000;
		int height = 2000;
		float cellSize = 5.0f;
		int noDataValue = -1;
		
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				
				System.out.println();
				System.out.println(f);
				
				String[] fs = f.replace(".tif", "").split("_");
				double minx = Integer.parseInt(fs[2]) * 1000.0;
				double maxx = minx + (cellSize * width);
				double maxy = Integer.parseInt(fs[3]) * 1000.0;
				double miny = maxy - (cellSize * height);
				
				Coverage cov = CoverageManager.getCoverage(depPathOccSol+f);
				EnteteRaster entete = cov.getEntete();
				
				System.out.println(entete);
				
				if(entete.width() == width && entete.height() == height) {
					
					float[] data = cov.getData();
					cov.dispose();
					
					EnteteRaster enteteBis = new EnteteRaster(width, height, minx, maxx, miny, maxy, cellSize, noDataValue);
					
					System.out.println("bis 1 "+enteteBis);
					
					CoverageManager.write(depPathTileOccSol+f, data, enteteBis);	
					
				}else {
					
					Envelope env = new Envelope(minx, maxx, miny, maxy);
					float[] data = cov.getData(EnteteRaster.getROI(entete, env));
					cov.dispose();
					
					int index = 0;
					for(float d : data) {
						if(d == 0) {
							data[index] = noDataValue;
						}
						index++;
					}
					
					EnteteRaster enteteBis = new EnteteRaster(width, height, minx, maxx, miny, maxy, cellSize, noDataValue);
					//EnteteRaster enteteBis = EnteteRaster.getEntete(entete, env);
					
					System.out.println("bis 2 "+enteteBis);
					
					CoverageManager.write(depPathTileOccSol+f, data, enteteBis);					
				}			
			}
		}
	}
	 */
	
}
