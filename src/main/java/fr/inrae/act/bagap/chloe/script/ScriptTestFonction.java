package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptTestFonction {

	public static void main(String[] args) {
		
		map();
	}
	
	private static void map() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile("C:/Data/projet/DIRO/data/points/window_17.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addCsvOutput("C:/Data/temp/lena/test/map3.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

}
