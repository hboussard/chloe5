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
		gbManager.setModeFast(true);
		gbManager.setBocage("H:/IGN/data/61_2020_5m/mean/");
		gbManager.setHauteurBoisement(dataPath+"hauteur_boisement_61_2020.tif");
		gbManager.setTypeBoisement(dataPath+"type_boisement_61_2020.tif");
		gbManager.setDistanceInfluenceBoisement(dataPath+"distance_influence_61_2020.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_5m_61_2020.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_5m_4classes_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrain50m() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setDistanceInfluenceBoisement(dataPath+"distance_influence_61_2020.tif");
		gbManager.setGrainCellSize(50);
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_50m_4classes_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculZoneEnjeux1km() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true); 
		
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setGrainBocagerFonctionnel(dataPath+"grain_bocager_fonctionnel_50m_61_2020.tif");
		gbManager.setClusterGrainBocagerFonctionnel(dataPath+"grain_bocager_cluster_50m_61_2020.tif");
		
		gbManager.setEnjeuxCellSize(200);
		gbManager.setEnjeuxWindowRadius(1000);
		
		gbManager.setProportionGrainBocagerFonctionnel(dataPath+"proportion_grain_bocager_fonc_1km_61_2020.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel(dataPath+"fragmentation_grain_bocager_fonc_1km_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculZoneEnjeux5km() {
		
		String dataPath = "H:/IGN/analyse_orne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true); 
		
		gbManager.setGrainBocager(dataPath+"grain_bocager_50m_61_2020.tif");
		gbManager.setGrainBocagerFonctionnel(dataPath+"grain_bocager_fonctionnel_50m_61_2020.tif");
		gbManager.setClusterGrainBocagerFonctionnel(dataPath+"grain_bocager_cluster_50m_61_2020.tif");
		
		gbManager.setEnjeuxCellSize(200);
		gbManager.setEnjeuxWindowRadius(5000);
		
		gbManager.setProportionGrainBocagerFonctionnel(dataPath+"proportion_grain_bocager_fonc_5km_61_2020.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel(dataPath+"fragmentation_grain_bocager_fonc_5km_61_2020.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
