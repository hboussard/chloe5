package fr.inrae.act.bagap.chloe;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;

public class Script {

	public static void main(String[] args){
		
		
	}
	
	private static void scriptHuge(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"za.asc");
		builder.addMetric("SHDI");
		builder.setWindowSize(21);
		//builder.setBufferROI(100);
		//builder.setDisplacement(2);
		//builder.setInternalROI(1);
		//builder.setInterpolation(true);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"analyse/testi.csv");
		builder.addAsciiGridOutput("SHDI", path+"analyse/shdi_ex100.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		//analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptACE(){
		//scriptACE("2000", "composition", "300m");
		//scriptACE("2009", "composition", "300m");
		//scriptACE("2015", "composition", "300m");
		//scriptACE("2000", "configuration", "300m");
		//scriptACE("2009", "configuration", "300m");
		//scriptACE("2015", "configuration", "300m");
		//scriptACE("2000", "composition", "500m");
		//scriptACE("2009", "composition", "500m");
		//scriptACE("2015", "composition", "500m");
		//scriptACE("2000", "configuration", "500m");
		//scriptACE("2009", "configuration", "500m");
		//scriptACE("2015", "configuration", "500m");
		//scriptACE("2000", "composition", "2km");
		//scriptACE("2009", "composition", "2km");
		//scriptACE("2015", "composition", "2km");
		//scriptACE("2000", "configuration", "2km");
		//scriptACE("2009", "configuration", "2km");
		//scriptACE("2015", "configuration", "2km");
		//scriptACE("2000", "composition", "3km");
		//scriptACE("2009", "composition", "3km");
		//scriptACE("2015", "composition", "3km");
		//scriptACE("2000", "configuration", "3km");
		//scriptACE("2009", "configuration", "3km");
		//scriptACE("2015", "configuration", "3km");
	}
	
	private static void scriptACE(String year, String compo_config, String scale){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/aquitaine/ocs/OCS_Dordogne_00_09_15_raster/carto/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setDisplacement(20);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setRaster(path+"OCS_Dordogne_"+year+".tif");
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i=1; i<=7; i++){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i=1; i<=7; i++){
				for(int j=i+1; j<=7; j++){
					builder.addMetric("pNC_"+i+"-"+j);
				}
			}
		}
		
		if(scale.equalsIgnoreCase("300m")){
			builder.setWindowSize(121); // 300m
		}
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(201); // 500m
		}
		if(scale.equalsIgnoreCase("2km")){
			builder.setWindowSize(801); // 2km
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(1201); // 3km
		}
		
		builder.addCsvOutput(path+"ace_"+compo_config+"_"+year+"_"+scale+".csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	
	
	private static void scriptFDCCA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/FDCCA/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRaster(path+"secteur1/carto/secteur1_distance.asc");
		//builder.setRaster(path+"secteur2/carto/secteur2_distance.asc");
		//builder.setRaster(path+"secteur3/carto/secteur3_distance.asc");
		//builder.setRaster(path+"secteur4/carto/secteur4_distance.asc");
		builder.setRaster(path+"secteur4bis/carto/secteur4bis_distance.asc");
		//builder.setRaster(path+"secteur5/carto/secteur5_distance.asc");
		builder.addMetric("MD");
		builder.setWindowSize(141);
		//builder.addAsciiGridOutput("MD", path+"secteur1/carto/secteur1_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur2/carto/secteur2_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur3/carto/secteur3_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur4/carto/secteur4_grain.asc");
		builder.addAsciiGridOutput("MD", path+"secteur4/carto/secteur4bis_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur5/carto/secteur5_grain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptBasleonEcopaysageFonctionnel(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/bas_leon/data/continuite/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRaster(path+"boise/norm_continuite_bois.asc");
		//builder.setRaster(path+"prairiale/norm_continuites_prairiales_talus_edge_wood.asc");
		builder.setRaster(path+"zh/norm_continuites_zones_humides.asc");
		builder.addMetric("sum");
		//builder.setWindowSize(401); // 500m gaussian
		builder.setWindowSize(2401); // 3km gaussian
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_boises_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_boises_3km.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_prairiales_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_prairiales_3km.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_zones_humides_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_zones_humides_3km.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptClemence(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/agent/clemence_brosse/Limousin/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"Limousin.tif");
		//builder.addMetric("NV_1");
		//builder.setWindowRadius(500);
		builder.setWindowSize(3);
		//builder.addCsvOutput(path+"Limousin_prop_eau.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptMathilde(){
		String path = "F:/woodnet/mathilde/Continuity/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(WindowAnalysisType.SELECTED);
		builder.setRaster(path+"PF.asc");
		builder.setWindowSize(151);
		builder.setPointFilter(path+"points.csv");
		builder.addAscExportWindowOutput(path+"filters");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
	
}
