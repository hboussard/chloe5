package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerManager {

	public static void main(String[] args){
		
		//scriptRecuperationHauteurBoisement();
		//scriptDetectionTypeBoisement();
		//scriptCalculDistanceInfluenceBoisement();
		//scriptCalculGrainBocager();
		
		scriptEnsembleCalculGrainBocager();
	}
		
	private static void scriptEnsembleCalculGrainBocager() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/rafcom/data/perimetre_RAF_2022_cadastre.shp");
		gbManager.setBufferArea(100);
		gbManager.setBocage("G:/FDCCA/diag_ea/data/mnhc/");
		gbManager.setPlantation("H:/rafcom/data/plantations_RAF_2008-2023.shp");
		gbManager.setAttributHauteurPlantation("code");
		gbManager.setHauteurBoisement("H:/rafcom/test_grain_bocager/hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/rafcom/test_grain_bocager/type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement("H:/rafcom/test_grain_bocager/distance_influence.tif");
		gbManager.setGrainCellSize(50.0);
		gbManager.setGrainBocager("H:/rafcom/test_grain_bocager/grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes("H:/rafcom/test_grain_bocager/grain_bocager_50m_4classes.tif");
		gbManager.setGrainBocagerFonctionnel("H:/rafcom/test_grain_bocager/grain_bocager_fonctionnel.tif");
		gbManager.setClusterGrainBocagerFonctionnel("H:/rafcom/test_grain_bocager/cluster_grain_bocager_fonctionnel.tif");
		gbManager.setProportionGrainBocagerFonctionnel("H:/rafcom/test_grain_bocager/proportion_grain_bocager_fonctionnel_1000m.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel("H:/rafcom/test_grain_bocager/fragmentation_grain_bocager_fonctionnel_1000m.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptRecuperationHauteurBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("recuperation_hauteur_boisement");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/rafcom/data/perimetre_RAF_2022_cadastre.shp");
		gbManager.setBufferArea(1000);
		gbManager.setBocage("G:/FDCCA/diag_ea/data/mnhc/");
		gbManager.setPlantation("H:/rafcom/data/plantations_RAF_2008-2023.shp");
		gbManager.setAttributHauteurPlantation("code");
		gbManager.setHauteurBoisement("H:/rafcom/test_grain_bocager/hauteur_boisement.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptDetectionTypeBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setHauteurBoisement("H:/rafcom/test_grain_bocager/hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/rafcom/test_grain_bocager/type_boisement.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculDistanceInfluenceBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_distance_influence_boisement");
		gbManager.setModeFast(true);
		gbManager.setHauteurBoisement("H:/rafcom/test_grain_bocager/hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/rafcom/test_grain_bocager/type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement("H:/rafcom/test_grain_bocager/distance_influence.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrainBocager() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setDistanceInfluenceBoisement("H:/rafcom/test_grain_bocager/distance_influence.tif");
		gbManager.setGrainCellSize(50.0);
		gbManager.setGrainBocager("H:/rafcom/test_grain_bocager/grain_bocager_50m.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	
}
