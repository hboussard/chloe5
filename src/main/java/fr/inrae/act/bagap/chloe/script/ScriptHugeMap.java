package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptHugeMap {

	public static void main(String[] args) {
		
		analyseMap2();
	}
	
	private static void analyseMap2(){
		
		String path = "D:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12");
		builder.addMetric("SHDI");
		//builder.addMetric("HET");
		builder.addMetric("NC-valid");
		builder.addMetric("NC_1-1");
		builder.addMetric("NC_2-2");
		builder.addMetric("NC_3-3");
		builder.addMetric("NC_4-4");
		builder.addMetric("NC_5-5");
		builder.addMetric("NC_6-6");
		builder.addMetric("NC_7-7");
		builder.addMetric("NC_8-8");
		builder.addMetric("NC_9-9");
		builder.addMetric("NC_10-10");
		builder.addMetric("NC_11-11");
		builder.addMetric("NC_12-12");
		//builder.addMetric("NP");	
		/*
		builder.addMetric("NV_1");
		builder.addMetric("NV_2");
		builder.addMetric("NV_3");
		builder.addMetric("NV_4");
		builder.addMetric("NV_5");
		builder.addMetric("NV_6");
		builder.addMetric("NV_7");
		builder.addMetric("NV_8");
		builder.addMetric("NV_9");
		builder.addMetric("NV_10");
		builder.addMetric("NV_11");
		builder.addMetric("NV_12");
		*/
		builder.addCsvOutput(path+"test/analyse2.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyseMap(){
		
		String path = "D:/data/sig/bretagne/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile(path+"Bretagne_2019_dispositif_bocage_ebr.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12");
		builder.addMetric("NP");	
		/*
		builder.addMetric("NV_1");
		builder.addMetric("NV_2");
		builder.addMetric("NV_3");
		builder.addMetric("NV_4");
		builder.addMetric("NV_5");
		builder.addMetric("NV_6");
		builder.addMetric("NV_7");
		builder.addMetric("NV_8");
		builder.addMetric("NV_9");
		builder.addMetric("NV_10");
		builder.addMetric("NV_11");
		builder.addMetric("NV_12");
		*/
		builder.addCsvOutput(path+"test/analyse.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
}
