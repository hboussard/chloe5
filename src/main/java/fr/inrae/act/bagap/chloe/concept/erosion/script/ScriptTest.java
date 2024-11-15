package fr.inrae.act.bagap.chloe.concept.erosion.script;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;

public class ScriptTest {

	private static final String rge_alti_11 = "D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D011_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D011_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D011/";
	private static final String rge_alti_31 = "D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D031/";
	private static final String rge_alti_32 = "D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D032_2019-11-21/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D032_2019-11-21/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D032/";
	
	private static final String bd_topo_11 = "D:/sig/bd_topo/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D011_2024-03-15/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D011_2024-03-15/BDTOPO/1_DONNEES_LIVRAISON_2024-04-00042/BDT_3-3_SHP_LAMB93_D011-ED2024-03-15/";
	private static final String bd_topo_31 = "D:/sig/bd_topo/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2023-03-15/BDTOPO_3-3_TOUSTHEMES_SHP_LAMB93_D031_2023-03-15/BDTOPO/1_DONNEES_LIVRAISON_2023-03-00212/BDT_3-3_SHP_LAMB93_D031-ED2023-03-15/";
	private static final String bd_topo_32 = "D:/sig/bd_topo/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D032_2022-06-15/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D032_2022-06-15/BDTOPO/1_DONNEES_LIVRAISON_2022-06-00168/BDT_3-0_SHP_LAMB93_D032-ED2022-06-15/";
	
	public static void main(String[] args) {
		
		//String evenType = "SOFT";
		//String evenType = "MEDIUM";
		//String evenType = "HARD";
		
		
		String bvShape, bvCode, bvAttribute;
		String[] rge_altis, bd_topos;
		
		/*
		// 0222 - Gardijol
		bvShape = "C:/Data/projet/coterra/data/BV tests/Zone hydro Gardijol.shp";
		bvAttribute = "code_zone";
		bvCode = "O222";	
		rge_altis = new String[] {rge_alti_11, rge_alti_31};
		bd_topos = new String[] {bd_topo_11, bd_topo_31};
		*/
		/*
		// O253 - Cédat
		bvShape = "C:/Data/projet/coterra/data/BV tests/Zone hydro Cédat.shp";
		bvAttribute = "code_zone";
		bvCode = "O253";	
		rge_altis = new String[] {rge_alti_32, rge_alti_31};
		bd_topos = new String[] {bd_topo_32, bd_topo_31};
		*/
		/*
		//  - test dans le Gers
		bvShape = "D:/sig/bd_carthage/ZoneHydro_FXX-shp/ZoneHydro_FXX.shp";
		bvAttribute = "CdZoneHydr";
		bvCode = "O655";	
		rge_altis = new String[] {rge_alti_32};
		bd_topos = new String[] {bd_topo_32};
		*/
	
		//initialisation(bvShape, bvCode, rge_altis, bd_topos);
		//procedure(bvCode);
		
		//wholeProcedure(bvShape, bvAttribute, bvCode, rge_altis, bd_topos, "SOFT");
		//wholeProcedure(bvShape, bvAttribute, bvCode, rge_altis, bd_topos, "MEDIUM");
		//wholeProcedure(bvShape, bvAttribute, bvCode, rge_altis, bd_topos, "HARD");
	}

