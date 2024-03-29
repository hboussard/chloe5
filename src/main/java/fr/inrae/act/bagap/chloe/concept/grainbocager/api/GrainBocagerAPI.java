package fr.inrae.act.bagap.chloe.concept.grainbocager.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class GrainBocagerAPI {

	public static void main(String[] args) {
		if(args[0].endsWith(".properties")){
			launchBatch(args[0]);
		}else{
			throw new IllegalArgumentException("argument "+args[0]+" is not recognize");
		}
	}
	
	public static void launchBatch(String file){
		try{
			Properties properties = new Properties();
	        Reader in = new InputStreamReader(new FileInputStream(file), "UTF8");
			properties.load(in);
			in.close();
			
			if(properties.containsKey("treatment")){
				
				long begin = System.currentTimeMillis();
				
				String treatment = properties.getProperty("treatment");
				GrainBocagerManager manager = new GrainBocagerManager(treatment); 
				
				importParameters(manager, properties);
				
				GrainBocagerProcedure procedure = manager.build();
				procedure.run();
				
				long end = System.currentTimeMillis();
				System.out.println("time computing : "+(end - begin));
			}
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	private static void importParameters(GrainBocagerManager manager, Properties properties) {
		
		importFastMode(manager, properties);
		importForce(manager, properties);
		importTerritory(manager, properties);
		importEnvelope(manager, properties);
		importBufferArea(manager, properties);
		importTileFolder(manager, properties);
		importBocage(manager, properties);
		importWoodRemoval(manager, properties);
		importWoodPlanting(manager, properties);
		importHeightPlantingAttribute(manager, properties);
		importWoodHeight(manager, properties);
		importWoodType(manager, properties);
		importInfluenceDistance(manager, properties);
		importGrainBocagerCellSize(manager, properties);
		importGrainBocagerWindowRadius(manager, properties);
		importThresholds(manager, properties);
		importThreshold(manager, properties);
		importGrainBocager(manager, properties);
		importGrainBocager4Classes(manager, properties);
		importFunctionalGrainBocager(manager, properties);
		importFunctionalGrainBocagerClustering(manager, properties);
		importIssuesCellSize(manager, properties);
		importIssuesWindowRadius(manager, properties);
		importFunctionalGrainBocagerProportion(manager, properties);
		importFunctionalGrainBocagerFragmentation(manager, properties);
		importOutputFolder(manager, properties);
	}

	private static void importTileFolder(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("tile_folder")){
			String prop = properties.getProperty("tile_folder");
			manager.setTile(prop);
		}
	}
	
	private static void importOutputFolder(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			manager.setOutputFolder(prop);
		}
	}
	
	private static void importFastMode(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("fast_mode")){
			boolean fastMode = Boolean.parseBoolean(properties.getProperty("fast_mode"));
			manager.setFastMode(fastMode);
		}
	}
	
	private static void importForce(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("force")){
			boolean force = Boolean.parseBoolean(properties.getProperty("force"));
			manager.setForce(force);
		}
	}
	
	private static void importTerritory(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("territory")){
			String prop = properties.getProperty("territory");
			manager.setTerritory(prop);
		}
	}
	
	private static void importEnvelope(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("envelope")){
			String prop = properties.getProperty("envelope");
			manager.setEnvelope(prop);
		}
	}
	
	private static void importBufferArea(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("buffer_area")){
			String prop = properties.getProperty("buffer_area");
			manager.setBufferArea(Double.parseDouble(prop));
		}
	}
	
	private static void importBocage(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("bocage")){
			String prop = properties.getProperty("bocage");
			manager.setBocage(prop);
		}
	}
	
	private static void importWoodRemoval(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_removal")){
			String prop = properties.getProperty("wood_removal");
			manager.setWoodRemoval(prop);
		}
	}
	
	private static void importWoodPlanting(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_planting")){
			String prop = properties.getProperty("wood_planting");
			manager.setWoodPlanting(prop);
		}
	}
	
	private static void importHeightPlantingAttribute(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("height_planting_attribute")){
			String prop = properties.getProperty("height_planting_attribute");
			manager.setHeightPlantingAttribute(prop);
		}
	}
	
	private static void importWoodHeight(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_height")){
			String prop = properties.getProperty("wood_height");
			manager.setWoodHeight(prop);
		}
	}
	
	private static void importWoodType(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_type")){
			String prop = properties.getProperty("wood_type");
			manager.setWoodType(prop);
		}
	}
	
	private static void importInfluenceDistance(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("influence_distance")){
			String prop = properties.getProperty("influence_distance");
			manager.setInfluenceDistance(prop);
		}
	}
	
	private static void importThresholds(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("thresholds")){
			String prop = properties.getProperty("thresholds");
			String[] s = prop.replace("{", "").replace("}", "").replaceAll(" ", "").split(";");
			double s1 = Double.parseDouble(s[0]);
			double s2 = Double.parseDouble(s[1]);
			double s3 = Double.parseDouble(s[2]);
			manager.setThresholds(s1, s2, s3);
		}
	}
	
	private static void importThreshold(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("threshold")){
			String prop = properties.getProperty("threshold");
			manager.setThreshold(Double.parseDouble(prop));
		}
	}
	
	private static void importGrainBocagerWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_window_radius")){
			String prop = properties.getProperty("grain_bocager_window_radius");
			manager.setGrainBocagerWindowRadius(Double.parseDouble(prop));
		}
	}
	
	private static void importGrainBocagerCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_cellsize")){
			String prop = properties.getProperty("grain_bocager_cellsize");
			manager.setGrainBocagerCellSize(Double.parseDouble(prop));
		}
	}
	
	private static void importGrainBocager(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager")){
			String prop = properties.getProperty("grain_bocager");
			manager.setGrainBocager(prop);
		}
	}
	
	private static void importGrainBocager4Classes(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_4classes")){
			String prop = properties.getProperty("grain_bocager_4classes");
			manager.setGrainBocager4Classes(prop);
		}
	}
	
	private static void importFunctionalGrainBocager(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager")){
			String prop = properties.getProperty("functional_grain_bocager");
			manager.setFunctionalGrainBocager(prop);
		}
	}
	
	private static void importFunctionalGrainBocagerClustering(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_clustering")){
			String prop = properties.getProperty("functional_grain_bocager_clustering");
			manager.setFunctionalGrainBocagerClustering(prop);
		}
	}
	
	private static void importIssuesWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("issues_window_radius")){
			String prop = properties.getProperty("issues_window_radius");
			manager.setIssuesWindowRadius(Double.parseDouble(prop));
		}
	}
	
	private static void importIssuesCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("issues_cellsize")){
			String prop = properties.getProperty("issues_cellsize");
			manager.setIssuesCellSize(Double.parseDouble(prop));
		}
	}
	
	private static void importFunctionalGrainBocagerProportion(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_proportion")){
			String prop = properties.getProperty("functional_grain_bocager_proportion");
			manager.setFunctionalGrainBocagerProportion(prop);
		}
	}
	
	private static void importFunctionalGrainBocagerFragmentation(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_fragmentation")){
			String prop = properties.getProperty("functional_grain_bocager_fragmentation");
			manager.setFunctionalGrainBocagerFragmentation(prop);
		}
	}
	/*
	private static void importZoneBocage(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("zone_bocage")){
			String prop = properties.getProperty("zone_bocage");
			manager.setZoneBocage(prop);
		}
	}

	private static void importCodeEA(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("code_exploitation")){
			String prop = properties.getProperty("code_exploitation");
			manager.addCodeEA(prop);
		}
	}

	private static void importAttributCodeEA(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("attribut_code_ea")){
			String prop = properties.getProperty("attribut_code_ea");
			manager.setAttributCodeEA(prop);
		}
	}

	private static void importAttributSecteur(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("attribut_secteur")){
			String prop = properties.getProperty("attribut_secteur");
			manager.setAttributSecteur(prop);
		}
	}
	
	private static void importScenarios(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("scenarios")){
			String prop = properties.getProperty("scenarios");
			String[] scs = prop.replace("{", "").replace("}", "").split(",");
			for(String sc : scs){
				manager.addScenario(sc);	
			}
		}
	}
	*/
	
}
