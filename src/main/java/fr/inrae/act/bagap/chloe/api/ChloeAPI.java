package fr.inrae.act.bagap.chloe.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import fr.inrae.act.bagap.chloe.WindowAnalysisType;
import fr.inrae.act.bagap.chloe.WindowDistanceType;
import fr.inrae.act.bagap.chloe.WindowShapeType;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysisBuilder;

public class ChloeAPI {

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
				case "sliding" : launchSliding(properties); break;
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

	private static void launchSliding(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(WindowAnalysisType.SLIDING);
				
			importInputRaster(builder, properties);
			importWindowShape(builder, properties);
			importWindowSizes(builder, properties);
			importDisplacement(builder, properties);
			importInterpolation(builder, properties);
			importMetrics(builder, properties);
			importWindowDistanceType(builder, properties);
			importWindowDistanceFunction(builder, properties);
			importFrictionRaster(builder, properties);
			importUnfilters(builder, properties);
			
			// TODO
			//importXOrigin(builder, properties);
			//importYOrigin(builder, properties);
			//importMaximumNoValueRate(builder, properties);
			//importFrictionFile(builder, properties);
			//importFilters(builder, properties);
			/*
			if(properties.containsKey("output_folder")){
				importOutputFolder(properties);
			}else{
				importOutputAscii(properties);
				importOutputCsv(properties);
			}*/
			
			importOutputRaster(builder, properties);
			importOutputCsv(builder, properties);
			
			LandscapeMetricAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	// required 
	public static void importInputRaster(LandscapeMetricAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_raster")){
			String prop = properties.getProperty("input_raster");
			if(new File(prop).isFile()){
				builder.setRasterFile(prop);
				return;
			}
			throw new NoParameterException("input_raster : "+prop);
		}
		throw new NoParameterException("input_raster");
	}
	
	// required
	public static void importWindowSizes(LandscapeMetricAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("sizes")){
			String prop = properties.getProperty("sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			for(String w : ws){
				builder.addWindowSize(Integer.parseInt(w));
			}
			return;
		}
		throw new NoParameterException("sizes");
	}
	
	// required
	public static void importMetrics(LandscapeMetricAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("metrics")){
			String prop = properties.getProperty("metrics");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			for(String m : ms){
				builder.addMetric(m);
			}
			return;
		}
		throw new NoParameterException("metrics");
	}
	
	// not required
	// default parameter : "CIRCLE"
	public static void importWindowShape(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("shape")){
			builder.setWindowShapeType(WindowShapeType.valueOf(properties.getProperty("shape")));
		}		
	}
	
	// not required
	// default parameter : "THRESHOLD"
	public static void importWindowDistanceType(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("distance_type")){
			builder.setWindowDistanceType(WindowDistanceType.valueOf(properties.getProperty("distance_type")));
		}		
	}
	
	// not required
	// default parameter : gaussienne par default
	public static void importWindowDistanceFunction(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("distance_function")){
			builder.setWindowDistanceFunction(properties.getProperty("distance_function"));
		}		
	}
	
	// not required
	// default parameter : "1"
	public static void importDisplacement(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("displacement")){
			builder.setDisplacement(Integer.parseInt(properties.getProperty("displacement")));
		}
	}
	
	// not required
	// default parameter : "FALSE"
	public static void importInterpolation(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("interpolation")){
			builder.setInterpolation(Boolean.parseBoolean(properties.getProperty("interpolation")));
		}
	}
	
	// not required
	public static void importFrictionRaster(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("friction_raster")){
			builder.setRasterFile2(properties.getProperty("friction_raster"));
		}
	}
	
	// not required
	public static void importUnfilters(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("unfilters")){
			String prop = properties.getProperty("unfilters");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			int[] unfilters = new int[fs.length];
			int index=0;
			for(String f : fs){
				unfilters[index++] = Integer.parseInt(f);
			}
			builder.setUnfilters(unfilters);
		}
	}
	
	// not required 
	public static void importOutputRaster(LandscapeMetricAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_raster")){
			String prop = properties.getProperty("output_raster");
			if(prop.endsWith(".asc")){
				builder.addAsciiGridOutput(prop);
			}else if(prop.endsWith(".tif")){
				builder.addGeoTiffOutput(prop);
			}else{
				throw new NoParameterException("output_raster : "+prop);
			}
		}
	}	
		
	// not required 
	public static void importOutputCsv(LandscapeMetricAnalysisBuilder builder, Properties properties) {
		if(properties.containsKey("output_csv")){
			builder.addCsvOutput(properties.getProperty("output_csv"));
		}
	}
	
	// required 
	/*
	public static void importOutputFolder(LandscapeMetricAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			builder.setOutputFolder();
		}
	}
	*/
	
}
