package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptSelected {

	public static void main(String[] args) {
		
		//generationFriction();
		//analyse2Friction();
		analyse2Friction();
	}
	
	private static void generationFriction(){
		Coverage osCov = CoverageManager.getCoverage("G:/chloe/winterschool/data/start/za.tif");
		EnteteRaster entete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[osData.length];
		Map<Float, Float> sarMap = new HashMap<Float, Float>();
		sarMap.put(1f, 2f);
		sarMap.put(2f, 4f);
		sarMap.put(3f, 1f);
		sarMap.put(4f, 1f);
		sarMap.put(5f, 1f);
		sarMap.put(6f, 1f);
		sarMap.put(7f, 100f);
		sarMap.put(8f, 10f);
		sarMap.put(9f, 10f);
		sarMap.put(10f, 5f);
		sarMap.put(11f, 1f);
		sarMap.put(12f, 1f);
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, sarMap);
		cal.run();
		
		CoverageManager.write("G:/chloe/winterschool/data/start/selected2_friction/za_friction.tif", data, entete);
	}
	
	private static void analyse2Friction(){
		
		String path = "G:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile(path+"za.tif");
		builder.setRasterFile2(path+"selected2_friction/za_friction.tif");
		builder.setPointsFilter(path+"points_id.csv");
		builder.setWindowSize(121);
		builder.addMetric("SHDI");
		builder.addCsvOutput(path+"selected2_friction/analyse.csv");
		builder.setWindowsPath(path+"selected2_friction/filters/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyse2(){
		
		String path = "G:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"za.tif");
		builder.setPointsFilter(path+"points_id.csv");
		builder.setWindowSize(21);
		builder.addMetric("SHDI");
		builder.addCsvOutput(path+"selected2/analyse.csv");
		builder.setWindowsPath(path+"selected2/filters/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyse3(){
		
		String path = "G:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"za.tif");
		builder.setPixelsFilter(path+"pixels.csv");
		builder.setWindowSize(21);
		builder.addMetric("SHDI");
		builder.addCsvOutput(path+"selected3/analyse.csv");
		builder.setWindowsPath(path+"selected3/filters/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