	private static void wholeProcedure(String bvShape, String bvAttribute, String bvCode, String[] rge_altis, String[] bd_topos, String eventType) {
		
		ErosionManager manager = new ErosionManager("erosion_calculation");
		
		//manager.setTerritoryShape("D:/sig/bd_carthage/ZoneHydro_FXX-shp/ZoneHydro_FXX.shp");
		//manager.setTerritoryIDAttribute("CdZoneHydr");
		manager.setTerritoryShape(bvShape);
		manager.setTerritoryIDAttribute(bvAttribute);
		manager.setTerritoryIDValues(bvCode);
		
		manager.setEventType(eventType); // type d'evenement meteorologique {"SOFT, "MEDIUM", "HARD"}
		
		for(String rge_alti : rge_altis) {
			manager.addElevationFolder(rge_alti);
		}
				
		manager.setOsSource("D:/sig/oso_thiea/OCS_2021.tif");
				
		for(String bd_topo : bd_topos) {
			manager.addSurfaceWoodShape(bd_topo+"OCCUPATION_DU_SOL/ZONE_DE_VEGETATION.shp");	
		}
		manager.setSurfaceWoodAttribute("NATURE");
		manager.addSurfaceWoodCode("Bois", 16);
		manager.addSurfaceWoodCode("Forêt fermée de conifères", 17);
		manager.addSurfaceWoodCode("Forêt fermée de feuillus", 16);
		manager.addSurfaceWoodCode("Forêt fermée mixte", 16);
		manager.addSurfaceWoodCode("Forêt ouverte", 16);
		manager.addSurfaceWoodCode("Haie", 24);
		manager.addSurfaceWoodCode("Lande herbacée", 19);
		manager.addSurfaceWoodCode("Lande ligneuse", 19);
		//manager.addSurfaceWoodCode("Peupleraie", );
		manager.addSurfaceWoodCode("Verger", 14);
		manager.addSurfaceWoodCode("Vigne", 15);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearWoodShape(bd_topo+"OCCUPATION_DU_SOL/HAIE.shp");	
		}
		manager.setLinearWoodCode(24);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearRoadShape(bd_topo+"TRANSPORT/TRONCON_DE_ROUTE.shp");
		}
		manager.setLinearRoadAttribute("NATURE");
		//manager.addLinearRoadCode("Bac ou liaisn maritime", );
		manager.addLinearRoadCode("Bretelle", 4);
		manager.addLinearRoadCode("Chemin", 25);
		manager.addLinearRoadCode("Escalier", 4);
		manager.addLinearRoadCode("Rond-point", 4);
		manager.addLinearRoadCode("Route emprierrée", 25);
		manager.addLinearRoadCode("Route à 1 chaussée", 4);
		manager.addLinearRoadCode("Route à 2 chaussées", 4);
		manager.addLinearRoadCode("Sentier", 25);
		manager.addLinearRoadCode("Type autoroutier", 4);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearTrainShape(bd_topo+"TRANSPORT/TRONCON_DE_VOIE_FERREE.shp");
		}
		manager.setLinearTrainCode(4);
				
		for(String bd_topo : bd_topos) {
			manager.addSurfaceWaterShape(bd_topo+"HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp");
		}
		manager.setSurfaceWaterCode(23);
				
		for(String bd_topo : bd_topos) {
			manager.addLinearWaterShape(bd_topo+"HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp");
		}
		manager.setLinearWaterCode(23);
		
		manager.setOutputFolder("C:/Data/projet/coterra/essai_"+bvCode+"/");
		manager.setOutputPrefix(bvCode);
		manager.setInfiltrationMapFile("C:/Data/projet/coterra/essai_"+bvCode+"/infiltration_map.txt");
		manager.setErodibilityMapFile("C:/Data/projet/coterra/essai_"+bvCode+"/erodibility_map.txt");
		
		ErosionProcedure procedure = manager.build();
		
		procedure.run();
	}	

	
	private static void procedure(String bvCode) {
		
		ErosionManager manager = new ErosionManager("erosion_calculation");
		
		manager.setOutputFolder("C:/Data/projet/coterra/essai_"+bvCode+"/");
		manager.setOutputPrefix(bvCode);
		manager.setInfiltrationMapFile("C:/Data/projet/coterra/essai_"+bvCode+"/infiltration_map.txt");
		manager.setErodibilityMapFile("C:/Data/projet/coterra/essai_"+bvCode+"/erodibility_map.txt");
		
		ErosionProcedure procedure = manager.build();
		
		procedure.run();
	}	
	
	private static void initialisation(String bvShape, String bvAttribute, String bvCode, String osSource, String[] rge_altis, String[] bd_topos) {
		
		ErosionManager manager = new ErosionManager("data_initialization");
		
		//manager.setTerritoryShape("D:/sig/bd_carthage/ZoneHydro_FXX-shp/ZoneHydro_FXX.shp");
		//manager.setTerritoryIDAttribute("CdZoneHydr");
		manager.setTerritoryShape(bvShape);
		manager.setTerritoryIDAttribute(bvAttribute);
		manager.setTerritoryIDValues(bvCode);
		
		for(String rge_alti : rge_altis) {
			manager.addElevationFolder(rge_alti);
		}
		
		manager.setOsSource("D:/sig/oso_thiea/OCS_2021.tif");
		
		for(String bd_topo : bd_topos) {
			manager.addSurfaceWoodShape(bd_topo+"OCCUPATION_DU_SOL/ZONE_DE_VEGETATION.shp");	
		}
		manager.setSurfaceWoodAttribute("NATURE");
		manager.addSurfaceWoodCode("Bois", 16);
		manager.addSurfaceWoodCode("Forêt fermée de conifères", 17);
		manager.addSurfaceWoodCode("Forêt fermée de feuillus", 16);
		manager.addSurfaceWoodCode("Forêt fermée mixte", 16);
		manager.addSurfaceWoodCode("Forêt ouverte", 16);
		manager.addSurfaceWoodCode("Haie", 24);
		manager.addSurfaceWoodCode("Lande herbacée", 19);
		manager.addSurfaceWoodCode("Lande ligneuse", 19);
		//manager.addSurfaceWoodCode("Peupleraie", );
		manager.addSurfaceWoodCode("Verger", 14);
		manager.addSurfaceWoodCode("Vigne", 15);
		
		for(String bd_topo : bd_topos) {
			manager.addLinearWoodShape(bd_topo+"OCCUPATION_DU_SOL/HAIE.shp");	
		}
		manager.setLinearWoodCode(24);
		
		for(String bd_topo : bd_topos) {
			manager.addLinearRoadShape(bd_topo+"TRANSPORT/TRONCON_DE_ROUTE.shp");
		}
		manager.setLinearRoadAttribute("NATURE");
		//manager.addLinearRoadCode("Bac ou liaisn maritime", );
		manager.addLinearRoadCode("Bretelle", 4);
		manager.addLinearRoadCode("Chemin", 25);
		manager.addLinearRoadCode("Escalier", 4);
		manager.addLinearRoadCode("Rond-point", 4);
		manager.addLinearRoadCode("Route emprierrée", 25);
		manager.addLinearRoadCode("Route à 1 chaussée", 4);
		manager.addLinearRoadCode("Route à 2 chaussées", 4);
		manager.addLinearRoadCode("Sentier", 25);
		manager.addLinearRoadCode("Type autoroutier", 4);
		
		for(String bd_topo : bd_topos) {
			manager.addLinearTrainShape(bd_topo+"TRANSPORT/TRONCON_DE_VOIE_FERREE.shp");
		}
		manager.setLinearTrainCode(4);
		
		for(String bd_topo : bd_topos) {
			manager.addSurfaceWaterShape(bd_topo+"HYDROGRAPHIE/SURFACE_HYDROGRAPHIQUE.shp");
		}
		manager.setSurfaceWaterCode(23);
		
		for(String bd_topo : bd_topos) {
			manager.addLinearWaterShape(bd_topo+"HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp");
		}
		manager.setLinearWaterCode(23);
		
		manager.setOutputFolder("C:/Data/projet/coterra/essai_"+bvCode+"/");
		manager.setOutputPrefix(bvCode);
		ErosionProcedure procedure = manager.build();
		
		procedure.run();
	}
	
}
