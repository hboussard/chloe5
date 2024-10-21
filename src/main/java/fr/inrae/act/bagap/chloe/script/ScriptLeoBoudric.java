package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptLeoBoudric {

	private static String path = "H:/temp/leo_boudric/";
	
	public static void main(String[] args) {
		
		System.out.println("script pour Leo");
		/*
		String input = path+"data/INDICATEURS/BENTHOS GLOBAL_SG.tif";
		String output = path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif";
		scriptCleanNoData(input, output);
		*/
		/*
		String input = path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif";
		String output = path+"data/INDICATEURS/BENTHOS GLOBAL_SG_classif.tif";
		scriptClassificationIndicateur(input, output);
		*/
		scriptSliding();
	}
	
	private static void scriptSliding(){
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowShapeType(WindowShapeType.CIRCLE);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_classif.tif");
		
		//builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		//builder.setRasterFile2(carto_permeabilite);
		
		builder.addMetric("pNV_1");
		builder.addMetric("pNV_2");
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.addMetric("NV_1");
		builder.addMetric("NV_2");
		builder.addMetric("NV_3");
		builder.addMetric("NV_4");
		builder.addMetric("NV_5");
		builder.addWindowSize(101);
		builder.setUnfilters(new int[]{0});
		//builder.setDisplacement(20);
		/*
		builder.addGeoTiffOutput("pNV_1", path+"sliding/p_mauvais_50m.tif");
		builder.addGeoTiffOutput("pNV_2", path+"sliding/p_mediocre_50m.tif");
		builder.addGeoTiffOutput("pNV_3", path+"sliding/p_moyen_50m.tif");
		builder.addGeoTiffOutput("pNV_4", path+"sliding/p_bon_50m.tif");
		builder.addGeoTiffOutput("pNV_5", path+"sliding/p_tres_bon_50m.tif");
		builder.addGeoTiffOutput("NV_1", path+"sliding/mauvais_50m.tif");
		builder.addGeoTiffOutput("NV_2", path+"sliding/mediocre_50m.tif");
		builder.addGeoTiffOutput("NV_3", path+"sliding/moyen_50m.tif");
		builder.addGeoTiffOutput("NV_4", path+"sliding/bon_50m.tif");
		builder.addGeoTiffOutput("NV_5", path+"sliding/tres_bon_50m.tif");
		*/
		builder.addCsvOutput(path+"sliding/analyse_BENTHOS GLOBAL_SG.csv");
		
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptClassificationIndicateur(String input, String output){
		
		Coverage cov1 = CoverageManager.getCoverage(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		float[] data = new float[data1.length];
		
		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		domains.put(DomainFactory.getFloatDomain("[0,0]"), 0);
		domains.put(DomainFactory.getFloatDomain("]0,3]"), 1);
		domains.put(DomainFactory.getFloatDomain("]3,4.5]"), 2);
		domains.put(DomainFactory.getFloatDomain("]4.5,5.5]"), 3);
		domains.put(DomainFactory.getFloatDomain("]5.5,7]"), 4);
		domains.put(DomainFactory.getFloatDomain("]7,]"), 5);
		
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(data, data1, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_classif.tif", data, entete);	
	}
	
	private static void scriptCleanNoData(String inputFile, String outputFile){
		
		Coverage cov1 = CoverageManager.getCoverage(inputFile);
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] data = new float[data1.length];
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(-3.4E38f, 0f);
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, data1, sarMap);
		cal.run();
		
		CoverageManager.write(outputFile, data, entete);	
	}
	
}
