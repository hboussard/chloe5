package fr.inrae.act.bagap.chloe.concept.erosion.script;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;

public class ScriptLivingLabGascogne {

	private static final String bd_topo_31 = "D:/sig/bd_topo/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2023-03-15/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2023-03-15/BDTOPO/1_DONNEES_LIVRAISON_2023-03-00212/BDT_3-3_SHP_LAMB93_D031-ED2023-03-15/";
	private static final String bd_topo_32 = "D:/sig/bd_topo/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D032_2024-06-15/BDTOPO_3-4_TOUSTHEMES_SHP_LAMB93_D032_2024-06-15/BDTOPO/1_DONNEES_LIVRAISON_2024-06-00178/BDT_3-4_SHP_LAMB93_D032-ED2024-06-15/";
	//private static final String bd_topo_65 = "D:/sig/bd_topo/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2024-03-15/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D065-ED2024-03-15/";
	
	public static void main(String[] args) {
		
		String path = "C:/Data/temp/gaelle/transfer_13770727_files_62f698fc/";
		String outputFolder = path+"erosion/";
		String bvShape = path+"emprise_Touch.shp";
		String bvAttribute = "Subbasin";
		String bvCode = "1";
		String rge_alti = path+"sb12_aligned.asc";
		String osSource = path+"Baseline_BVT.tif";
		String surfaceWoodShape = path+"vec_wood.shp";
		String[] bd_topos = new String[] {bd_topo_31, bd_topo_32};
		String infiltrationMapFile = path+"infiltration_map_PACSE.txt";
		String erodibilityMapFile = path+"erodibility_map_PACSE.txt";
				
		wholeProcedure(outputFolder, bvShape, bvAttribute, bvCode, rge_alti, osSource, surfaceWoodShape, bd_topos, infiltrationMapFile, erodibilityMapFile);
	}
	
