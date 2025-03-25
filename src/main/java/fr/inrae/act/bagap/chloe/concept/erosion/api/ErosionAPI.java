package fr.inrae.act.bagap.chloe.concept.erosion.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import fr.inrae.act.bagap.chloe.api.NoParameterException;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;

public class ErosionAPI {

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
				ErosionManager manager = new ErosionManager(treatment); 
				
				importParameters(manager, properties);
				
				ErosionProcedure procedure = manager.build();
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
	
	private static void importParameters(ErosionManager manager, Properties properties) {
		
		try{
		
			importForce(manager, properties);
			importEventType(manager, properties);
			importDisplacement(manager, properties);
			importOutputFolder(manager, properties);
			importOutputPrefix(manager, properties);
			importTerritoryShape(manager, properties);
			importTerritoryIDAttribute(manager, properties);
			importTerritoryIDValues(manager, properties);
			importElevationFolder(manager, properties);
			importOsSource(manager, properties);
			importSurfaceWoodShape(manager, properties);
			importSurfaceWoodAttribute(manager, properties);
			importSurfaceWoodCode(manager, properties);
			importLinearWoodShape(manager, properties);
			importLinearWoodCode(manager, properties);
			importLinearRoadShape(manager, properties);
			importLinearRoadAttribute(manager, properties);
			importLinearRoadCode(manager, properties);
			importLinearTrainShape(manager, properties);
			importLinearTrainCode(manager, properties);
			importSurfaceWaterShape(manager, properties);
			importSurfaceWaterCode(manager, properties);
			importLinearWaterShape(manager, properties);
			importLinearWaterCode(manager, properties);
			importInfiltrationMapFile(manager, properties);
			importErodibilityMapFile(manager, properties);
			
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void importForce(ErosionManager manager, Properties properties) {
		if(properties.containsKey("force")){
			boolean force = Boolean.parseBoolean(properties.getProperty("force"));
			manager.setForce(force);
		}
	}
	
	public static void importEventType(ErosionManager manager, Properties properties) {
		if(properties.containsKey("event_type")){
			manager.setEventType(properties.getProperty("event_type"));
		}		
	}
	
	// not required
	// default parameter : "1"
	public static void importDisplacement(ErosionManager manager, Properties properties) {
		if(properties.containsKey("displacement")){
			manager.setDisplacement(Integer.parseInt(properties.getProperty("displacement")));
		}
	}
	
	public static void importOutputFolder(ErosionManager manager, Properties properties) {
		if(properties.containsKey("output_folder")){
			String prop = properties.getProperty("output_folder");
			if(!(prop.endsWith("/") || prop.endsWith("\\\\"))){
				prop += "/";
			}
			manager.setOutputFolder(prop);
		}
	}
	
	public static void importOutputPrefix(ErosionManager manager, Properties properties) {
		if(properties.containsKey("output_prefix")){
			manager.setOutputPrefix(properties.getProperty("output_prefix"));
		}
	}
	
	public static void importTerritoryShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("territory_shape")){
			manager.setTerritoryShape(properties.getProperty("territory_shape"));
		}
	}
	
	public static void importTerritoryIDAttribute(ErosionManager manager, Properties properties) {
		if(properties.containsKey("territory_id_attribute")){
			manager.setTerritoryIDAttribute(properties.getProperty("territory_id_attribute"));
		}
	}
	
	public static void importTerritoryIDValues(ErosionManager manager, Properties properties) {
		if(properties.containsKey("territory_id_values")){
			String prop = properties.getProperty("territory_id_values");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			manager.setTerritoryIDValues(prop.split(";"));
		}
	}
	
	public static void importElevationFolder(ErosionManager manager, Properties properties) {
		if(properties.containsKey("elevation_folder")){
			String prop = properties.getProperty("elevation_folder").replace("{",  "").replace("}", "");
			String[] elevations = prop.split(";");
			for(String elevation : elevations) {
				manager.addElevationFolder(elevation);
			}
		}
	}
	
	public static void importOsSource(ErosionManager manager, Properties properties) {
		if(properties.containsKey("os_source")){
			manager.setOsSource(properties.getProperty("os_source"));
		}
	}
	
