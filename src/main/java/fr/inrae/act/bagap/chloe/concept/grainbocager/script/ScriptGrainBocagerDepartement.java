package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerDepartement {

	public static void main(String[] args){
		
		/*
		//procedureGrainBocager("02", "2018");
		//procedureGrainBocager("09", "2022");
		//procedureGrainBocager("11", "2021");
		//procedureGrainBocager("12", "2019");
		//procedureGrainBocager("12", "2022");
		//procedureGrainBocager("14", "2020");
		//procedureGrainBocager("16", "2020");
		//procedureGrainBocager("21", "2020");
		//procedureGrainBocager("22", "2018");
		//procedureGrainBocager("23", "2020");
		//procedureGrainBocager("27", "2019");
		//procedureGrainBocager("29", "2018");
		//procedureGrainBocager("30", "2021");
		//procedureGrainBocager("32", "2019");
		//procedureGrainBocager("32", "2022");
		//procedureGrainBocager("31", "2022");
		//procedureGrainBocager("34", "2021");
		//procedureGrainBocager("35", "2020");
		//procedureGrainBocager("36", "2020");
		//procedureGrainBocager("44", "2020");
		//procedureGrainBocager("46", "2019");
		//procedureGrainBocager("46", "2022");
		//procedureGrainBocager("48", "2021");
		//procedureGrainBocager("49", "2020");
		//procedureGrainBocager("50", "2019");
		//procedureGrainBocager("53", "2019");
		//procedureGrainBocager("56", "2019");
		//procedureGrainBocager("58", "2020");
		//procedureGrainBocager("59", "2018");
		//procedureGrainBocager("61", "2020");
		//procedureGrainBocager("65", "2022");
		//procedureGrainBocager("66", "2021");
		//procedureGrainBocager("71", "2020");
		//procedureGrainBocager("72", "2019");
		//procedureGrainBocager("76", "2019");
		//procedureGrainBocager("79", "2020");
		//procedureGrainBocager("81", "2022");
		//procedureGrainBocager("82", "2022");
		//procedureGrainBocager("85", "2019");
		//procedureGrainBocager("87", "2020");
		procedureGrainBocager("16", "2023");
		procedureGrainBocager("17", "2021");
		procedureGrainBocager("19", "2023");
		procedureGrainBocager("23", "2023");
		procedureGrainBocager("24", "2021");
		procedureGrainBocager("33", "2021");
		procedureGrainBocager("40", "2021");
		procedureGrainBocager("47", "2021");
		procedureGrainBocager("64", "2021");
		procedureGrainBocager("79", "2023");
		procedureGrainBocager("86", "2023");
		procedureGrainBocager("87", "2023");
		// aura
		procedureGrainBocager("01", "2021");
		procedureGrainBocager("03", "2022");
		procedureGrainBocager("07", "2023");
		procedureGrainBocager("15", "2022");
		procedureGrainBocager("26", "2023");
		procedureGrainBocager("38", "2021");
		procedureGrainBocager("42", "2022");
		procedureGrainBocager("43", "2022");
		procedureGrainBocager("63", "2022");
		procedureGrainBocager("69", "2023");
		procedureGrainBocager("73", "2022");
		procedureGrainBocager("74", "2023");
		// grand est
		procedureGrainBocager("08", "2022");
		procedureGrainBocager("10", "2022");
		procedureGrainBocager("51", "2022");
		procedureGrainBocager("52", "2022");
		procedureGrainBocager("54", "2022");
		procedureGrainBocager("55", "2022");
		procedureGrainBocager("57", "2022");
		procedureGrainBocager("67", "2021");
		procedureGrainBocager("68", "2021");
		procedureGrainBocager("88", "2023");
		// bourgogne franche comte
		procedureGrainBocager("21", "2023");
		procedureGrainBocager("25", "2023");
		procedureGrainBocager("39", "2023");
		procedureGrainBocager("58", "2023");
		procedureGrainBocager("70", "2023");
		procedureGrainBocager("71", "2023");
		procedureGrainBocager("89", "2023");
		procedureGrainBocager("90", "2023");
		// centre val de loire
		procedureGrainBocager("18", "2023");
		procedureGrainBocager("28", "2023");
		procedureGrainBocager("36", "2023");
		procedureGrainBocager("37", "2023");
		procedureGrainBocager("41", "2023");
		procedureGrainBocager("45", "2023");
		// haut de france
		procedureGrainBocager("02", "2021");
		procedureGrainBocager("59", "2021");
		procedureGrainBocager("60", "2021");
		procedureGrainBocager("62", "2021");
		procedureGrainBocager("80", "2021");
		// paca
		procedureGrainBocager("04", "2021");
		procedureGrainBocager("05", "2022");
		procedureGrainBocager("06", "2023");
		procedureGrainBocager("13", "2023");
		procedureGrainBocager("83", "2023");
		procedureGrainBocager("84", "2021");
		
		// normandie
		procedureGrainBocager("14", "2023");
		procedureGrainBocager("27", "2022");
		procedureGrainBocager("50", "2022");
		procedureGrainBocager("61", "2023");
		procedureGrainBocager("76", "2022");
		
		//pays de la loire
		procedureGrainBocager("44", "2022");
		procedureGrainBocager("49", "2022");
		procedureGrainBocager("53", "2022");
		procedureGrainBocager("72", "2022");
		procedureGrainBocager("85", "2022");
		
		// corse
		procedureGrainBocager("2A", "2021");
		procedureGrainBocager("2B", "2021");
		
		// bretagne
		procedureGrainBocager("22", "2021");
		procedureGrainBocager("29", "2021");
		procedureGrainBocager("35", "2023");
		procedureGrainBocager("56", "2022");
		
		// ile-de-france
		procedureGrainBocager("75", "2021");
		procedureGrainBocager("77", "2021");
		procedureGrainBocager("78", "2021");
		procedureGrainBocager("91", "2021");
		procedureGrainBocager("92", "2021");
		procedureGrainBocager("93", "2021");
		procedureGrainBocager("94", "2021");
		procedureGrainBocager("95", "2021");
		*/
	}
	
	private static void procedureGrainBocager(String numDep, String annee) {
		
		calculGrainBocager5m(numDep, annee);
		calculGrainBocager50m(numDep, annee);
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
