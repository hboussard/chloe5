package fr.inrae.act.bagap.chloe.script;

import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptMultipleSliding {

	
	public static void main(String[] args) {
		//convert();
		scriptMultipleSliding();
		
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
