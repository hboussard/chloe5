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

import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.cluster.ClusterType;
import fr.inrae.act.bagap.chloe.concept.grainbocager.api.GrainBocagerAPI;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceType;
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
				case "grid" : launchGrid(properties); break;
				case "map" : launchMap(properties); break;
				case "entity" : launchEntity(properties); break;
				case "combine" : launchCombine(properties); break;
				case "search_and_replace" : launchSearchAndReplace(properties); break;
				case "classification" : launchClassification(properties); break;
				case "raster_from_csv" : launchRasterFromCsv(properties); break;
				case "raster_from_shapefile" : launchRasterFromShapefile(properties); break;
				case "distance" : launchDistance(properties); break;
				case "cluster" : launchCluster(properties); break;
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
			
			if(properties.containsKey("output_folder")){
				importTypeMime(builder, properties);
				importOutputFolderForWindow(builder, properties);
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
			importWindowsPath(builder, properties);
			
			if(properties.containsKey("output_folder")){
				importTypeMime(builder, properties);
				importOutputFolderForWindow(builder, properties);
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
	
	private static void launchGrid(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.GRID);
				
			importInputRaster(builder, properties);
			importWindowSizes(builder, properties);
			importMetrics(builder, properties);
			importMaximumNoValueRate(builder, properties);
			
			if(properties.containsKey("output_folder")){
				importTypeMime(builder, properties);
				importOutputFolderForWindow(builder, properties);
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
	
	private static void launchMap(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.MAP);
				
			importInputRaster(builder, properties);
			importMetrics(builder, properties);
			importOutputCsv(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchEntity(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.ENTITY);
				
			importInputRaster(builder, properties);
			importEntityRaster(builder, properties);
			importMetrics(builder, properties);
			
			if(properties.containsKey("output_folder")){
				importTypeMime(builder, properties);
				importOutputFolderForWindow(builder, properties);
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
	
	private static void launchClassification(Properties properties) {
		
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.CLASSIFICATION);
				
			importInputRaster(builder, properties);
			importDomains(builder, properties);
			importOutputRaster(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchRasterFromCsv(Properties properties) {
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.RASTER_FROM_CSV);
				
			importInputCsv(builder, properties);
			importOutputRaster(builder, properties);
			importOutputPrefix(builder, properties);
			importOutputFolder(builder, properties);
			importOutputSuffix(builder, properties);
			importTypeMime(builder, properties);
			importVariables(builder, properties);
			importWidth(builder, properties);
			importHeight(builder, properties);
			importXMin(builder, properties);
			importYMin(builder, properties);
			importCellSize(builder, properties);
			importNoDataValue(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchRasterFromShapefile(Properties properties) {
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.RASTER_FROM_SHAPEFILE);
				
			importInputShapefile(builder, properties);
			importOutputRaster(builder, properties);
			importAttribute(builder, properties);
			importXMin(builder, properties);
			importXMax(builder, properties);
			importYMin(builder, properties);
			importYMax(builder, properties);
			importCellSize(builder, properties);
			importNoDataValue(builder, properties);
			importFillValue(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchDistance(Properties properties) {
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.DISTANCE);
				
			importInputRaster(builder, properties);
			importOutputRaster(builder, properties);
			importFrictionRaster(builder, properties);
			importDistanceSources(builder, properties);
			importDistanceType(builder, properties);
			importMaxDistance(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchCluster(Properties properties) {
		try{
			long begin = System.currentTimeMillis();
			
			ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.CLUSTER);
				
			importInputRaster(builder, properties);
			importClusterType(builder, properties);
			importClusterSources(builder, properties);
			importDistanceRaster(builder, properties);
			importMaxDistance(builder, properties);
			importOutputCsv(builder, properties);
			importOutputRaster(builder, properties);
			
			ChloeAnalysis analysis = builder.build();
			analysis.allRun();
				
			long end = System.currentTimeMillis();
			System.out.println("time computing : "+(end - begin));
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	// importations

	// required 
	private static void importAttribute(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("attribute")){
			String prop = properties.getProperty("attribute");
			builder.setAttribute(prop);
			return;
		}
		throw new NoParameterException("attribute");
	}

	// required 
	public static void importInputRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_raster")){
			String prop = properties.getProperty("input_raster").replace("{",  "").replace("}", "");
			String[] rasters = prop.split(";");
			boolean ok = false;
			for(String raster : rasters) {
				if(new File(raster).isFile()){
					builder.addRasterFile(raster);
					ok = true;
				}	
			}
			if(ok) {
				return;	
			}
		}
		throw new NoParameterException("input_raster");
	}
	
	// required 
	public static void importEntityRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("entity_raster")){
			String prop = properties.getProperty("entity_raster");
			if(new File(prop).isFile()){
				builder.setEntityRasterFile(prop);
				return;
			}
		}
		throw new NoParameterException("entity_raster");
	}
	
	// required 
	public static void importInputShapefile(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_shapefile")){
			String prop = properties.getProperty("input_shapefile");
			if(new File(prop).isFile() && prop.endsWith(".shp")){
				builder.setShapefile(prop);
				return;
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	// required 
	public static void importInputCsv(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_csv")){
			String prop = properties.getProperty("input_csv");
			if(new File(prop).isFile()){
				builder.setCsvFile(prop);
				return;
			}
		}
		throw new NoParameterException("input_csv");
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
	private static void importDistanceSources(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_sources")){
			String prop = properties.getProperty("distance_sources");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ds = prop.split(";");
			for(String s : ds){
				builder.addSource(Integer.parseInt(s));
			}
			return;
		}
		throw new NoParameterException("distance_sources");
	}
	
	// required 
	private static void importClusterSources(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_sources")){
			String prop = properties.getProperty("cluster_sources");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ds = prop.split(";");
			for(String s : ds){
				builder.addSource(Integer.parseInt(s));
			}
			return;
		}
		throw new NoParameterException("cluster_sources");
	}
	
	// required 
	private static void importDistanceType(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_type")){
			builder.setDistanceType(DistanceType.valueOf(properties.getProperty("distance_type").toUpperCase()));
			return;
		}
		throw new NoParameterException("distance_type");
	}
	
	// required 
	private static void importClusterType(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_type")){
			builder.setClusterType(ClusterType.valueOf(properties.getProperty("cluster_type").toUpperCase()));
			return;
		}
		throw new NoParameterException("cluster_type");
	}
	
	// not required
	private static void importMaxDistance(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("max_distance")){
			String prop = properties.getProperty("max_distance");
			builder.setMaxDistance(Float.parseFloat(prop));
		}
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
	
	// required
	public static void importVariables(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("variables")){
			String prop = properties.getProperty("variables");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			for(String m : ms){
				builder.addVariable(m);
			}
			return;
		}
		throw new NoParameterException("variables");
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
	public static void importDistanceRaster(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_raster")){
			builder.setRasterFile2(properties.getProperty("distance_raster"));
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
			String prop = properties.getProperty("windows_path");
			if(!(prop.endsWith("/") || prop.endsWith("\\\\"))){
				prop += "/";
			}
			builder.setWindowsPath(prop);
		}
	}
	
	// not required 
	public static void importOutputPrefix(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_prefix")){
			builder.setOutputPrefix(properties.getProperty("output_prefix"));
		}
	}
	
	// not required 
	public static void importOutputFolder(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			if(!(prop.endsWith("/") || prop.endsWith("\\\\"))){
				prop += "/";
			}
			builder.setOutputFolder(prop);
		}
	}
	
	// not required 
	public static void importOutputSuffix(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_suffix")){
			builder.setOutputSuffix(properties.getProperty("output_suffix"));
		}
	}
	
	// not required 
	public static void importTypeMime(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("type_mime")){
			builder.setTypeMime(properties.getProperty("type_mime"));
		}
	}
	
	// not required
	public static void importOutputFolderForWindow(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			if(!(prop.endsWith("/") || prop.endsWith("\\\\"))){
				prop += "/";
			}
			
			boolean export_raster = true;
			if(properties.containsKey("export_raster")){
				export_raster = Boolean.parseBoolean(properties.getProperty("export_raster"));
			}
			if(export_raster) {
				RasterTypeMime typeMime = builder.getTypeMime();
				if(typeMime == RasterTypeMime.ASCII_GRID){
					builder.setAsciiGridOutputFolder(prop);
				}else if(typeMime == RasterTypeMime.GEOTIFF){
					builder.setGeoTiffOutputFolder(prop);
				}else{
					throw new NoParameterException("type_mime : '"+builder.getTypeMime()+"' interpretation problem");
				}
			}
			
			boolean export_csv = true;
			if(properties.containsKey("export_csv")){
				export_csv = Boolean.parseBoolean(properties.getProperty("export_csv"));
			}
			if(export_csv) {
				builder.setCsvOutputFolder(prop);
			}
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
	
	// not required
	public static void importFillValue(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("fill_value")){
			builder.setFillValue(Float.parseFloat(properties.getProperty("fill_value")));
		}
	}	
	
	// not required
	public static void importCellSize(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("cellsize")){
			builder.setCellSize(Float.parseFloat(properties.getProperty("cellsize")));
		}
	}
	
	// not required
	public static void importHeight(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("height")){
			builder.setHeight(Integer.parseInt(properties.getProperty("height")));
		}
	}
	
	// not required
	public static void importWidth(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("width")){
			builder.setWidth(Integer.parseInt(properties.getProperty("width")));
		}
	}
	
	// not required
	public static void importXMin(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("xmin")){
			builder.setXMin(Double.parseDouble(properties.getProperty("xmin")));
		}
	}
	
	// not required
	public static void importXMax(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("xmax")){
			builder.setXMax(Double.parseDouble(properties.getProperty("xmax")));
		}
	}
	
	// not required
	public static void importYMin(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("ymin")){
			builder.setYMin(Double.parseDouble(properties.getProperty("ymin")));
		}
	}
	
	// not required
	public static void importYMax(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("ymax")){
			builder.setYMax(Double.parseDouble(properties.getProperty("ymax")));
		}
	}
	
	// required
	private static void importDomains(ChloeAnalysisBuilder builder, Properties properties) throws NoParameterException {
		if(properties.containsKey("domains")){
			String prop = properties.getProperty("domains").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split("-");
				domains.put(DomainFactory.getFloatDomain(dd[0]), Integer.parseInt(dd[1]));
			}
			builder.setDomains(domains);
			
			return;
		}
		throw new NoParameterException("domains");
	}
	
}
