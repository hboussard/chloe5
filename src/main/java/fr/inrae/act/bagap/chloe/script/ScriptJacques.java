package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.util.Map;

import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptJacques {

	public static void main(String[] args) {
		
		//scriptCompile();
		/*
		String path = "D:/temp/chloe/problème CHLOE4/problème CHLOE4/";
		scriptSearchAndReplace(path+"Queen/cluster_MNHC_class1_all_queen.asc", path+"Queen/cluster_MNHC_class1_all_queen.csv", path+"area/H1CONT_surface.asc");
		scriptSearchAndReplace(path+"Queen/cluster_MNHC_class2_supx25_queen.asc", path+"Queen/cluster_MNHC_class2_supx25_queen.csv", path+"area/H2CONT_surface.asc");
		*/
		//conversion();
		//scriptSelected();
	}
	
	private static void scriptSelected(){
		
		String path = "D:/temp/chloe/PB plugin CHLOE/PB plugin CHLOE/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"SPOT2.tif");
		builder.setPointsFilter(path+"SPOTb.csv");
		builder.setWindowSize(11);
		builder.addMetric("sum");
		builder.addCsvOutput(path+"test.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void conversion() {
		File folder = new File("D:/temp/chloe/cartes à analyser/cartes à analyser/");
		
		for(File f : folder.listFiles()) {
			for(String file : f.list()) {
				if(file.endsWith(".asc")) {
					String name = file.replace(".asc", "");
					scriptSearchAndReplace(f.toString()+"/"+name+".asc", f.toString()+"/"+name+".csv", f.toString()+"/"+name+"_surface.asc");
				}
			}
		}
		
	}
	
	private static void scriptSearchAndReplace(String inputRaster, String mapFile, String outputRaster) {
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		float[] inData = cov.getData();
		cov.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Map<Float, Float> sarMap = Util.importData(mapFile, "id", "surface_patch");
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(outData, inData, sarMap);
		cal.run();
		
		CoverageManager.write(outputRaster, outData, entete); // export sur fichier
	}

	private static void scriptCompile(){
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/22_2018_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/22_2018_5m/mnhc_22_2018_5m.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/29_2018_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/29_2018_5m/mnhc_29_2018_5m.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/56_2019_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/56_2019_5m/mnhc_56_2019_5m.tif", data, entete);
		*/
		/*
		
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/35_2020_5m/mean/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/35_2020_5m/mnhc_35_2020_5m.tif", data, entete);
		*/
	}
	
	
}
