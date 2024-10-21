package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptEntete {

	public static void main(String[] args){
		
		scriptSliding();
		
	}
	
	private static void scriptEnteteExport(){
		Coverage cov = CoverageManager.getCoverage("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/pf_2018_10m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		System.out.println(entete);
		EnteteRaster.export(entete, "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/entete_raster.properties");
	}
	
	private static void scriptSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		
		builder.addRasterFile(path+"pf_2018_10m.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addWindowSize(3);
		builder.addWindowSize(5);
		builder.addCsvOutput(path+"/entete/analysis_sliding.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
