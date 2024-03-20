package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMultipleWindow {

	public static void main(String[] args) {
		
		analyseSliding();
		//analyseSelected();
		//analyseGrid();
		//analyseMap();
	}
	
	private static void analyseSliding(){
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(path+"os_za_2016.tif");
		//builder.addRasterFile(path+"os_za_2017.tif");
		//builder.addRasterFile(path+"os_za_2018.tif");
		builder.setWindowSizes(new int[]{53, 87});
		builder.addMetric("SHDI");
		builder.addMetric("average");		
		//builder.setCsvOutputFolder(path+"test");
		builder.addCsvOutput(path+"test/analyse3.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyseSelected(){
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.addRasterFile(path+"os_za_2016.tif");
		builder.addRasterFile(path+"os_za_2017.tif");
		builder.addRasterFile(path+"os_za_2018.tif");
		builder.setPointsFilter(path+"points.csv");
		//builder.setWindowSizes(new int[]{51});
		builder.setWindowSizes(new int[]{51, 71});
		builder.addMetric("SHDI");
		builder.addMetric("average");		
		//builder.setCsvOutputFolder(path+"test");
		builder.addCsvOutput(path+"test/analyse.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyseGrid(){
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.GRID);
		builder.addRasterFile(path+"os_za_2016.tif");
		builder.addRasterFile(path+"os_za_2017.tif");
		builder.addRasterFile(path+"os_za_2018.tif");
		//builder.setWindowSizes(new int[]{10});
		builder.setWindowSizes(new int[]{10, 50});
		builder.addMetric("SHDI");
		builder.addMetric("average");		
		//builder.setCsvOutputFolder(path+"test");
		builder.addCsvOutput(path+"test/analyse.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void analyseMap(){
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile(path+"os_za_2016.tif");
		builder.addRasterFile(path+"os_za_2017.tif");
		builder.addRasterFile(path+"os_za_2018.tif");
		builder.addMetric("SHDI");
		builder.addMetric("average");		
		//builder.setCsvOutputFolder(path+"test");
		builder.addCsvOutput(path+"test/analyse.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
