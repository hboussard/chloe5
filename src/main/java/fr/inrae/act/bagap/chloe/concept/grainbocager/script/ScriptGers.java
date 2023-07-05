package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGers {

	public static void main(String[] args){
		//scriptCalculGrainInitial();
		//scriptCalculGrainAmenagement();
		scriptCalculGrainAmenagementAlternatif();
	}
	
	private static void scriptCalculGrainInitial() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setTerritoire(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setSeuils(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setHauteurBoisement(dataPath+"hauteur_boisement_init.tif");
		gbManager.setTypeBoisement(dataPath+"type_boisement_init.tif");
		gbManager.setDistanceInfluenceBoisement(dataPath+"distance_influence_init.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_init_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_init_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrainAmenagement() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setTerritoire(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setSeuils(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setPlantation(dataPath+"creation_haies.shp");
		gbManager.setAttributHauteurPlantation("hauteur");
		gbManager.setHauteurBoisement(dataPath+"hauteur_boisement_amenagement.tif");
		gbManager.setTypeBoisement(dataPath+"type_boisement_amenagement.tif");
		gbManager.setDistanceInfluenceBoisement(dataPath+"distance_influence_amenagement.tif");
		gbManager.setGrainBocager(dataPath+"grain_bocager_amenagement_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"grain_bocager_amenagement_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void scriptCalculGrainAmenagementAlternatif() {
		
		String dataPath = "H:/temp/gers/natais/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setTerritoire(dataPath+"parcellaire exploitation.shp");
		gbManager.setBufferArea(1000);
		gbManager.setSeuils(0.22, 0.3, 0.4);
		gbManager.setBocage("H:/IGN/data/32_2019_5m/mean/");
		gbManager.setPlantation(dataPath+"alternatif/creation_haies.shp");
		gbManager.setAttributHauteurPlantation("hauteur");
		gbManager.setHauteurBoisement(dataPath+"alternatif/hauteur_boisement_alternatif.tif");
		gbManager.setTypeBoisement(dataPath+"alternatif/type_boisement_alternatif.tif");
		gbManager.setDistanceInfluenceBoisement(dataPath+"alternatif/distance_influence_alternatif.tif");
		gbManager.setGrainBocager(dataPath+"alternatif/grain_bocager_alternatif_5m.tif");
		gbManager.setGrainBocager4Classes(dataPath+"alternatif/grain_bocager_alternatif_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
