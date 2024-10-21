package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.Map;

import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptArticleGrainBocager {

	public static void main(String[] args){
		
		//analyseGrainBocager();
		//analyseDensiteBoisement();
		
		//sarClusterGrainBocager();
		sarClusterDensiteBoisement();
	}
	
	private static void sarClusterGrainBocager() {
		
		FileMap fm = new FileMap("E:/IGN/data_bretagne/cluster_grain/cluster_grain.csv", "id", "class");
		
		Coverage cov = CoverageManager.getCoverage("E:/IGN/data_bretagne/cluster_grain/cluster_grain.tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(outData, data, fm.getMap());
		cal.run();
		
		CoverageManager.write("E:/IGN/data_bretagne/cluster_grain/cluster_grain_class.tif", outData, entete);
		
	}
	
	private static void sarClusterDensiteBoisement() {
		
		FileMap fm = new FileMap("E:/IGN/data_bretagne/cluster_grain/cluster_densite.csv", "id", "class");
		
		Coverage cov = CoverageManager.getCoverage("E:/IGN/data_bretagne/cluster_grain/cluster_densite.tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(outData, data, fm.getMap());
		cal.run();
		
		CoverageManager.write("E:/IGN/data_bretagne/cluster_grain/cluster_densite_class.tif", outData, entete);
		
	}

	private static void analyseDensiteBoisement() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setPointsFilter("E:/grain_bocager/article/data/XYbzh.csv");
		builder.setRasterFile("E:/IGN/data_bretagne/boisement/");
		builder.addMetric("pNV_1");
		for(int ws=21; ws<=401; ws+=20) {
			builder.addWindowSize(ws);
		}
		builder.addCsvOutput("E:/IGN/data_bretagne/analyse_biodiversite_densite_boisement.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyseGrainBocager() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setPointsFilter("E:/grain_bocager/article/data/XYbzh.csv");
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setRasterFile("E:/IGN/data_bretagne/distance_influence/");
		builder.addMetric("average");
		int size;
		for(int ws=21; ws<=401; ws+=20) {
			size = (int) (ws*1.5);
			if(size%2 == 0) {
				size++;
			}
			//System.out.println(size);
			builder.addWindowSize(size);
		}
		builder.addCsvOutput("E:/IGN/data_bretagne/analyse_biodiversite_grain_bocager.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	public static Coverage calculGrainBocager(float[] dataDistanceInfluence, EnteteRaster entete, double windowRadius, double outputCellSize, boolean fastMode) {
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		CoverageOutput covOutput = new CoverageOutput("average");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}
		builder.setRasterTab(dataDistanceInfluence);
		builder.setEntete(entete);
		builder.setDisplacement(displacement); 
		builder.addMetric("average");
		if(fastMode){
			builder.setWindowSize((int) (windowSize*1.5));
		}else{
			builder.setWindowSize(windowSize);
		}
		
		builder.addCoverageOutput(covOutput);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return covOutput.getCoverage();
	}
	
}
