package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptLea {

	public static void main(String[] args) {
		analyse();
	}
	
	private static void analyse(){
		
		String path = "E:/temp/lea/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setRasterFile(path+"Occ_sol_Reseauas0_2022/Occsol_Haies_Reseauas0_2022_Clip.asc");
		//builder.setRasterFile(path+"Occ_sol_2022/Occsol_2022_Clip.asc");
		builder.setPointsFilter(path+"Occ_sol_Reseauas0_2022/pixel_coordinates.csv");
		builder.setWindowSize(501);
		builder.addMetric("N-valid");
		builder.addMetric("SHDI");
		builder.addMetric("NC-valid");
		builder.addMetric("NC-hete");
		builder.addMetric("pNC-hete");
		
		builder.addCsvOutput(path+"analyse_square_avec_haies.csv");
		//builder.addCsvOutput(path+"analyse_square_sans_haies.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
