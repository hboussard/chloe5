package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerOrne {

	public static void main(String[] args){
		//scriptCalculGrain();
		//scriptCalculGrain50m();
		//scriptCalculZoneEnjeux1km();
		//scriptCalculZoneEnjeux5km();
	}
	
	
	private static void scriptCalculGrain() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setBocage("H:/IGN/data/61_2020_5m/mean/");
		gbManager.setWoodHeight(dataPath+"hauteur_boisement_61_2020.tif");
		gbManager.setWoodType(dataPath+"type_boisement_61_2020.tif");
		gbManager.setInfluenceDistance(dataPath+"distance_influence_61_2020.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_5m_61_2020.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_5m_4classes_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrain50m() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setInfluenceDistance(dataPath+"distance_influence_61_2020.tif");
		gbManager.setGrainBocagerCellSize(50);
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_50m_4classes_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculZoneEnjeux1km() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setFastMode(true); 
		
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setFunctionalGrainBocager(dataPath+"grain_bocager_fonctionnel_50m_61_2020.tif");
		gbManager.setFunctionalGrainBocagerClustering(dataPath+"grain_bocager_cluster_50m_61_2020.tif");
		
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(1000);
		
		gbManager.setFunctionalGrainBocagerProportion(dataPath+"proportion_grain_bocager_fonc_1km_61_2020.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(dataPath+"fragmentation_grain_bocager_fonc_1km_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculZoneEnjeux5km() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setFastMode(true); 
		
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setFunctionalGrainBocager(dataPath+"grain_bocager_fonctionnel_50m_61_2020.tif");
		gbManager.setFunctionalGrainBocagerClustering(dataPath+"grain_bocager_cluster_50m_61_2020.tif");
		
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(5000);
		
		gbManager.setFunctionalGrainBocagerProportion(dataPath+"proportion_grain_bocager_fonc_5km_61_2020.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(dataPath+"fragmentation_grain_bocager_fonc_5km_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
