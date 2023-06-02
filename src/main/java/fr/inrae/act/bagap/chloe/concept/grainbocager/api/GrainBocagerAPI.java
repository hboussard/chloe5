package fr.inrae.act.bagap.chloe.concept.grainbocager.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import fr.inrae.act.bagap.chloe.api.NoParameterException;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.farm.DiagnosticGrainBocagerExploitationBuilder;

public class GrainBocagerAPI {

	public static void main(String[] args) {
		if(args[0].endsWith(".properties")){
			launchBatch(args[0]);
		}else{
			throw new IllegalArgumentException("argument "+args[0]+" is not recognize");
		}
	}
	
	private static void launchBatch(String file){
		try{
			Properties properties = new Properties();
	        Reader in = new InputStreamReader(new FileInputStream(file), "UTF8");
			properties.load(in);
			in.close();
			if(properties.containsKey("treatment")){
				String treatment = properties.getProperty("treatment");
				switch(treatment){
				case "diagnostique_exploitation" : launchDiagnosticExploitation(properties); break;
				//case "diagnostique_territoire" : launchDiagnosticTerritoire(properties); break;
				default :
					throw new IllegalArgumentException("treatment "+treatment+" is not implemented yet");
				}
			}
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	private static void launchDiagnosticExploitation(Properties properties) {
		try{
			long begin = System.currentTimeMillis();
			
			DiagnosticGrainBocagerExploitationBuilder builder = new DiagnosticGrainBocagerExploitationBuilder();
			
			importOutputPath(builder, properties);
			importBocage(builder, properties);
			importParcellaire(builder, properties);
			importZoneBocageExploitation(builder, properties);
			importReplantationBocagere(builder, properties);
			importCodeEA(builder, properties);
			importAttributCodeEA(builder, properties);
			importAttributSecteur(builder, properties);
			importSeuil(builder, properties);
			importScenarios(builder, properties);
			
			DiagnosticGrainBocagerExploitation diagEA = builder.build();
			diagEA.run();
			
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	// required
	private static void importScenarios(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("scenarios")){
			String prop = properties.getProperty("scenarios");
			String[] scs = prop.replace("{", "").replace("}", "").split(",");
			for(String sc : scs){
				builder.addScenario(sc);	
			}
			return;
		}
		throw new NoParameterException("scenarios");
	}

	// required 
	private static void importOutputPath(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_path")){
			String prop = properties.getProperty("output_path");
			builder.setOutputPath(prop);
			return;
		}
		throw new NoParameterException("output_path");
	}
	
	// required 
	private static void importBocage(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("bocage")){
			String prop = properties.getProperty("bocage");
			builder.setBocage(prop);
			return;
		}
		throw new NoParameterException("bocage");
	}
	
	// required 
	private static void importParcellaire(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("parcellaire")){
			String prop = properties.getProperty("parcellaire");
			builder.setParcellaire(prop);
			return;
		}
		throw new NoParameterException("parcellaire");
	}
	
	// required 
	private static void importZoneBocageExploitation(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("zone_bocage_exploitation")){
			String prop = properties.getProperty("zone_bocage_exploitation");
			builder.setZoneBocageExploitation(prop);
			return;
		}
		throw new NoParameterException("zone_bocage_exploitation");
	}

	// not required 
	private static void importReplantationBocagere(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("replantation_bocagere")){
			String prop = properties.getProperty("replantation_bocagere");
			builder.setReplantationBocagere(prop);
		}
	}
	
	// required 
	private static void importCodeEA(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("code_exploitation")){
			String prop = properties.getProperty("code_exploitation");
			builder.setCodeEA(prop);
			return;
		}
		throw new NoParameterException("code_exploitation");
	}

	// not required 
	private static void importAttributCodeEA(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("attribut_code_ea")){
			String prop = properties.getProperty("attribut_code_ea");
			builder.setAttributCodeEA(prop);
		}
	}

	// not required 
	private static void importAttributSecteur(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("attribut_secteur")){
			String prop = properties.getProperty("attribut_secteur");
			builder.setAttributSecteur(prop);
		}
	}
	
	// not required 
	private static void importSeuil(DiagnosticGrainBocagerExploitationBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("seuil")){
			String prop = properties.getProperty("seuil");
			builder.setSeuil(Double.parseDouble(prop));
		}
	}
	
}
