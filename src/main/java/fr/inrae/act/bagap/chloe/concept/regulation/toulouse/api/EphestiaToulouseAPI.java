package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import fr.inrae.act.bagap.chloe.api.NoParameterException;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse.CubistEphestiaToulouseAnalyse;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse.CubistEphestiaToulouseManager;

public class EphestiaToulouseAPI {

	public static void launchBatch(String file){
		try{
			Properties properties = new Properties();
	        Reader in = new InputStreamReader(new FileInputStream(file), "UTF8");
			properties.load(in);
			in.close();
			
			if(properties.containsKey("treatment")){
				
				long begin = System.currentTimeMillis();
				
				CubistEphestiaToulouseManager manager = new CubistEphestiaToulouseManager(); 
				
				importParameters(manager, properties);
				
				CubistEphestiaToulouseAnalyse analyse = manager.build();
				analyse.run();
				
				long end = System.currentTimeMillis();
				System.out.println("time computing : "+(end - begin));
			}
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private static void importParameters(CubistEphestiaToulouseManager manager, Properties properties) {
		
		try{
		
			importCubistModel(manager, properties);
			importDataCover(manager, properties);
			importDataFarm(manager, properties);
			importSystemFile(manager, properties);
			importIFTFile(manager, properties);
			importMeteoFile(manager, properties);
			importModelOutput(manager, properties);
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void importCubistModel(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("cubist_model")){
			manager.setCubistModel(properties.getProperty("cubist_model"));
			return;
		}
		throw new NoParameterException("cubist_model missing");
	}

	private static void importDataCover(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("data_cover")){
			manager.setDataCover(properties.getProperty("data_cover"));
			return;
		}
		throw new NoParameterException("data_cover missing");
	}

	private static void importDataFarm(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("data_farm")){
			manager.setDataFarm(properties.getProperty("data_farm"));
			return;
		}
		throw new NoParameterException("data_farm missing");
	}

	private static void importSystemFile(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("system_file")){
			manager.setSystemFile(properties.getProperty("system_file"));
			return;
		}
		throw new NoParameterException("system_file missing");
	}

	private static void importIFTFile(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("IFT_file")){
			manager.setIFTFile(properties.getProperty("IFT_file"));
			return;
		}
		throw new NoParameterException("IFT_file missing");
	}

	private static void importMeteoFile(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("meteo_file")){
			manager.setMeteoFile(properties.getProperty("meteo_file"));
			return;
		}
		throw new NoParameterException("meteo_file missing");
	}

	private static void importModelOutput(CubistEphestiaToulouseManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("model_output")){
			manager.setModelOutput(properties.getProperty("model_output"));
			return;
		}
		throw new NoParameterException("model_output missing");
	}
}