	private static void wholeProcedure(String outputFolder, String bvShape, String bvAttribute, String bvCode, String rge_alti, String osSource, String surfaceWoodShape, String[] bd_topos, String infiltrationMapFile, String erodibilityMapFile) {
		
		ErosionManager manager = new ErosionManager("erosion_calculation");
	
		manager.setTerritoryShape(bvShape);
		manager.setTerritoryIDAttribute(bvAttribute);
		manager.setTerritoryIDValues(bvCode);
		
		manager.setEventType("HARD"); // type d'evenement meteorologique {"SOFT, "MEDIUM", "HARD"}
		
		manager.addElevationFolder(rge_alti);
				
		manager.setOsSource(osSource);
				
		manager.addSurfaceWoodShape(surfaceWoodShape);	
		manager.setSurfaceWoodAttribute("NomID");
		manager.addSurfaceWoodCode("Coeur de foret", 114);
		manager.addSurfaceWoodCode("Lisiere de foret", 115);
		manager.addSurfaceWoodCode("Haie", 116);
		manager.addSurfaceWoodCode("VRG", 112);
		manager.addSurfaceWoodCode("VRT", 113);
		manager.addSurfaceWoodCode("VRC", 111);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearWoodShape(bd_topo+"OCCUPATION_DU_SOL/HAIE.shp");	
		}
		manager.setLinearWoodCode(116);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearRoadShape(bd_topo+"TRANSPORT/TRONCON_DE_ROUTE.shp");
		}
		manager.setLinearRoadAttribute("NATURE");
		manager.addLinearRoadCode("Bretelle", 200);
		manager.addLinearRoadCode("Chemin", 116);
		manager.addLinearRoadCode("Escalier", 200);
		manager.addLinearRoadCode("Rond-point", 200);
		manager.addLinearRoadCode("Route emprierrée", 116);
		manager.addLinearRoadCode("Route à 1 chaussée", 200);
		manager.addLinearRoadCode("Route à 2 chaussées", 200);
		manager.addLinearRoadCode("Sentier", 25);
		manager.addLinearRoadCode("Type autoroutier", 200);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearTrainShape(bd_topo+"TRANSPORT/TRONCON_DE_VOIE_FERREE.shp");
		}
		manager.setLinearTrainCode(200);
				
		for(String bd_topo : bd_topos) {
			manager.addSurfaceWaterShape(bd_topo+"HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp");
		}
		manager.setSurfaceWaterCode(8);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearWaterShape(bd_topo+"HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp");
		}
		manager.setLinearWaterCode(8);
		
		//manager.setDisplacement(10); // 50m
		
		manager.setInfiltrationMapFile(infiltrationMapFile);
		manager.setErodibilityMapFile(erodibilityMapFile);
		
		manager.setOutputPrefix("BVT_Baseline_1_v4");
		manager.setOutputFolder(outputFolder);
	
		ErosionProcedure procedure = manager.build();
		
		procedure.run();
	}	

	/*
	chloe::erosion(

			infiltration_map_file="/media/gaelle/DATA/Solubiod/PACSE/R_PACSE/data/erosion/infiltration_map_PACSE.txt",
			erodibility_map_file="/media/gaelle/DATA/Solubiod/PACSE/R_PACSE/data/erosion/erodibility_map_PACSE.txt",
			displacement=1,
			territory_shape="/media/gaelle/DATA/Solubiod/PACSE/QGIS/emprise_Touch.shp",
			territory_id_attribute="Subbasin",
			territory_id_values=1,
			elevation_folder=c("/media/gaelle/DATA/Solubiod/PACSE/R_PACSE/data/erosion/RGE_3m_model"),
			os_source="/media/gaelle/DATA/Solubiod/PACSE/R_PACSE/data/erosion/Baseline_BVT.tif",
			surface_wood_shape="/media/gaelle/DATA/Solubiod/PACSE/R_PACSE/data/erosion/vec_wood.shp",
			surface_wood_attribute="NomID",
			surface_wood_code=list(c("Coeur de foret",114),	c("Lisiere de foret",115),c("Haie",	116),c("VRG",112),c("VRT",113),	c("VRC",111)),
			linear_wood_shape=
			c("/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D031-ED2024-03-15/OCCUPATION_DU_SOL/HAIE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D032_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D032-ED2024-03-15/OCCUPATION_DU_SOL/HAIE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2023-09-15/BDTOPO/1_DONNEES_LIVRAISON_2023-09-00196/BDT_3-3_SHP_LAMB93_D065-ED2023-09-15/OCCUPATION_DU_SOL/HAIE.shp"),
			linear_wood_code=116,
			linear_road_shape= 
			c("/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D031-ED2024-03-15/TRANSPORT/TRONCON_DE_ROUTE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D032_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D032-ED2024-03-15/TRANSPORT/TRONCON_DE_ROUTE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2023-09-15/BDTOPO/1_DONNEES_LIVRAISON_2023-09-00196/BDT_3-3_SHP_LAMB93_D065-ED2023-09-15/TRANSPORT/TRONCON_DE_ROUTE.shp"),
			linear_road_attribute="NATURE",
			linear_road_code=list(c("Bretelle",	200),c("Chemin",116),c("Escalier",200),	c("Rond-point",	200),c("Route emprierrée",116),	c("Route à 1 chaussée",	200),c("Route à 2 chaussées",200),c("Sentier",25),c("Type autoroutier",	200)),
			linear_train_shape	=
			c("/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D031-ED2024-03-15/TRANSPORT/TRONCON_DE_VOIE_FERREE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D032_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D032-ED2024-03-15/TRANSPORT/TRONCON_DE_VOIE_FERREE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2023-09-15/BDTOPO/1_DONNEES_LIVRAISON_2023-09-00196/BDT_3-3_SHP_LAMB93_D065-ED2023-09-15/TRANSPORT/TRONCON_DE_VOIE_FERREE.shp"),
			linear_train_code=200,
			surface_water_shape	= 
			c("/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D031-ED2024-03-15/HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D032_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D032-ED2024-03-15/HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2023-09-15/BDTOPO/1_DONNEES_LIVRAISON_2023-09-00196/BDT_3-3_SHP_LAMB93_D065-ED2023-09-15/HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp"),
			surface_water_code= 8,
			linear_water_shape= 
			c("/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D031-ED2024-03-15/HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D032_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D032-ED2024-03-15/HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp",
			"/media/gaelle/DATA/Solubiod/scenarios/cartes/donnees/BDTOPO/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D065_2023-09-15/BDTOPO/1_DONNEES_LIVRAISON_2023-09-00196/BDT_3-3_SHP_LAMB93_D065-ED2023-09-15/HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp"),
			linear_water_code=8,
			output_folder="/home/gaelle/Documents/outputs_erosion/BVT_Baseline_1_v3",
			output_prefix="BVT_Baseline_1_v3",
			properties_file="params.properties"
		)
		*/
}
