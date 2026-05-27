package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptPrepare {

	public static void main(String args[]) {
		
		//moyenne();
		//cleanOptimize();
		//moyenneGlobale();
	}
	
	private static void moyenneGlobale(){
		
		String path = "F:/PREPARE/dijon/model2/outputs/moyenne_dijon/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.addRasterFile(path+"moyenne_model_dijon_scenario1.tif");
		//builder.addRasterFile(path+"moyenne_model_dijon_scenario2.tif");
		builder.addRasterFile(path+"model_dijon_scenario_optimize.tif");
		builder.addMetric("Central");
		builder.setUnfilters(new int[] {-1});
		builder.addWindowSize(3);
		builder.addCsvOutput(path+"moyenne_model_dijon_scenarioOptimize.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}

	private static void cleanOptimize() {
		String path = "F:/PREPARE/dijon/model2/outputs/moyenne_dijon/";
		
		Coverage cov1 = CoverageManager.getCoverage(path+"moyenne_model_dijon_scenario1.tif");
		EnteteRaster entete = cov1.getEntete();
		cov1.dispose();
		
		Coverage covO = CoverageManager.getCoverage("F:/PREPARE/dijon/model2/outputs_old/scenario_optimize/1_model_Dijon_2020_optimize.asc");
		float[] dataO = covO.getData();
		covO.dispose();
		
		CoverageManager.write(path+"model_dijon_scenario_optimize.tif", dataO, entete);	
	}

	private static void moyenne() {
		
		String path = "F:/PREPARE/dijon/model2/outputs/scenario_2/";
		
		Coverage cov1 = CoverageManager.getCoverage(path+"1_model_Dijon_2020.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(path+"2_model_Dijon_2020.tif");
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		Coverage cov3 = CoverageManager.getCoverage(path+"3_model_Dijon_2020.tif");
		float[] data3 = cov3.getData();
		cov3.dispose();
		
		Coverage cov4 = CoverageManager.getCoverage(path+"4_model_Dijon_2020.tif");
		float[] data4 = cov4.getData();
		cov4.dispose();
		
		Coverage cov5 = CoverageManager.getCoverage(path+"5_model_Dijon_2020.tif");
		float[] data5 = cov5.getData();
		cov5.dispose();
		
		Coverage cov6 = CoverageManager.getCoverage(path+"6_model_Dijon_2020.tif");
		float[] data6 = cov6.getData();
		cov6.dispose();
		
		Coverage cov7 = CoverageManager.getCoverage(path+"7_model_Dijon_2020.tif");
		float[] data7 = cov7.getData();
		cov7.dispose();
		
		Coverage cov8 = CoverageManager.getCoverage(path+"8_model_Dijon_2020.tif");
		float[] data8 = cov8.getData();
		cov8.dispose();
		
		Coverage cov9 = CoverageManager.getCoverage(path+"9_model_Dijon_2020.tif");
		float[] data9 = cov9.getData();
		cov9.dispose();
		
		Coverage cov10 = CoverageManager.getCoverage(path+"10_model_Dijon_2020.tif");
		float[] data10 = cov10.getData();
		cov10.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10) {

			@Override
			protected float doTreat(float[] v) {
				
				if(v[0] != entete.noDataValue()) {
					
					return (v[0] + v[1] + v[2] + v[3] + v[4] + v[5] + v[6] + v[7] + v[8] + v[9])/10.0f;
				}
				
				return -1;
			}
		};
		pptc.run();
		
		CoverageManager.write(path+"moyenne_model_dijon_scenario2.tif", data, entete);
	}
	
}
