package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptProjetGahard {

	
	public static void main(String[] args) {
		
		scriptConvertTypeBoisement();
		//scriptSplit();
		//scriptMultipleSliding();
		
	}
	
	private static void scriptConvertTypeBoisement(){
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/35_2020_5m/bocage_distance_massif/");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/IGN/data/35_2020_5m/type_boisement_35_2020.tif", data, entete);
	}
	

	private static void scriptSplit(){
		
		String path = "G:/data/sig/CGTV/";
		
		Coverage cov = CoverageManager.getCoverage(path+"cgtv.tif");
		EnteteRaster enteteRef = cov.getEntete();
		
		Envelope env = new Envelope(353150, 383600, 6783579, 6819440);
		Rectangle roi = EnteteRaster.getROI(enteteRef, env);
		
		float[] data = cov.getData(roi);
		EnteteRaster entete = EnteteRaster.getEntete(enteteRef, env);
		cov.dispose();
		
		CoverageManager.write(path+"cgtv_gahard.tif", data, entete);
		
	}
	
	private static void scriptMultipleSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "G:/data/sig/CGTV/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		
		builder.setDisplacement(5);
		builder.addRasterFile(path+"cgtv_gahard.tif");
		builder.addMetric("pNV_6");
		builder.addMetric("pNV_5");
		builder.addMetric("pNC_6-12");
		builder.addMetric("pNC_5-12");
		builder.addWindowSize(1001); //5km
		//builder.addWindowSize(201); //1km
		//builder.addWindowSize(51); //250m
		//builder.addCsvOutput(path+"mini/sliding2/analysis_sliding.csv");
		//builder.addGeoTiffOutput(1001, "pNV_6", path+"cgtv/pNV_6_1001.tif");
		//builder.addGeoTiffOutput(1001, "pNV_5", path+"cgtv/pNV_5_1001.tif");
		//builder.setAsciiGridOutputFolder(path+"mini/sliding3/");
		builder.setGeoTiffOutputFolder(path+"cgtv/");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
}
