package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptGers {

	public static void main(String[] args){
		//scriptCalculGrainInitial();
		//scriptCalculGrainAmenagement();
		//scriptCalculGrainAmenagementAlternatif();
		cleanMNHC();
	}
	
	private static void cleanMNHC() {
		
		String path = "E:/temp/gers/wetransfer_suivi-evolutif-grain-2019-2022_2024-09-13_1227/";
		
		Coverage cov = CoverageManager.getCoverage(path+"hauteur_arboree_initiale_CCT_2022.tif");
		EnteteRaster entete = cov.getEntete();
		float[] dataH = cov.getData();
		cov.dispose();
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("Bois", 0);
		codes.put("Forêt fermée de feuillus", 0);
		codes.put("Forêt fermée de conifères", 0);
		codes.put("Forêt fermée mixte", 0);
		codes.put("Forêt ouverte", 0);
		codes.put("Haie", 0);
		codes.put("Lande ligneuse", 0);
		codes.put("Peupleraie", 0);
		codes.put("Verger", 0);
		codes.put("Vigne", 1);
		
		Coverage covWood = ShapeFile2CoverageConverter.getSurfaceCoverage("D:/sig/bd_topo/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D032_2022-06-15/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D032_2022-06-15/BDTOPO/1_DONNEES_LIVRAISON_2022-06-00168/BDT_3-0_SHP_LAMB93_D032-ED2022-06-15/OCCUPATION_DU_SOL/ZONE_DE_VEGETATION.shp", "NATURE", codes, entete, 0);
		float[] dataWood = covWood.getData();
		covWood.dispose();
		
		for(int i=0; i<entete.width()*entete.height(); i++) {
			if(dataWood[i] == 1) {
				dataH[i] = 0;
			}
		}
		CoverageManager.write(path+"hauteur_arboree_initiale_CCT_2022_cleanVigne.tif", dataH, entete);
	}
	
	private static void scriptCalculGrainInitial() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setTerritory(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setThresholds(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setWoodHeight(dataPath+"hauteur_boisement_init.tif");
		gbManager.setWoodType(dataPath+"type_boisement_init.tif");
		gbManager.setInfluenceDistance(dataPath+"distance_influence_init.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_init_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_init_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrainAmenagement() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setTerritory(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setThresholds(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setWoodPlanting(dataPath+"creation_haies.shp");
		gbManager.setHeightPlantingAttribute("hauteur");
		gbManager.setWoodHeight(dataPath+"hauteur_boisement_amenagement.tif");
		gbManager.setWoodType(dataPath+"type_boisement_amenagement.tif");
		gbManager.setInfluenceDistance(dataPath+"distance_influence_amenagement.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_amenagement_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_amenagement_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrainAmenagementAlternatif() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setTerritory(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setThresholds(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setWoodPlanting(dataPath+"alternatif/creation_haies.shp");
		gbManager.setHeightPlantingAttribute("hauteur");
		gbManager.setWoodHeight(dataPath+"alternatif/hauteur_boisement_alternatif.tif");
		gbManager.setWoodType(dataPath+"alternatif/type_boisement_alternatif.tif");
		gbManager.setInfluenceDistance(dataPath+"alternatif/distance_influence_alternatif.tif");
		gbManager.setGrainBocager(dataPath+"alternatif/grain_bocager_alternatif_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"alternatif/grain_bocager_alternatif_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	// tous les noms de fonctions ont changées (english)
	private static void scriptCalculGrainAmenagementNew() {
		String dataPath = "C:/Users/gladys/Desktop/grain PETR True/";
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setForce(true); // pour forcer la construction de toutes les couches intermediaires 
		gbManager.setTerritory(dataPath+"Zonage_PETR.shp"); // pour recuperer l'enveloppe d'analyse
		gbManager.setBufferArea(1000);
		gbManager.setThresholds(0.3, 0.45, 1); // le dernier seuil a 1 n'a pas de sens, 
												// c'est le second seuil qui est pris en compte comme seuil de fonctionnalite
		gbManager.setBocage("D:/DSB_data/MNHC_mean/");
		gbManager.setWoodPlanting(dataPath+"Amenagement.shp");
		gbManager.setHeightPlantingAttribute("hauteur");
		gbManager.setWoodHeight(dataPath+"hauteur_post_amenagement_cours_eau.tif");
		gbManager.setWoodType(dataPath+"type_element_arbore_post_amenagement_cours_eau.tif");
		gbManager.setInfluenceDistance(dataPath+"distance_influence_post_amenagement_cours_eau.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_continu_post_amenagement_cours_eau.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_3_classes_post_amenagement_cours_eau.tif");
		GrainBocagerProcedure gbProcedure = gbManager.build();
		gbProcedure.run();
	}
	
}
