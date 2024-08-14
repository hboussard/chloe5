package fr.inrae.act.bagap.chloe.concept.erosion.script;

import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Envelope;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptTerritoireNormandie {

	private static final String bd_topo_14 = "D:/sig/bd_topo/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D014_2024-06-15/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D014_2024-06-15/BDTOPO/1_DONNEES_LIVRAISON_2024-06-00178/BDT_3-4_SHP_LAMB93_D014-ED2024-06-15/";
	private static final String bd_topo_27 = "D:/sig/bd_topo/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D027_2022-06-15/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D027_2022-06-15/BDTOPO/1_DONNEES_LIVRAISON_2022-06-00168/BDT_3-0_SHP_LAMB93_D027-ED2022-06-15/";
	private static final String bd_topo_76 = "D:/sig/bd_topo/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D076_2024-06-15/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D076_2024-06-15/BDTOPO/1_DONNEES_LIVRAISON_2024-06-00178/BDT_3-4_SHP_LAMB93_D076-ED2024-06-15/";
	
	private static final String grain_bocager_14 = "D:/grain_bocager/data/14/2020/14_2020_grain_bocager_5m.tif";
	private static final String grain_bocager_27 = "D:/grain_bocager/data/27/2019/27_2019_grain_bocager_5m.tif";
	private static final String grain_bocager_76 = "D:/grain_bocager/data/76/2019/76_2019_grain_bocager_5m.tif";
	
	private static EnteteRaster entete;
	private static String grain_bocager;
	private static String territoryCode;
	private static String territoryShape;
	private static String bd_topo;
	private static String outputPath;
	private static String os;
	private static String osSource;
	private static String surfaceWoodShape;
	private static String surfaceWoodAttribute;
	private static Map<String, Integer> surfaceWoodCodes;
	private static String linearWoodShape;
	private static int linearWoodCode;
	private static String linearRoadShape;
	private static String linearRoadAttribute;
	private static Map<String, Integer> linearRoadCodes;
	private static String linearTrainShape;
	private static int linearTrainCode;
	private static String surfaceWaterShape;
	private static int surfaceWaterCode;
	private static String linearWaterShape;
	private static int linearWaterCode;
	
	public static void main(String[] args) {
		/*
		territoryCode = "T1";
		territoryShape = "D:/chloe/formation/normandie/Communes_TVB/Caen la Mer.shp";
		bd_topo = bd_topo_14;
		grain_bocager = grain_bocager_14;
		
		procedure();
		
		territoryCode = "T2";
		territoryShape = "D:/chloe/formation/normandie/Communes_TVB/Evreux Portes de Normandie.shp";
		bd_topo = bd_topo_27;
		grain_bocager = grain_bocager_27;
		
		procedure();
		
		territoryCode = "T3";
		territoryShape = "D:/chloe/formation/normandie/Communes_TVB/Cingal Suisse Normande.shp";
		bd_topo = bd_topo_14;
		grain_bocager = grain_bocager_14;
		
		procedure();
		
		territoryCode = "T4";
		territoryShape = "D:/chloe/formation/normandie/Communes_TVB/La Havre Seine Metropole.shp";
		bd_topo = bd_topo_76;
		grain_bocager = grain_bocager_76;
		
		procedure();
		*/
	}
	
	private static void procedure() {
		initialisation();
		construction();
	}
	
	
	private static void initialisation() {
		
		outputPath = "D:/chloe/formation/normandie/data/"+territoryCode+"/";
		Util.createAccess(outputPath);
		os = outputPath+territoryCode+"_os.tif";
		
		osSource = "D:/sig/oso_thiea/OCS_2021.tif";
		
		surfaceWoodShape = bd_topo+"OCCUPATION_DU_SOL/ZONE_DE_VEGETATION.shp";
		surfaceWoodAttribute = "NATURE";
		surfaceWoodCodes = new HashMap<String, Integer>();
		surfaceWoodCodes.put("Bois", 16);
		surfaceWoodCodes.put("Forêt fermée de conifères", 17);
		surfaceWoodCodes.put("Forêt fermée de feuillus", 16);
		surfaceWoodCodes.put("Forêt fermée mixte", 16);
		surfaceWoodCodes.put("Forêt ouverte", 16);
		surfaceWoodCodes.put("Haie", 24);
		surfaceWoodCodes.put("Lande herbacée", 19);
		surfaceWoodCodes.put("Lande ligneuse", 19);
		//surfaceWoodCodes.put("Peupleraie", );
		surfaceWoodCodes.put("Verger", 14);
		surfaceWoodCodes.put("Vigne", 15);
		
		linearWoodShape = bd_topo+"OCCUPATION_DU_SOL/HAIE.shp";
		linearWoodCode = 24;
		
		linearRoadShape = bd_topo+"TRANSPORT/TRONCON_DE_ROUTE.shp";
		linearRoadAttribute = "NATURE";
		linearRoadCodes = new HashMap<String, Integer>();
		//linearRoadCodes.put("Bac ou liaisn maritime", );
		linearRoadCodes.put("Bretelle", 4);
		linearRoadCodes.put("Chemin", 25);
		linearRoadCodes.put("Escalier", 4);
		linearRoadCodes.put("Rond-point", 4);
		linearRoadCodes.put("Route emprierrée", 25);
		linearRoadCodes.put("Route à 1 chaussée", 4);
		linearRoadCodes.put("Route à 2 chaussées", 4);
		linearRoadCodes.put("Sentier", 25);
		linearRoadCodes.put("Type autoroutier", 4);
		
		linearTrainShape = bd_topo+"TRANSPORT/TRONCON_DE_VOIE_FERREE.shp";
		linearTrainCode = 4;
		
		surfaceWaterShape = bd_topo+"HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp";
		surfaceWaterCode = 23;
		
		linearWaterShape = bd_topo+"HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp";
		linearWaterCode = 23;
	}
	
	private static void construction() {
		
		entete = initEntete(territoryShape, territoryCode, grain_bocager);
		
		osRecovery(os, entete, osSource, 2);
		
		surfaceWoodRasterization(os, surfaceWoodShape, surfaceWoodAttribute, surfaceWoodCodes);
		
		linearWoodRasterization(os, linearWoodShape, linearWoodCode);
		
		linearRoadRasterization(os, linearRoadShape, linearRoadAttribute, linearRoadCodes);
		
		linearTrainRasterization(os, linearTrainShape, linearTrainCode);
		
		surfaceWaterRasterization(os, surfaceWaterShape, surfaceWaterCode);
		
		linearWaterRasterization(os, linearWaterShape, linearWaterCode);

	}
	
	public static EnteteRaster initEntete(String territoryShape, String territoryCode, String grain_bocager) {
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(territoryShape, 2000, "code", territoryCode);
		
		Coverage cov = CoverageManager.getCoverage(grain_bocager);
		EnteteRaster refEntete = cov.getEntete();
		cov.dispose();
		
		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		//System.out.println(entete);
		
		return entete;
	}
	
	public static void osRecovery(String os, EnteteRaster entete, String osSource, int divisor) {
		
		System.out.println("recuperation de l'occupation des sols (OSO, Theia)");
		
		Coverage ossCov = CoverageManager.getCoverage(osSource);
				
		Coverage cov = Util.extendAndFill(ossCov, entete, divisor);
		
		ossCov.dispose();
		
		CoverageManager.write(os, cov.getData(), cov.getEntete());
		
		cov.dispose();
	}
	
	public static void surfaceWoodRasterization(String os, String surfaceWoodShape, String surfaceWoodAttribute, Map<String, Integer> surfaceWoodCodes){
		
		System.out.println("recuperation des boisement surfaciques (bd_topo, Zone de vegetation, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(osData, osEntete, surfaceWoodShape, surfaceWoodAttribute, surfaceWoodCodes);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearWoodRasterization(String os, String linearWoodShape, int linearWoodCode){
		
		System.out.println("recuperation des boisement lineaires (bd_topo, Haie, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearWoodShape, linearWoodCode, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearRoadRasterization(String os, String linearRoadShape, String linearRoadAttribute, Map<String, Integer> linearRoadCodes){
		
		System.out.println("recuperation des routes lineaires (bd_topo, troncon de route, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearRoadShape, linearRoadAttribute, linearRoadCodes, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearTrainRasterization(String os, String linearTrainShape, int linearTrainCode){
		
		System.out.println("recuperation des voies ferrees (bd_topo, troncon de voie ferree, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearTrainShape, linearTrainCode, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void surfaceWaterRasterization(String os, String surfaceWaterShape, int surfaceWaterCode){
		
		System.out.println("recuperation des surfaces hydrographiques (bd_topo, surface hydrographique, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(osData, osEntete, surfaceWaterShape, surfaceWaterCode);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearWaterRasterization(String os, String linearWaterShape, int linearWaterCode){
		
		System.out.println("recuperation des troncons hydrographiques (bd_topo, troncon hydrographique, IGN)");
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
	
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearWaterShape, linearWaterCode, 2.5);
		cov.dispose();
	
		CoverageManager.write(os, osData, osEntete);
	}
}
