package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.script;

import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse.CubistEphestiaToulouseAnalyse;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse.CubistEphestiaToulouseManager;

public class ScriptEphestiaToulouse {

	public static void main(String[] args) {
		
		String path = "C:/Data/projet/living_lab_cascogne/data/";
		
		CubistEphestiaToulouseManager manager = new CubistEphestiaToulouseManager();
		
		manager.setCubistModel(path+"CUB_rules_Toulouse_Ephestia_2023-01-09.csv");
		manager.setDataCover(path+"cover.tif");
		manager.setDataFarm(path+"farm.tif");
		manager.setSystemFile(path+"systems.csv");
		manager.setIFTFile(path+"covers_ift.txt");
		manager.setMeteoFile(path+"meteo.csv");
		manager.setModelOutput(path+"model_ephestia_toulouse.tif");
		
		CubistEphestiaToulouseAnalyse analyse = manager.build();
		
		analyse.run();
		
	}

}
