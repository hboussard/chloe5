package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerDepartement {

	public static void main(String[] args){
		
		
		//calculGrainBocager5m(22, 2018);
		//calculGrainBocager5m(29, 2018);
		//calculGrainBocager5m(35, 2020);
		//calculGrainBocager5m(53, 2019);
		//calculGrainBocager5m(56, 2019);
		//calculGrainBocager5m(14, 2020);
		//calculGrainBocager5m(61, 2020);
		
		//calculGrainBocager50m(14, 2020);
		//calculGrainBocager50m(61, 2020);
		//calculGrainBocager50m(22, 2018);
		//calculGrainBocager50m(29, 2018);
		//calculGrainBocager50m(35, 2020);
		//calculGrainBocager50m(56, 2019);
		
		//calculZoneEnjeux(14, 2020, 1);
		//calculZoneEnjeux(61, 2020, 1);
		//calculZoneEnjeux(22, 2018, 1);
		//calculZoneEnjeux(29, 2018, 1);
		//calculZoneEnjeux(35, 2020, 1);
		//calculZoneEnjeux(56, 2019, 1);
		
		//calculZoneEnjeux(14, 2020, 5);
		//calculZoneEnjeux(61, 2020, 5);
		//calculZoneEnjeux(22, 2018, 5);
		//calculZoneEnjeux(29, 2018, 5);
		//calculZoneEnjeux(35, 2020, 5);
		//calculZoneEnjeux(56, 2019, 5);
	}
	
	private static void calculGrainBocager5m(int numDep, int annee) {
		
		String dataPath = "E:/IGN/data/";
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setBocage(dataPath+numDep+"_"+annee+"_5m/mean/");
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}

	private static void calculGrainBocager50m(int numDep, int annee) {
		
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculZoneEnjeux(int numDep, int annee, int km) {
		
		int ewr = km * 1000;
		
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setFastMode(true); 
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setFunctionalGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_fonctionnel_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_cluster_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
