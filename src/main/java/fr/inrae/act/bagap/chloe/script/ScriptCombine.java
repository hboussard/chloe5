package fr.inrae.act.bagap.chloe.script;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.tab.CombinationExpressionPixel2PixelTabCalculation;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptCombine {

	public static void main(String[] args) {
		//scriptConvert();
		scriptCombine();
	}
	
	private static void scriptConvert(){
		
		Coverage cov = CoverageManager.getCoverage("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/sr_2018_10m.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/sr_2018_10m.tif", data, entete);
		
	}
	
	private static void scriptCombine(){
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";

		Coverage cov1 = CoverageManager.getCoverage(path+"pf_2018_10m.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(path+"sr_2018_10m.tif");
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] data = new float[data1.length];
		
		String formula = "m1*m2";
		String[] names = new String[]{"m1", "m2"};
		float[][] tabs = new float[][]{data1, data2};
		
		CombinationExpressionPixel2PixelTabCalculation cal = CombinationExpressionFactory.createPixel2PixelTabCalculation(data, formula, entete.noDataValue(), names, tabs);
		cal.run();
		
		CoverageManager.write(path+"comb2_2018_10m.tif", data, entete);	
	}
}
