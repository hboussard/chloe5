package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptRSEINRAE {

	public static void main(String[] args){
		
		//scriptINRAECentreBretagneNormandieInit();
		scriptINRAECentreBretagneNormandieAmenagement();
	}
		
	private static void scriptINRAECentreBretagneNormandieInit() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/rse/data/Donn�esRHEU_Hugues/parcelles_INRA/parcelles_INRAE_LeRheu.shp");
		gbManager.setBufferArea(5000);
		gbManager.setBocage("G:/FDCCA/diag_ea/data/mnhc/");
		//gbManager.setPlantation("H:/rafcom/data/plantations_RAF_2008-2023.shp");
		//gbManager.setAttributHauteurPlantation("code");
		gbManager.setHauteurBoisement("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_distance_influence.tif");
		gbManager.setGrainBocager("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_grain_bocager.tif");
		gbManager.setGrainBocager4Classes("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_grain_bocager_4classes.tif");
		gbManager.setGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_grain_bocager_fonctionnel.tif");
		gbManager.setClusterGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_cluster_grain_bocager_fonctionnel.tif");
		gbManager.setEnjeuxCellSize(50.0);
		gbManager.setProportionGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_proportion_grain_bocager_fonctionnel.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_2020/rse_lerheu_2020_fragmentation_grain_bocager_fonctionnel.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptINRAECentreBretagneNormandieAmenagement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true);
		gbManager.setTerritoire("H:/rse/data/Donn�esRHEU_Hugues/parcelles_INRA/parcelles_INRAE_LeRheu.shp");
		gbManager.setBufferArea(5000);
		gbManager.setBocage("G:/FDCCA/diag_ea/data/mnhc/");
		gbManager.setPlantation("H:/rse/data/rse_lerheu_amenagement_bocage.shp");
		gbManager.setAttributHauteurPlantation("hauteur");
		gbManager.setHauteurBoisement("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_hauteur_boisement.tif");
		gbManager.setTypeBoisement("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_distance_influence.tif");
		gbManager.setGrainBocager("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_grain_bocager.tif");
		gbManager.setGrainBocager4Classes("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_grain_bocager_4classes.tif");
		gbManager.setGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_grain_bocager_fonctionnel.tif");
		gbManager.setClusterGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_cluster_grain_bocager_fonctionnel.tif");
		gbManager.setEnjeuxCellSize(50.0);
		gbManager.setProportionGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_proportion_grain_bocager_fonctionnel.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel("H:/rse/grain_bocager_lerheu_amenagement/rse_lerheu_amenagement_fragmentation_grain_bocager_fonctionnel.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}