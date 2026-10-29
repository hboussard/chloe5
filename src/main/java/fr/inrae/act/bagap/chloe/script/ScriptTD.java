package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptTD {

	public static void main(String[] args) {
		
		//generateOcsol();
	}

	private static void generateOcsol() {
		
		String outputPath = "C:/Data/temp/LMBCF/data/";
		String baseOccsol = "E:/projet/caphaie/occsol/raster/france/";
		
		int[] values = new int[38];
		for(int i=1; i<=values.length; i++) {
			values[i-1] = i;
		}
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.addRasterFile(baseOccsol);
		builder.setValues(values);
		builder.setPointsFilter(outputPath+"point_chateau.csv");
		builder.setWindowSizes(new int[]{2001}); // rayon 5km --> 10km de cote
		builder.addMetric("N-valid");
		builder.setWindowsPath(outputPath);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
