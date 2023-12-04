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
		builder.addMetric("NV_7");
		builder.addWindowSize(401);
		builder.setUnfilters(new int[]{-1});
		//builder.setDisplacement(20);
		builder.addGeoTiffOutput("NV_7", path+"sliding/NV_7_401p.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