	public static void importSurfaceWoodShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("surface_wood_shape")){
			String prop = properties.getProperty("surface_wood_shape").replace("{",  "").replace("}", "");
			String[] surfaces = prop.split(";");
			for(String wood : surfaces) {
				manager.addSurfaceWoodShape(wood);
			}
		}
	}
	
	public static void importSurfaceWoodAttribute(ErosionManager manager, Properties properties) {
		if(properties.containsKey("surface_wood_attribute")){
			manager.setSurfaceWoodAttribute(properties.getProperty("surface_wood_attribute"));
		}
	}
	
	public static void importSurfaceWoodCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("surface_wood_code")){
			String prop = properties.getProperty("surface_wood_code").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				manager.addSurfaceWoodCode(vv[0], Integer.parseInt(vv[1]));
			}
		}
	}
	
	public static void importLinearWoodShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_wood_shape")){
			String prop = properties.getProperty("linear_wood_shape").replace("{",  "").replace("}", "");
			String[] linears = prop.split(";");
			for(String wood : linears) {
				manager.addLinearWoodShape(wood);
			}
		}
	}
	
	public static void importLinearWoodCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_wood_code")){
			String prop = properties.getProperty("linear_wood_code");
			manager.setLinearWoodCode(Integer.parseInt(prop));
		}
	}
	
	public static void importLinearRoadShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_road_shape")){
			String prop = properties.getProperty("linear_road_shape").replace("{",  "").replace("}", "");
			String[] linears = prop.split(";");
			for(String road : linears) {
				manager.addLinearRoadShape(road);
			}
		}
	}
	
	public static void importLinearRoadAttribute(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_road_attribute")){
			manager.setLinearRoadAttribute(properties.getProperty("linear_road_attribute"));
		}
	}
	
	public static void importLinearRoadCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_road_code")){
			String prop = properties.getProperty("linear_road_code").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				manager.addLinearRoadCode(vv[0], Integer.parseInt(vv[1]));
			}
		}
	}
	
	public static void importLinearTrainShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_train_shape")){
			String prop = properties.getProperty("linear_train_shape").replace("{",  "").replace("}", "");
			String[] linears = prop.split(";");
			for(String train : linears) {
				manager.addLinearTrainShape(train);
			}
		}
	}
	
	public static void importLinearTrainCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_train_code")){
			String prop = properties.getProperty("linear_train_code");
			manager.setLinearTrainCode(Integer.parseInt(prop));
		}
	}
	
	public static void importSurfaceWaterShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("surface_water_shape")){
			String prop = properties.getProperty("surface_water_shape").replace("{",  "").replace("}", "");
			String[] surfaces = prop.split(";");
			for(String water : surfaces) {
				manager.addSurfaceWaterShape(water);
			}
		}
	}
	
	public static void importSurfaceWaterCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("surface_water_code")){
			String prop = properties.getProperty("surface_water_code");
			manager.setSurfaceWaterCode(Integer.parseInt(prop));
		}
	}
	
	public static void importLinearWaterShape(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_water_shape")){
			String prop = properties.getProperty("linear_water_shape").replace("{",  "").replace("}", "");
			String[] linears = prop.split(";");
			for(String water : linears) {
				manager.addLinearWaterShape(water);
			}
		}
	}
	
	public static void importLinearWaterCode(ErosionManager manager, Properties properties) {
		if(properties.containsKey("linear_water_code")){
			String prop = properties.getProperty("linear_water_code");
			manager.setLinearWaterCode(Integer.parseInt(prop));
		}
	}
	
	public static void importInfiltrationMapFile(ErosionManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("infiltration_map_file")){
			manager.setInfiltrationMapFile(properties.getProperty("infiltration_map_file"));
			return;
		}
		throw new NoParameterException("infiltration_map_file");
	}
	
	public static void importErodibilityMapFile(ErosionManager manager, Properties properties) throws NoParameterException {
		if(properties.containsKey("erodibility_map_file")){
			manager.setErodibilityMapFile(properties.getProperty("erodibility_map_file"));
			return;
		}
		throw new NoParameterException("erodibility_map_file");
	}
	
}
