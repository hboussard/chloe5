package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerDepartement {

	public static void main(String[] args){
		
		//procedureGrainBocager("09", "2022");
		//procedureGrainBocager("11", "2021");
		//procedureGrainBocager("12", "2022");
		//procedureGrainBocager("16", "2020");
		//procedureGrainBocager("22", "2018");
		//procedureGrainBocager("29", "2018");
		//procedureGrainBocager("30", "2021");
		//procedureGrainBocager("32", "2019");
		//procedureGrainBocager("32", "2022");
		//procedureGrainBocager("31", "2022");
		//procedureGrainBocager("34", "2021");
		//procedureGrainBocager("35", "2020");
		//procedureGrainBocager("44", "2020");
		//procedureGrainBocager("46", "2022");
		//procedureGrainBocager("48", "2021");
		//procedureGrainBocager("49", "2020");
		//procedureGrainBocager("53", "2019");
		//procedureGrainBocager("56", "2019");
		//procedureGrainBocager("65", "2022");
		//procedureGrainBocager("66", "2021");
		//procedureGrainBocager("72", "2019");
		//procedureGrainBocager("79", "2020");
		//procedureGrainBocager("81", "2022");
		//procedureGrainBocager("82", "2022");
		//procedureGrainBocager("85", "2019");
		//procedureGrainBocager("87", "2020");
		

		
		//procedureGrainBocager(14, 2020);
		//procedureGrainBocager(23, 2020);
		//procedureGrainBocager(50, 2019);
		//procedureGrainBocager(61, 2020);
		//procedureGrainBocager(87, 2020);
		
		
		
		
	}
	
	private static void procedureGrainBocager(String numDep, String annee) {
		
		//calculGrainBocager5m(numDep, annee);
		//calculGrainBocager50m(numDep, annee);
		calculZoneEnjeux(numDep, annee, 1);
		calculZoneEnjeux(numDep, annee, 5);
		
	}
	
	private static void calculGrainBocager5m(String numDep, String annee) {
		
		String dataPath = "D:/grain_bocager/mnhc/data/";
		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setBocage(dataPath+numDep+"_"+annee+"_5m/");
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}

	private static void calculGrainBocager50m(String numDep, String annee) {
		
		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
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
	
	private static void calculZoneEnjeux(String numDep, String annee, int km) {
		
		int ewr = km * 1000;
		
		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
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
