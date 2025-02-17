package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.GeoPackage2CoverageConverter;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;

public class ScriptCosia {

	public static void main(String[] args) {
		
		//generateCosiaRasters();
		//cleanDep37();
		
		//generateDepartementRasters(1, 20);
		//generateDepartementRasters(21, 40);
		//generateDepartementRasters(41, 60);
		//generateDepartementRasters(61, 80);
		//generateDepartementRasters(81, 90);
		//generateDepartementRasters(91, 100);
		
		//generateTuilesCosia();
		
		//generateBoisement();
		//generateTypeBoisement();
		//generateDistanceInfluence();
		//generateGrainBocager50m();
		//calculGrainBocagerZoneEnjeux(1);
		//calculGrainBocagerZoneEnjeux(5);
		
		//generateBoisement37();
		//generateTypeBoisement37();
		
		//cleanBoisement();
		
		//generateTuileCesbio();
		
		//generateRasterBDTopo();
		
		//generateTuileFranceBocage();
		//cleanTuileFranceBocage();
	}
	
	private static void cleanTuileFranceBocage() {
		
		String pathOSBocage = "C:/Data/data/sig/france/OS_Bocage/raster_5m_tuile_10x10/";
		String pathOSBocage2 = "C:/Data/data/sig/france/OS_Bocage2/raster_5m_tuile_10x10/";
		Util.createAccess(pathOSBocage2);
		
		File folder = new File(pathOSBocage);
		Coverage cov;
		EnteteRaster entete;
		float[] data;
		String[] s;
		String xTile, yTile;
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(2f, 2f); 
		sarMap.put(3f, 2f);
		Pixel2PixelTabCalculation cal;
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
		
				System.out.println(file);
				
				s = file.replace(".tif", "").split("_");
				xTile = s[2];
				yTile = s[3];
				
				cov = CoverageManager.getCoverage(pathOSBocage+"OS_Bocage_"+xTile+"_"+yTile+".tif");
				entete = cov.getEntete();
				data = cov.getData();
				cov.dispose();
				
				cal = new SearchAndReplacePixel2PixelTabCalculation(data, data, sarMap);
				cal.run();
				
				CoverageManager.write(pathOSBocage2+"OS_Bocage2_"+xTile+"_"+yTile+".tif", data, entete);
			}
		}
	}
	
	private static void generateTuileFranceBocage() {
		
		String pathTypeBoismeent = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement/";
		String pathCesbio = "//147.99.163.169/OpenData/CESBIO/raster_5m_tuile_10x10/";
		String pathOSBocage = "C:/Data/data/sig/france/OS_Bocage/raster_5m_tuile_10x10/";
		Util.createAccess(pathOSBocage);
		
		File folder = new File(pathCesbio);
		Coverage covCesbio, covTypeBoisement;
		EnteteRaster entete;
		float[] dataCesbio, dataTypeBoisement, dataOS;
		String[] s;
		String xTile, yTile;
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0f); 
		sarMap.put(2f, 0f); 
		sarMap.put(3f, 0f);
		sarMap.put(4f, 0f); 
		sarMap.put(5f, 2f); 
		sarMap.put(6f, 2f);
		sarMap.put(7f, 2f);
		sarMap.put(8f, 3f);
		sarMap.put(9f, 3f);
		sarMap.put(10f, 3f);
		sarMap.put(11f, 3f);
		sarMap.put(12f, 3f);
		sarMap.put(13f, 4f);
		sarMap.put(14f, 6f);
		sarMap.put(15f, 7f);
		sarMap.put(16f, 0f);
		sarMap.put(17f, 0f);
		sarMap.put(18f, 0f);
		sarMap.put(19f, 0f);
		sarMap.put(20f, 0f);
		sarMap.put(21f, 0f);
		sarMap.put(22f, 0f);
		sarMap.put(23f, 0f);
		sarMap.put(24f, 0f);
		sarMap.put(25f, 0f);
		Pixel2PixelTabCalculation cal;
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
		
				System.out.println(file);
				
				s = file.replace(".tif", "").split("_");
				xTile = s[2];
				yTile = s[3];
				
				covCesbio = CoverageManager.getCoverage(pathCesbio+"OCS_2023_"+xTile+"_"+yTile+".tif");
				entete = covCesbio.getEntete();
				dataCesbio = covCesbio.getData();
				covCesbio.dispose();
				
				dataOS = new float[entete.width()*entete.height()];
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataOS, dataCesbio, sarMap);
				cal.run();
						
				covTypeBoisement = CoverageManager.getCoverage(pathTypeBoismeent+"type_boisement_"+xTile+"_"+yTile+".tif");
				dataTypeBoisement = covTypeBoisement.getData();
				covTypeBoisement.dispose();
				
				cal = new Pixel2PixelTabCalculation(dataOS, dataOS, dataTypeBoisement){
					@Override
					protected float doTreat(float[] v) {
						float v1 = v[1];
						if(v1 > 0) {
							return v1;
						}
						return v[0];
					}
				};
				cal.run();
				
				CoverageManager.write(pathOSBocage+"OS_Bocage_"+xTile+"_"+yTile+".tif", dataOS, entete);
			}
		}
	}
	
	private static void generateRasterBDTopo() {
		
		String pathDep = "//147.99.163.169/OpenData/France/Departement/raster_5m_tuile_10x10/";
		String pathBDTopo = "//147.99.163.169/OpenData/BDTOPO/";
		Util.createAccess(pathBDTopo+"raster_5m_tuile_10x10/troncon_de_route/");
		Util.createAccess(pathBDTopo+"raster_5m_tuile_10x10/troncon_de_voie_ferree/");
		Util.createAccess(pathBDTopo+"raster_5m_tuile_10x10/surface_hydrographique/");
		Util.createAccess(pathBDTopo+"raster_5m_tuile_10x10/troncon_hydrographique/");
		
		String linearRoadGpkg = pathBDTopo+"data/3-4_france_metropolitaine/BDTOPO_3-4_TOUSTHEMES_GPKG_LAMB93_FXX_2024-09-15/BDTOPO/1_DONNEES_LIVRAISON_2024-09-00156/BDT_3-4_GPKG_LAMB93_FXX-ED2024-09-15/TRANSPORT/troncon_de_route.gpkg";
		String linearRoadAttribute = "nature";
		Map<String, Integer> linearRoadCodes = new HashMap<String, Integer>();
		//linearRoadCodes.put("Bac ou liaisn maritime", );
		linearRoadCodes.put("Bretelle", 4);
		linearRoadCodes.put("Chemin", 3);
		linearRoadCodes.put("Escalier", 4);
		linearRoadCodes.put("Rond-point", 4);
		linearRoadCodes.put("Route emprierrée", 3);
		linearRoadCodes.put("Route à 1 chaussée", 4);
		linearRoadCodes.put("Route à 2 chaussées", 4);
		linearRoadCodes.put("Sentier", 3);
		linearRoadCodes.put("Type autoroutier", 4);
		
		String linearTrainGpkg = pathBDTopo+"data/3-4_france_metropolitaine/BDTOPO_3-4_TOUSTHEMES_GPKG_LAMB93_FXX_2024-09-15/BDTOPO/1_DONNEES_LIVRAISON_2024-09-00156/BDT_3-4_GPKG_LAMB93_FXX-ED2024-09-15/TRANSPORT/troncon_de_voie_ferree.gpkg";
		int linearTrainCode = 4;
		
		String surfaceWaterGpkg = pathBDTopo+"data/3-4_france_metropolitaine/BDTOPO_3-4_TOUSTHEMES_GPKG_LAMB93_FXX_2024-09-15/BDTOPO/1_DONNEES_LIVRAISON_2024-09-00156/BDT_3-4_GPKG_LAMB93_FXX-ED2024-09-15/HYDROGRAPHIE/surface_hydrographique.gpkg";
		int surfaceWaterCode = 23;
		
		String linearWaterGpkg = pathBDTopo+"data/3-4_france_metropolitaine/BDTOPO_3-4_TOUSTHEMES_GPKG_LAMB93_FXX_2024-09-15/BDTOPO/1_DONNEES_LIVRAISON_2024-09-00156/BDT_3-4_GPKG_LAMB93_FXX-ED2024-09-15/HYDROGRAPHIE/troncon_hydrographique.gpkg";
		int linearWaterCode = 23;
		
		File folder = new File(pathDep);
		Coverage covDep;
		EnteteRaster entete;
		String[] s;
		String xTile, yTile;
		
		/*
		System.out.println(GeoPackage2CoverageConverter.getGeoPackageType(linearRoadGpkg));
		System.out.println(GeoPackage2CoverageConverter.getGeoPackageType(linearTrainGpkg));
		System.out.println(GeoPackage2CoverageConverter.getGeoPackageType(surfaceWaterGpkg));
		System.out.println(GeoPackage2CoverageConverter.getGeoPackageType(linearWaterGpkg));
		*/
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(pathDep+file);
				
				s = file.replace(".tif", "").split("_");
				xTile = s[1];
				yTile = s[2];
				
				covDep = CoverageManager.getCoverage(pathDep+"dep_"+xTile+"_"+yTile+".tif");
				entete = covDep.getEntete();
				covDep.dispose();
				
				GeoPackage2CoverageConverter.rasterize(pathBDTopo+"raster_5m_tuile_10x10/troncon_de_route/troncon_de_route_"+xTile+"_"+yTile+".tif", linearRoadGpkg, linearRoadAttribute, linearRoadCodes, 0, entete, 2.5);
				GeoPackage2CoverageConverter.rasterize(pathBDTopo+"raster_5m_tuile_10x10/troncon_de_voie_ferree/troncon_de_voie_ferree_"+xTile+"_"+yTile+".tif", linearTrainGpkg, linearTrainCode, 0, entete, 2.5);
				GeoPackage2CoverageConverter.rasterize(pathBDTopo+"raster_5m_tuile_10x10/surface_hydrographique/surface_hydrographique_"+xTile+"_"+yTile+".tif", surfaceWaterGpkg, surfaceWaterCode, 0, entete);
				GeoPackage2CoverageConverter.rasterize(pathBDTopo+"raster_5m_tuile_10x10/troncon_hydrographique/troncon_hydrographique_"+xTile+"_"+yTile+".tif", linearWaterGpkg, linearWaterCode, 0, entete, 2.5);
			}
		}
	
	}
	
	private static void generateTuileCesbio() {
		String pathDep = "//147.99.163.169/OpenData/France/Departement/raster_5m_tuile_10x10/";
		String pathCesbio = "//147.99.163.169/OpenData/CESBIO/";
		Util.createAccess(pathCesbio+"raster_5m_tuile_10x10/");
		
		Coverage covCesbio = CoverageManager.getCoverage(pathCesbio+"data/OCS_2023.tif");
		
		File folder = new File(pathDep);
		Coverage covDep, cov;
		EnteteRaster entete;
		String[] s;
		String xTile, yTile;
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
					
				System.out.println(pathDep+file);
					
				s = file.replace(".tif", "").split("_");
				xTile = s[1];
				yTile = s[2];
				
				covDep = CoverageManager.getCoverage(pathDep+"dep_"+xTile+"_"+yTile+".tif");
				entete = covDep.getEntete();
				covDep.dispose();
				
				cov = Util.extendAndFill(covCesbio, entete, 2);
				
				CoverageManager.write(pathCesbio+"raster_5m_tuile_10x10/OCS_2023_"+xTile+"_"+yTile+".tif", cov.getData(), entete);
			}
		}
		
		covCesbio.dispose();
	}

	private static void cleanBoisement() {
		String pathTypeBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement_37/";
		String pathTypeBoisementClean = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement_37_clean/";
		Util.createAccess(pathTypeBoisementClean);
		
		File folder = new File(pathTypeBoisement);
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				File src = new File(pathTypeBoisement+file);
				File dest = new File(pathTypeBoisementClean+file.replace("_37_", "_"));
				
				src.renameTo(dest);
			}
		}
	}

	private static void cleanDep37() {
		
		String pathCosia37 = "//147.99.163.169/OpenData/COSIA/raster_5m/CoSIA_D037_2021/";
		String pathCosia37Clean = "//147.99.163.169/OpenData/COSIA/raster_5m/CoSIA_D037_2021_clean/";
		Util.createAccess(pathCosia37Clean);
		
		File folder = new File(pathCosia37);
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				System.out.println(file);
				//Tool.copy(pathCosia37+file, pathCosia37Clean+file.replace("CoSIA_", ""));
				Tool.copy(pathCosia37+file, pathCosia37Clean+file.replace(".tif", "_vecto.tif"));
			}
		}
		
	}

	private static void generateTypeBoisement() {
		
		String pathHauteurBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/hauteur_boisement/";
		String pathTypeBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("wood_type_detection");
		gbManager.setTile(pathHauteurBoisement);
		gbManager.setWoodHeight(pathHauteurBoisement);
		gbManager.setWoodType(pathTypeBoisement);
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void generateDistanceInfluence() {
		
		String pathHauteurBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/hauteur_boisement/";
		String pathTypeBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement/";
		String pathDistanceInfluence = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/distance_influence/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("influence_distance_calculation");
		gbManager.setTile(pathHauteurBoisement);
		gbManager.setWoodHeight(pathHauteurBoisement);
		gbManager.setWoodType(pathTypeBoisement);
		gbManager.setInfluenceDistance(pathDistanceInfluence);
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void generateGrainBocager50m() {
		
		String pathDistanceInfluence = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/distance_influence/";
		String pathGrainBocager = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setInfluenceDistance(pathDistanceInfluence);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setGrainBocager(pathGrainBocager+"france_2020-2023_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(pathGrainBocager+"france_2020-2023_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void calculGrainBocagerZoneEnjeux(int km) {
		
		int ewr = km * 1000;
		
		String pathGrainBocager = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
		gbManager.setGrainBocager(pathGrainBocager+"france_2020-2023_grain_bocager_50m.tif");
		gbManager.setThreshold(0.45);
		gbManager.setFunctionalGrainBocager(pathGrainBocager+"france_2020-2023_grain_bocager_fonctionnel_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(pathGrainBocager+"france_2020-2023_grain_bocager_cluster_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(pathGrainBocager+"france_2020-2023_grain_bocager_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(pathGrainBocager+"france_2020-2023_grain_bocager_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void generateTypeBoisement37() {
		
		String pathHauteurBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/hauteur_boisement_37/";
		String pathTypeBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/type_boisement_37/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("wood_type_detection");
		gbManager.setTile(pathHauteurBoisement);
		gbManager.setWoodHeight(pathHauteurBoisement);
		gbManager.setWoodType(pathTypeBoisement);
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void generateBoisement() {
		
		String pathCosiaFrance = "//147.99.163.169/OpenData/COSIA/raster_5m_tuile_10x10/";
		String pathHauteurBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/hauteur_boisement/";
		Util.createAccess(pathHauteurBoisement);
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0f); 
		sarMap.put(2f, 0f); 
		sarMap.put(3f, 0f);
		sarMap.put(4f, 0f); 
		sarMap.put(5f, 0f); 
		sarMap.put(6f, 0f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 10f);
		sarMap.put(9f, 10f);
		sarMap.put(10f, 10f);
		sarMap.put(11f, 0f);
		sarMap.put(12f, 0f);
		sarMap.put(13f, 0f);
		sarMap.put(14f, 0f);
		sarMap.put(15f, 0f);
		sarMap.put(16f, 0f);
		sarMap.put(17f, 0f);
		sarMap.put(18f, 0f);
		sarMap.put(19f, 0f);
		sarMap.put(20f, 0f);
		
		Coverage covCosia;
		EnteteRaster entete;
		float[] dataCosia;
		String[] s;
		String xTile, yTile;
		
		File folder = new File(pathCosiaFrance);
		SearchAndReplacePixel2PixelTabCalculation cal;
		for(String file : folder.list()) {
			if(file.endsWith(".tif")) {
				
				s = file.split("_");
				xTile = s[1];
				yTile = s[2];
				
				System.out.println(pathCosiaFrance+file);
				
				covCosia = CoverageManager.getCoverage(pathCosiaFrance+file);
				dataCosia = covCosia.getData();
				entete = covCosia.getEntete();
				covCosia.dispose();
				
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataCosia, dataCosia, sarMap);
				cal.run();
				
				CoverageManager.write(pathHauteurBoisement+"hauteur_boisement_"+xTile+"_"+yTile+".tif", dataCosia, entete);
				
			}
		}
				
	}
	
	private static void generateBoisement37() {
		
		String pathCosia37 = "//147.99.163.169/OpenData/COSIA/raster_5m/CoSIA_D037_2021/";
		String pathCosiaFrance = "//147.99.163.169/OpenData/COSIA/raster_5m_tuile_10x10/";
		String pathHauteurBoisement = "//147.99.163.169/OpenData/grain_bocager/data/france/2020-2023/hauteur_boisement/";
		Util.createAccess(pathHauteurBoisement);
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0f); 
		sarMap.put(2f, 0f); 
		sarMap.put(3f, 0f);
		sarMap.put(4f, 0f); 
		sarMap.put(5f, 0f); 
		sarMap.put(6f, 0f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 10f);
		sarMap.put(9f, 10f);
		sarMap.put(10f, 10f);
		sarMap.put(11f, 0f);
		sarMap.put(12f, 0f);
		sarMap.put(13f, 0f);
		sarMap.put(14f, 0f);
		sarMap.put(15f, 0f);
		sarMap.put(16f, 0f);
		sarMap.put(17f, 0f);
		sarMap.put(18f, 0f);
		sarMap.put(19f, 0f);
		sarMap.put(20f, 0f);
		
		Coverage covCosia;
		EnteteRaster entete;
		float[] dataCosia;
		String[] s;
		String xTile, yTile;
		
		File folder = new File(pathCosia37);
		SearchAndReplacePixel2PixelTabCalculation cal;
		for(String file : folder.list()) {
			if(file.endsWith(".tif")) {
				
				s = file.split("_");
				xTile = s[2];
				yTile = s[3];
				
				System.out.println(file+" "+xTile+" "+yTile);
				
				covCosia = CoverageManager.getCoverage(pathCosiaFrance+"cosia_"+xTile+"_"+yTile+".tif");
				dataCosia = covCosia.getData();
				entete = covCosia.getEntete();
				covCosia.dispose();
				
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataCosia, dataCosia, sarMap);
				cal.run();
				
				CoverageManager.write(pathHauteurBoisement+"hauteur_boisement_"+xTile+"_"+yTile+".tif", dataCosia, entete);
				
			}
		}
				
	}
	
	private static void generateTuilesCosia() {
		
		String pathDep = "//147.99.163.169/OpenData/France/Departement/raster_5m_tuile_10x10/";
		String pathCosia = "//147.99.163.169/OpenData/COSIA/raster_5m/";
		String pathCosiaFrance = "//147.99.163.169/OpenData/COSIA/raster_5m_tuile_10x10/";
		Util.createAccess(pathCosiaFrance);
		
		Coverage covCosia, covDep, cov;
		EnteteRaster entete, enteteCosia;
		float[] dataCosia, dataDep, data;
		String[] s, s1;
		String xTile, yTile;
		Pixel2PixelTabCalculation cal;
		File folder = new File(pathCosia);
		for(File folderDep : folder.listFiles()) {
			
			s1 = folderDep.getName().split("_");
			int dep = Integer.parseInt(s1[1].replace("D", ""));
			if(dep == 37) { 
				for(String file : folderDep.list()) {
					
					if(file.endsWith(".tif")) {
						
						System.out.println(pathCosia+folderDep.getName()+"/"+file);
						
						s = file.split("_");
						xTile = s[2];
						yTile = s[3];
						
						//System.out.println(xTile+" "+yTile);
						
						covDep = CoverageManager.getCoverage(pathDep+"dep_"+xTile+"_"+yTile+".tif");
						dataDep = covDep.getData();
						entete = covDep.getEntete();
						covDep.dispose();
						
						covCosia = CoverageManager.getCoverage(pathCosia+folderDep.getName()+"/"+file);
						enteteCosia = covCosia.getEntete();
						Rectangle  rec = EnteteRaster.getROI(enteteCosia, entete.getEnvelope());
						dataCosia = covCosia.getData(rec);
						covCosia.dispose();
						
						//System.out.println(entete);
						
						if(new File(pathCosiaFrance+"cosia_"+xTile+"_"+yTile+".tif").exists()) {
							
							cov = CoverageManager.getCoverage(pathCosiaFrance+"cosia_"+xTile+"_"+yTile+".tif");
							data = cov.getData();
							cov.dispose();
							
						}else {
							
							data = new float[entete.width()*entete.height()];
						}
						
						cal = new Pixel2PixelTabCalculation(data, data, dataCosia, dataDep){
							@Override
							protected float doTreat(float[] v) {
								if(v[2] == dep) {
									return v[1];
								}else {
									return v[0];
								}
							}
						};
						cal.run();
						
						CoverageManager.write(pathCosiaFrance+"cosia_"+xTile+"_"+yTile+".tif", data, entete);
						
					}
				}
			}
		}
	}

	private static void generateDepartementRasters(int minDep, int maxDep) {
		
		String pathDep = "//147.99.163.169/OpenData/France/Departement/";
		String pathCosia = "//147.99.163.169/OpenData/COSIA/raster_5m/";

		File folder = new File(pathCosia);
		for(File folderDep : folder.listFiles()) {
			
			String[] s1 = folderDep.getName().split("_");
			int dep = Integer.parseInt(s1[1].replace("D", ""));
			
			if(dep>=minDep && dep<=maxDep) {
				
				System.out.println(folderDep.getName());
				
				for(String file : folderDep.list()) {
					
					if(file.endsWith(".tif")) {
						
						System.out.println(pathCosia+"/"+folderDep.getName()+"/"+file);	
						
						String[] s = file.split("_");
						String xTile = s[2];
						String yTile = s[3];
						
						//System.out.println(xTile+" "+yTile);
						
						if(!new File(pathDep+"raster_5m_tuile_10x10/dep_"+xTile+"_"+yTile+".tif").exists()) {
						
							Coverage cov = CoverageManager.getCoverage(pathCosia+"/"+folderDep.getName()+"/"+file);
							EnteteRaster entete = cov.getEntete();
							cov.dispose();
							
							Coverage cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(pathDep+"data/departement_france_2019.shp", "code", entete, 0);
							float[] data = cov2.getData();
							cov2.dispose();
							
							CoverageManager.write(pathDep+"raster_5m_tuile_10x10/dep_"+xTile+"_"+yTile+".tif", data, entete);
						
						}else {
							
							System.out.println("tuile deja traitee");							
						}
					}
				}
			}
		}
	}
	
	private static void generateCosiaRasters() {
		
		String path = "//147.99.163.169/OpenData/COSIA/";
		String tempFolder = "C:/Data/temp/cosia/";
		new File(tempFolder).mkdirs();
		String rasterFolder = path+"raster_5m/";
		//String rasterFolder = "C:/Data/data/sig/cosia/raster/";
		new File(rasterFolder).mkdirs();
		File folder = new File(path+"archive/");
		String name;
		for(String file : folder.list()) {
			System.out.println(file);
			if(file.endsWith(".zip") && 
					!file.equalsIgnoreCase("CoSIA_D001_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D002_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D003_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D004_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D005_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D006_2020.zip") &&
					!file.equalsIgnoreCase("CoSIA_D007_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D008_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D009_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D010_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D011_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D012_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D013_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D014_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D015_2022.zip") &&
					!file.equalsIgnoreCase("CoSIA_D016_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D017_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D018_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D019_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D021_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D022_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D023_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D024_2021.zip") &&
					!file.equalsIgnoreCase("CoSIA_D025_2023.zip") &&
					!file.equalsIgnoreCase("CoSIA_D026_2023.zip")
					) {
				name = file.replace(".zip", "");
				
				Tool.unZip(tempFolder, path+"archive/"+file);
				
				convertGPKGtoGEOTIFF(rasterFolder+name+"/", tempFolder+name+"/");
		
				Tool.deleteFolder(tempFolder+name+"/");
			}
		}
	}
	
	private static void convertGPKGtoGEOTIFF(String outputFolder, String inputFolder) {
		
		new File(outputFolder).mkdirs();
		File folder = new File(inputFolder);
		String name;
		for(String file : folder.list()) {	
			
			if(file.endsWith(".gpkg")) {
				name = file.replace(".gpkg", ".tif");
				System.out.println("file rasterization : "+outputFolder+name);
				GeoPackage2CoverageConverter.rasterize(outputFolder+name, inputFolder+file, "numero", -1, 5, -1, null);
			}
		}
	}
	 
	
	
}
