package fr.inrae.act.bagap.chloe.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.concept.grainbocager.api.GrainBocagerAPI;
import fr.inrae.act.bagap.chloe.util.analysis.ChloeUtilAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

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
			if(properties.containsKey("procedure")){
				String procedure = properties.getProperty("procedure");
				switch(procedure){
				case "grain_bocager" : GrainBocagerAPI.launchBatch(file); break;
				default :
					throw new IllegalArgumentException("procedure "+procedure+" is not implemented yet");
				}
			}else if(properties.containsKey("treatment")){
				String treatment = properties.getProperty("treatment");
				switch(treatment){
				case "sliding" : launchSliding(properties); break;
				case "selected" : launchSelected(properties); break;
				case "combine" : launchCombine(properties); break;
				case "search_and_replace" : launchSearchAndReplace(properties); break;
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
			
			ChloeAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.SLIDING);
				
			importInputRaster(builder, properties);
			importWindowShape(builder, properties);
			importWindowSizes(builder, properties);
			importDisplacement(builder, properties);
			importInterpolation(builder, properties);
			importMetrics(builder, properties);
			importWindowDistanceType(builder, properties);
			importWindowDistanceFunction(builder, properties);
			importFrictionRaster(builder, properties);
			importFilters(builder, properties);
			importUnfilters(builder, properties);
			importMaximumNoValueRate(builder, properties);
			
			//importXOrigin(builder, properties); // finalement abandonné
			//importYOrigin(builder, properties); // finalement abandonné
			//importFrictionFile(builder, properties); // finalement abandonné
			
			if(properties.containsKey("output_folder")){
				importOutputFolderForCenteredWindow(builder, properties);
			}else{
				importOutputRaster(builder, properties);
				importOutputCsv(builder, properties);
			}
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchSelected(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.SELECTED);
				
			importInputRaster(builder, properties);
			importWindowShape(builder, properties);
			importWindowSizes(builder, properties);
			importMetrics(builder, properties);
			importWindowDistanceType(builder, properties);
			importWindowDistanceFunction(builder, properties);
			importFrictionRaster(builder, properties);
			importPointsFilter(builder, properties);
			importPixelsFilter(builder, properties);
			importWindowsPath(builder, properties);
			
			if(properties.containsKey("output_folder")){
				importOutputFolderForCenteredWindow(builder, properties);
			}else{
				importOutputRaster(builder, properties);
				importOutputCsv(builder, properties);
			}
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchCombine(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.COMBINE);
				
			importCombination(builder, properties);
			importInputNamesAndRasters(builder, properties);
			importOutputRaster(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchSearchAndReplace(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.SEARCHANDREPLACE);
				
			importInputRaster(builder, properties);
			importChanges(builder, properties);
			importNoDataValue(builder, properties);
			importOutputRaster(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	// required 
	public static void importInputRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_raster")){
			String prop = properties.getProperty("input_raster");
			if(new File(prop).isFile()){
				builder.setRasterFile(prop);
				return;
			}
		}
		throw new NoParameterException("input_raster");
	}
	
	// required
	public static void importWindowSizes(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
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
	public static void importMetrics(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
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
	public static void importWindowShape(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("shape")){
			builder.setWindowShapeType(WindowShapeType.valueOf(properties.getProperty("shape")));
		}		
	}
	
	// not required
	// default parameter : "THRESHOLD"
	public static void importWindowDistanceType(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_type")){
			builder.setWindowDistanceType(WindowDistanceType.valueOf(properties.getProperty("distance_type")));
		}		
	}
	
	// not required
	// default parameter : gaussienne par default
	public static void importWindowDistanceFunction(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_function")){
			builder.setWindowDistanceFunction(properties.getProperty("distance_function"));
		}		
	}
	
	// not required
	// default parameter : "1"
	public static void importDisplacement(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("displacement")){
			builder.setDisplacement(Integer.parseInt(properties.getProperty("displacement")));
		}
	}
	
	// not required
	// default parameter : "FALSE"
	public static void importInterpolation(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("interpolation")){
			builder.setInterpolation(Boolean.parseBoolean(properties.getProperty("interpolation")));
		}
	}
	
	// not required
	public static void importFrictionRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("friction_raster")){
			builder.setRasterFile2(properties.getProperty("friction_raster"));
		}
	}
	
	// not required
	public static void importFilters(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("filters")){
			String prop = properties.getProperty("filters");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			int[] filters = new int[fs.length];
			int index=0;
			for(String f : fs){
				filters[index++] = Integer.parseInt(f);
			}
			builder.setFilters(filters);
		}
	}
	
	// not required
	public static void importUnfilters(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
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
	public static void importOutputRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
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
	public static void importOutputCsv(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_csv")){
			builder.addCsvOutput(properties.getProperty("output_csv"));
		}
	}
	
	// not required 
	public static void importWindowsPath(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("windows_path")){
			builder.setWindowsPath(properties.getProperty("windows_path"));
		}
	}
	
	// not required
	public static void importOutputFolderForCenteredWindow(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			String input = properties.getProperty("input_raster");
			if(input.endsWith(".asc")){
				builder.addAsciiGridFolderOutput(prop);
			}else if(input.endsWith(".tif")){
				builder.addGeoTiffFolderOutput(prop);
			}else{
				throw new NoParameterException("output_raster : "+input+" extension problem");
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(new File(input).getName().replace(".asc", "").replace(".tif", "")); // file name, assume it exists
			if(builder.getWindowShapeType() == WindowShapeType.CIRCLE){
				sb.append("_cr");
			}else if(builder.getWindowShapeType() == WindowShapeType.SQUARE){
				sb.append("_sq");
			}else if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
				sb.append("_fu");
			}
			sb.append("_w"+builder.getWindowSize());
			sb.append("_d_"+builder.getDisplacement());
			sb.append(".csv");
			
			builder.addCsvOutput(prop+sb.toString());
		}
	}
	
	// not required
	public static void importXOrigin(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("x_origin")){
			builder.setROIX(Integer.parseInt(properties.getProperty("x_origin")));
		}
	}
		
	// not required
	public static void importYOrigin(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("y_origin")){
			builder.setROIY(Integer.parseInt(properties.getProperty("y_origin")));
		}
	}
	
	// not required
	public static void importMaximumNoValueRate(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("maximum_rate_nodata_value")){
			builder.setMinRate(1.0 / (Double.parseDouble(properties.getProperty("maximum_rate_nodata_value"))/100.0));
		}
	}
	
	// not required
	public static void importPointsFilter(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("points")){
			builder.setPointsFilter(properties.getProperty("points"));
		}
	}
	
	// not required
	public static void importPixelsFilter(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("pixels")){
			builder.setPointsFilter(properties.getProperty("pixels"));
		}
	}
	
	// required 
	public static void importInputNamesAndRasters(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
		
			Map<String, String> factors = new TreeMap<String, String>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				factors.put(dd[1], dd[0]);
			}
			
			builder.setNamesAndRasters(factors);
			
			return;
		}
		throw new NoParameterException("factors");
	}
	
	// required 
	private static void importCombination(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("combination")){
			builder.setCombination(properties.getProperty("combination"));
			
			return;
		}
		throw new NoParameterException("combination");
	}
	
	// required 
	public static void importChanges(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("changes")){
			String prop = properties.getProperty("changes").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			Map<Float, Float> changes = new HashMap<Float, Float>();
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				changes.put(Float.parseFloat(vv[0]), Float.parseFloat(vv[1]));
			}
			builder.setChanges(changes);
			
			return;
		}
		throw new NoParameterException("changes");
	}
	
	// not required
	public static void importNoDataValue(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("nodata_value")){
			builder.setNoDataValue(Integer.parseInt(properties.getProperty("nodata_value")));
		}
	}
	
}
