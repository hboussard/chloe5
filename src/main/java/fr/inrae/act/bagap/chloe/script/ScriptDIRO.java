package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptDIRO {

	private static String path = "C:/Data/projet/DIRO/data/";
	
	public static void main(String[] args) {
		
		//scriptGenerationPaysages();
		//scriptGenerationPermeabilites();
		//scriptContinuitesPrairiales();
		
		//scriptContinuitesStructurelles(500);
		scriptContinuitesStructurelles(3000);
		//scriptContinuitesStructurelles(5000);
	}
	
	private static void scriptContinuitesStructurelles(int size) {
		
		int ws = ((int) (size * 2 /* 1.5*/ / 5)) + 1;
		System.out.println(ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		
		builder.addRasterFile("F:/data/sig/grand_ouest/GO_2021_ebr.tif");
		builder.setValues("1,2,3,4,5,6,7,9,10,12,13,14,15,16,17,18,19,20,22,23");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(ws);
		builder.setDisplacement(20);
		builder.addMetric("pNVm_13&14&19");
		builder.addGeoTiffOutput("pNVm_13&14&19", path+"structure/proportion_habitats_prairiaux_"+size+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void scriptContinuitesPrairiales() {
		
		File folder = new File(path+"points/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
				builder.setRasterFile(path+"points/"+file);
				builder.setRasterFile2(path+"permeabilites/permeabilite_"+file);
				builder.setWindowSize(101);
				//builder.setDisplacement(4);
				builder.addMetric("surface");
				builder.addMetric("volume");
				builder.setDMax(250.0);
				builder.addGeoTiffOutput("surface", path+"permeabilites/outputs/surface_accessibilite_prairiale_"+file);
				builder.addGeoTiffOutput("volume", path+"permeabilites/outputs/volume_deplacement_prairiale_"+file);
				
				LandscapeMetricAnalysis analysis = builder.build();
			
				analysis.allRun();
			}
		}
		
		
	}
	
	private static void scriptGenerationPermeabilites() {
		
		File folder = new File(path+"points/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				Coverage covOS = CoverageManager.getCoverage(path+"points/"+file);
				EnteteRaster entete = covOS.getEntete();
				float[] dataOS = covOS.getData();
				covOS.dispose();
				
				Map<Float, Float> sarMap = new TreeMap<Float, Float>();
				sarMap.put(0f, 100f); 
				sarMap.put(1f, 100f); 
				sarMap.put(2f, 5f);
				sarMap.put(3f, 100f); 
				sarMap.put(4f, 10f); 
				sarMap.put(5f, 2.5f);
				sarMap.put(6f, 2.5f);
				sarMap.put(7f, 2.5f);
				sarMap.put(9f, 2.5f);
				sarMap.put(10f, 2.5f);
				sarMap.put(12f, 2.5f);
				sarMap.put(13f, 1f);
				sarMap.put(14f, 1f);
				sarMap.put(15f, 5f);
				sarMap.put(16f, 1.8f);
				sarMap.put(17f, 1.8f);
				sarMap.put(18f, 1.3f);
				sarMap.put(19f, 1f);
				sarMap.put(20f, 1.8f);
				sarMap.put(22f, 100f);
				sarMap.put(23f, 100f);
				Pixel2PixelTabCalculation cal;
				
				float[] dataPermeabilite = new float[entete.width()*entete.height()];
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataPermeabilite, dataOS, sarMap);
				cal.run();
				
				CoverageManager.write(path+"permeabilites/permeabilite_"+file, dataPermeabilite, entete);
			}
		}
	}

	private static void scriptGenerationPaysages(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		//builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.addRasterFile("F:/data/sig/grand_ouest/GO_2021_ebr.tif");
		builder.setValues("1,2,3,4,5,6,7,9,10,12,13,14,15,16,17,18,19,20,22,23");
		builder.addMetric("N-valid");
		builder.setWindowSize(1001); // 2.5km autour des points
		builder.setPointsFilter(path+"points_potentiels.txt");
		builder.addCsvOutput(path+"analyse.csv");
		builder.setWindowsPath(path+"points/");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
