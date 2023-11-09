package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMultipleMap {

	public static void main(String[] args) {
		scriptMultipleMap();
	}
	
	private static void scriptMultipleMap(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.setRasterFile(path+"mini/landscape1.tif");
		//builder.addRasterFile(path+"mini/landscape1.tif");
		//builder.addRasterFile(path+"mini/landscape2.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addMetric("NP");
		builder.addMetric("LPI");
		builder.addMetric("MPS");
		
		builder.addCsvOutput(path+"mini/map2/analysis_map.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
}
