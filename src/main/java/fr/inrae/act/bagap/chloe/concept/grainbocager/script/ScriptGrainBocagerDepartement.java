package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptGrainBocagerDepartement {

	public static void main(String[] args){
		
		scriptCalculGrainBocager(53, 2019);
		
	}
	
	private static void scriptCalculGrainBocager(int numDep, int annee) {
		
		String dataPath = "E:/IGN/data/";
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setBufferArea(0);
		gbManager.setSeuils(0.2, 0.33, 0.45);
		gbManager.setBocage(dataPath+numDep+"_"+annee+"_5m/mean/");
		gbManager.setHauteurBoisement(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setTypeBoisement(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
