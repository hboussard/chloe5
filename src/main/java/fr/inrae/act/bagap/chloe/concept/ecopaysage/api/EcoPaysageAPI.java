package fr.inrae.act.bagap.chloe.concept.ecopaysage.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.api.NoParameterException;
import fr.inrae.act.bagap.chloe.api.RasterTypeMime;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;

public class EcoPaysageAPI {

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
				EcoPaysageManager manager = new EcoPaysageManager(treatment); 
				
				importParameters(manager, properties);
				
				EcoPaysageProcedure procedure = manager.build();
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
	
	private static void importParameters(EcoPaysageManager manager, Properties properties) {
		
		try{
		
			importForce(manager, properties);
			importInputRaster(manager, properties);
			importScales(manager, properties);
			importClasses(manager, properties);
			importOutputFolder(manager, properties);
		
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void importForce(EcoPaysageManager manager, Properties properties) {
		if(properties.containsKey("force")){
			boolean force = Boolean.parseBoolean(properties.getProperty("force"));
			manager.setForce(force);
		}
	}
	
	public static void importInputRaster(EcoPaysageManager builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_raster")){
			String prop = properties.getProperty("input_raster").replace("{",  "").replace("}", "");
			String[] rasters = prop.split(";");
			for(String raster : rasters) {
				if(new File(raster).isFile()){
					builder.setInputRaster(raster);
				}	
			}
			return;
		}
		throw new NoParameterException("input_raster");
	}
	
	public static void importScales(EcoPaysageManager builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("scales")){
			String prop = properties.getProperty("scales");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			int[] scales = new int[ms.length];
			for(int i=0; i<ms.length; i++){
				scales[i] = Integer.parseInt(ms[i]);
			}
			builder.setScales(scales);
			return;
		}
		throw new NoParameterException("scales");
	}
	
	public static void importClasses(EcoPaysageManager builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("classes")){
			String prop = properties.getProperty("classes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			int[] classes = new int[ms.length];
			for(int i=0; i<ms.length; i++){
				classes[i] = Integer.parseInt(ms[i]);
			}
			builder.setClasses(classes);
			return;
		}
		throw new NoParameterException("classes");
	}
	
	public static void importOutputFolder(EcoPaysageManager builder, Properties properties) {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			if(!(prop.endsWith("/") || prop.endsWith("\\\\"))){
				prop += "/";
			}
			builder.setOutputFolder(prop);
		}
	}
	
}
