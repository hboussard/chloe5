package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptJacquesDepartement {

	public static void main(String[] args){
		/*
		String inputPath = "E:/IGN/data/";
		String outputPath = "E:/temp/jacques/boisement/";
		scriptmasqueBoisementDepartement(outputPath, inputPath, 22, 2018);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 29, 2018);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 35, 2020);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 56, 2019);
		*/
		
		String input = "D:/temp/jacques/PFboisements.asc";
		String output = "D:/temp/jacques/PFboisements2.tif";
		//scriptConversion(input, output);
		scriptSliding(input, output);
	}
	
	private static void scriptmasqueBoisementDepartement(String outputPath, String inputPath, int numDep, int annee) {
		
		Coverage cov = CoverageManager.getCoverage(inputPath+numDep+"_"+annee+"_5m/mean/");
		EnteteRaster entete = cov.getEntete();
		float[] inData = cov.getData();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, inData){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				if(v1 > 0) {
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write(outputPath+"boisement_"+numDep+"_"+annee+".tif", data, entete);
	}
	
	private static void scriptConversion(String input, String output) {
		
		Coverage cov = CoverageManager.getCoverage(input);
		EnteteRaster entete = cov.getEntete();
		float[] inData = cov.getData();
		cov.dispose();
	
		
		CoverageManager.write(output, inData, entete);
	}
	
	private static void scriptSliding(String input, String output){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(input);
		builder.addMetric("pNV_1");
		builder.addWindowSize(201);
		builder.addGeoTiffOutput("pNV_1", output);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
}
