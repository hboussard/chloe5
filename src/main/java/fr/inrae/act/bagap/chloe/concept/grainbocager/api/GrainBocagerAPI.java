package fr.inrae.act.bagap.chloe.concept.grainbocager.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.raster.Tile;

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
			}
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	private static void importParameters(GrainBocagerManager manager, Properties properties) {
		
		importTile(manager, properties);
		importOutputPath(manager, properties);
		importName(manager, properties);
		importModeFast(manager, properties);
		importTerritoire(manager, properties);
		importEnveloppe(manager, properties);
		importBufferArea(manager, properties);
		importBocage(manager, properties);
		importSuppression(manager, properties);
		importPlantation(manager, properties);
		importAttributHauteurPlantation(manager, properties);
		importHauteurBoisement(manager, properties);
		importTypeBoisement(manager, properties);
		importDistanceInfluence(manager, properties);
		importGrainCellSize(manager, properties);
		importGrainWindowRadius(manager, properties);
		importSeuils(manager, properties);
		importGrainBocager(manager, properties);
		importGrainBocager4Classes(manager, properties);
		importSeuil(manager, properties);
		importGrainBocagerFonctionnel(manager, properties);
		importClusterFonctionnel(manager, properties);
		importEnjeuxCellSize(manager, properties);
		importEnjeuxWindowRadius(manager, properties);
		importProportionGrainBocagerFonctionnel(manager, properties);
		importZoneFragmentationGrainBocagerFonctionnel(manager, properties);
	
	}

	private static boolean importTile(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("tuilage")){
			String prop = properties.getProperty("tuilage");
			manager.setTile(Tile.getTile(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importOutputPath(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("output_path")){
			String prop = properties.getProperty("output_path");
			manager.setOutputPath(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importName(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("name")){
			String prop = properties.getProperty("name");
			manager.setName(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importModeFast(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("mode_fast")){
			boolean modeFast = Boolean.parseBoolean(properties.getProperty("mode_fast"));
			manager.setModeFast(modeFast);
			return true;
		}
		return false;
	}
	
	private static boolean importTerritoire(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("territoire")){
			String prop = properties.getProperty("territoire");
			manager.setTerritoire(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importEnveloppe(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("enveloppe")){
			String prop = properties.getProperty("enveloppe");
			manager.setEnveloppe(prop);
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
	
	private static boolean importSuppression(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("suppression")){
			String prop = properties.getProperty("suppression");
			manager.setSuppression(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importPlantation(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("plantation")){
			String prop = properties.getProperty("plantation");
			manager.setPlantation(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importAttributHauteurPlantation(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("attribut_hauteur_plantation")){
			String prop = properties.getProperty("attribut_hauteur_plantation");
			manager.setAttributHauteurPlantation(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importHauteurBoisement(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("hauteur_boisement")){
			String prop = properties.getProperty("hauteur_boisement");
			manager.setHauteurBoisement(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importTypeBoisement(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("type_boisement")){
			String prop = properties.getProperty("type_boisement");
			manager.setTypeBoisement(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importDistanceInfluence(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("distance_influence")){
			String prop = properties.getProperty("distance_influence");
			manager.setDistanceInfluenceBoisement(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importSeuils(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("seuil")){
			String prop = properties.getProperty("seuil");
			String[] s = prop.replace("{", "").replace("}", "").replaceAll(" ", "").split(";");
			double s1 = Double.parseDouble(s[0]);
			double s2 = Double.parseDouble(s[1]);
			double s3 = Double.parseDouble(s[2]);
			manager.setSeuils(s1, s2, s3);
			return true;
		}
		return false;
	}
	
	private static boolean importGrainWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("window_radius")){
			String prop = properties.getProperty("window_radius");
			manager.setGrainWindowRadius(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importGrainCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_cellsize")){
			String prop = properties.getProperty("grain_cellsize");
			manager.setGrainCellSize(Double.parseDouble(prop));
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
	
	private static boolean importSeuil(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("seuil")){
			String prop = properties.getProperty("seuil");
			manager.setSeuil(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importGrainBocagerFonctionnel(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("grain_bocager_fonctionnel")){
			String prop = properties.getProperty("grain_bocager_fonctionnel");
			manager.setGrainBocagerFonctionnel(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importClusterFonctionnel(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("clusterisation_grain_bocager_fonctionnel")){
			String prop = properties.getProperty("clusterisation_grain_bocager_fonctionnel");
			manager.setClusterGrainBocagerFonctionnel(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importEnjeuxWindowRadius(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("enjeux_window_radius")){
			String prop = properties.getProperty("enjeux_window_radius");
			manager.setEnjeuxWindowRadius(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importEnjeuxCellSize(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("enjeux_cellsize")){
			String prop = properties.getProperty("enjeux_cellsize");
			manager.setEnjeuxCellSize(Double.parseDouble(prop));
			return true;
		}
		return false;
	}
	
	private static boolean importProportionGrainBocagerFonctionnel(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("proportion_grain_bocager_fonctionnel")){
			String prop = properties.getProperty("proportion_grain_bocager_fonctionnel");
			manager.setProportionGrainBocagerFonctionnel(prop);
			return true;
		}
		return false;
	}
	
	private static boolean importZoneFragmentationGrainBocagerFonctionnel(GrainBocagerManager manager, Properties properties) {
		if(properties.containsKey("fragmentation_grain_bocager_fonctionnel")){
			String prop = properties.getProperty("fragmentation_grain_bocager_fonctionnel");
			manager.setZoneFragmentationGrainBocagerFonctionnel(prop);
			return true;
		}
		return false;
	}
	
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
