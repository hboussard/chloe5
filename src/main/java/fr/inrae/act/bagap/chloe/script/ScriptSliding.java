package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class ScriptSliding {

	public static void main(String[] args) {
		scriptSliding();
		
	}
	
	private static void scriptSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"pf_2018_10m.tif");
		builder.addMetric("HET");
		//builder.addMetric("pNV_7");
		builder.addWindowSize(201);
		builder.setUnfilters(new int[]{-1});
		builder.addGeoTiffOutput("HET", path+"fast/HET_fast_201p3.tif");
		//builder.addGeoTiffOutput("pNV_7", path+"fast/pNV_7_fast_201p.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
