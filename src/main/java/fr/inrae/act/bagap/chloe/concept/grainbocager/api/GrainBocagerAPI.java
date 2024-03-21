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
				
				/*
				switch(treatment){
				case "recuperation_hauteur_boisement" : importRecuperationHauteurBoisement(manager, properties); break;
				case "detection_type_boisement" : importDetectionTypeBoisement(manager, properties); break;
				case "calcul_distance_influence_boisement" : importCalculDistanceInfluenceBoisement(manager, properties); break;
				case "calcul_grain_bocager" : importCalculGrainBocager(manager, properties); break;
				case "calcul_enjeux_globaux" : importCalculEnjeuxGlobaux(manager, properties); break;
				//case "diagnostique_territoire" : importDiagnosticTerritoire(manager, properties); break;
				//case "diagnostique_exploitation" : importDiagnosticExploitation(manager, properties); break;
				default :
					throw new IllegalArgumentException("treatment "+treatment+" is not implemented yet");
				}
				*/
				
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
		//importName(manager, properties);
	}

	private static boolean importTileFolder(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("tile_folder")){
			String prop = properties.getProperty("tile_folder");
			manager.setTile(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importOutputFolder(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			manager.setOutputFolder(prop);
			return true;
		}
		return false;
	}
	
	/*
	private static boolean importName(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("name")){
			String prop = properties.getProperty("name");
			manager.setName(prop);
			return true;
		}
		return false;
	}
	*/
	
	private static boolean importFastMode(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("fast_mode")){
			boolean fastMode = Boolean.parseBoolean(properties.getProperty("fast_mode"));
			manager.setFastMode(fastMode);
			return true;
		}
		return false;
	}
	
	private static boolean importForce(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("force")){
			boolean force = Boolean.parseBoolean(properties.getProperty("force"));
			manager.setForce(force);
			return true;
		}
		return false;
	}
	
	private static boolean importTerritory(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("territory")){
			String prop = properties.getProperty("territory");
			manager.setTerritory(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importEnvelope(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("envelope")){
			String prop = properties.getProperty("envelope");
			manager.setEnvelope(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importBufferArea(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("buffer_area")){
			String prop = properties.getProperty("buffer_area");
			manager.setBufferArea(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importBocage(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("bocage")){
			String prop = properties.getProperty("bocage");
			manager.setBocage(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importWoodRemoval(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_removal")){
			String prop = properties.getProperty("wood_removal");
			manager.setWoodRemoval(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importWoodPlanting(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_planting")){
			String prop = properties.getProperty("wood_planting");
			manager.setWoodPlanting(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importHeightPlantingAttribute(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("height_planting_attribute")){
			String prop = properties.getProperty("height_planting_attribute");
			manager.setHeightPlantingAttribute(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importWoodHeight(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_height")){
			String prop = properties.getProperty("wood_height");
			manager.setWoodHeight(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importWoodType(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("wood_type")){
			String prop = properties.getProperty("wood_type");
			manager.setWoodType(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importInfluenceDistance(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("influence_distance")){
			String prop = properties.getProperty("influence_distance");
			manager.setInfluenceDistance(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importThresholds(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("thresholds")){
			String prop = properties.getProperty("thresholds");
			String[] s = prop.replace("{", "").replace("}", "").replaceAll(" ", "").split(";");
			double s1 = Double.parseDouble(s[0]);
			double s2 = Double.parseDouble(s[1]);
			double s3 = Double.parseDouble(s[2]);
			manager.setThresholds(s1, s2, s3);
			return true;
		}
		return false;
	}
	
	private static boolean importThreshold(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("threshold")){
			String prop = properties.getProperty("threshold");
			manager.setThreshold(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importGrainBocagerWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_window_radius")){
			String prop = properties.getProperty("grain_bocager_window_radius");
			manager.setGrainBocagerWindowRadius(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importGrainBocagerCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_cellsize")){
			String prop = properties.getProperty("grain_bocager_cellsize");
			manager.setGrainBocagerCellSize(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importGrainBocager(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager")){
			String prop = properties.getProperty("grain_bocager");
			manager.setGrainBocager(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importGrainBocager4Classes(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_4classes")){
			String prop = properties.getProperty("grain_bocager_4classes");
			manager.setGrainBocager4Classes(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importFunctionalGrainBocager(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager")){
			String prop = properties.getProperty("functional_grain_bocager");
			manager.setFunctionalGrainBocager(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importFunctionalGrainBocagerClustering(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_clustering")){
			String prop = properties.getProperty("functional_grain_bocager_clustering");
			manager.setFunctionalGrainBocagerClustering(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importIssuesWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("issues_window_radius")){
			String prop = properties.getProperty("issues_window_radius");
			manager.setIssuesWindowRadius(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importIssuesCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("issues_cellsize")){
			String prop = properties.getProperty("issues_cellsize");
			manager.setIssuesCellSize(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importFunctionalGrainBocagerProportion(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_proportion")){
			String prop = properties.getProperty("functional_grain_bocager_proportion");
			manager.setFunctionalGrainBocagerProportion(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importFunctionalGrainBocagerFragmentation(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("functional_grain_bocager_fragmentation")){
			String prop = properties.getProperty("functional_grain_bocager_fragmentation");
			manager.setFunctionalGrainBocagerFragmentation(prop);
			return true;
		}
		return false;
	}
	/*
	private static boolean importZoneBocage(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("zone_bocage")){
			String prop = properties.getProperty("zone_bocage");
			manager.setZoneBocage(prop);
			return true;
		}
		return false;
	}

	private static boolean importCodeEA(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("code_exploitation")){
			String prop = properties.getProperty("code_exploitation");
			manager.addCodeEA(prop);
			return true;
		}
		return false;
	}

	private static boolean importAttributCodeEA(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("attribut_code_ea")){
			String prop = properties.getProperty("attribut_code_ea");
			manager.setAttributCodeEA(prop);
			return true;
		}
		return false;
	}

	private static boolean importAttributSecteur(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("attribut_secteur")){
			String prop = properties.getProperty("attribut_secteur");
			manager.setAttributSecteur(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importScenarios(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("scenarios")){
			String prop = properties.getProperty("scenarios");
			String[] scs = prop.replace("{", "").replace("}", "").split(",");
			for(String sc : scs){
				manager.addScenario(sc);	
			}
			return true;
		}
		return false;
	}
	*/
	/*
	 * private static void importRecuperationHauteurBoisement(GrainBocagerManager manager, Properties properties) {
		
		importTerritoire(manager, properties);
		importBufferArea(manager, properties);
		importBocage(manager, properties);
		importOutputPath(manager, properties);
		importOutputFile(manager, properties);
		importPlantation(manager, properties);
		importAttributHauteurPlantation(manager, properties);
		importArrachage(manager, properties);
		
	}

	private static void importDetectionTypeBoisement(GrainBocagerManager manager, Properties properties) {
		
		importTerritoire(manager, properties);
		importBufferArea(manager, properties);
		importBocage(manager, properties);
		importOutputPath(manager, properties);
		importOutputFile(manager, properties);
		importModeFast(manager, properties);
		
	}
	
	private static void importCalculDistanceInfluenceBoisement(GrainBocagerManager manager, Properties properties) {
		
		importBocage(manager, properties);
		importTypeBoisement(manager, properties);
		importOutputPath(manager, properties);
		importOutputFile(manager, properties);
		importModeFast(manager, properties);
		
	}
	
	private static void importCalculGrainBocager(GrainBocagerManager manager, Properties properties) {
		
		importBocage(manager, properties);
		importTypeBoisement(manager, properties);
		importDistanceInfluenceBoisement(manager, properties);
		importOutputPath(manager, properties);
		importOutputFile(manager, properties);
		importModeFast(manager, properties);
		importWindowRadius(manager, properties);
		importOutputCellSize(manager, properties);
		
	}
	
	private static void importCalculEnjeuxGlobaux(GrainBocagerManager manager, Properties properties) {
		
	}

	private static void importDiagnosticTerritoire(GrainBocagerManager manager, Properties properties) {
		
	}
	
	private static void importDiagnosticExploitation(GrainBocagerManager manager, Properties properties) {
		
		importOutputPath(manager, properties);
		importBocage(manager, properties);
		importTerritoire(manager, properties);
		importZoneBocage(manager, properties);
		importPlantation(manager, properties);
		importCodeEA(manager, properties);
		importAttributCodeEA(manager, properties);
		importAttributSecteur(manager, properties);
		importSeuil(manager, properties);
		importScenarios(manager, properties);
		
	}
	 */
	
}
