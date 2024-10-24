package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptMultipleSliding {

	
	public static void main(String[] args) {
		//convert();
		//scriptMultipleSliding();
		analyseSliding();
		
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
		builder.setCsvOutputFolder(path+"test");
		//builder.addCsvOutput(path+"test/analyse.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void convert(){
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/mini/";
		
		Coverage cov = CoverageManager.getCoverage(path+"landscape2.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write(path+"landscape2.asc", data, entete);
		
	}
	
	
	private static void scriptMultipleSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		
		builder.addRasterFile(path+"mini/landscape1.tif");
		builder.addRasterFile(path+"mini/landscape2.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addMetric("NP");
		builder.addMetric("LPI");
		builder.addMetric("MPS");
		builder.addWindowSize(31);
		builder.addWindowSize(51);
		//builder.addCsvOutput(path+"mini/sliding2/analysis_sliding.csv");
		//builder.addGeoTiffOutput(31, "SHDI", path+"mini/sliding/SHDI_31.tif");
		//builder.setAsciiGridFolderOutput(path+"mini/sliding3/");
		//builder.setGeoTiffFolderOutput(path+"mini/sliding3/");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
