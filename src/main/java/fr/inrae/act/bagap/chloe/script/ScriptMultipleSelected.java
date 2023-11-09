package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.tab.CombinationExpressionPixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptMultipleSelected {

	public static void main(String[] args) {
		//scriptSplit();
		scriptMultipleSelected();
	}
	
	private static void scriptSplit(){
		
		Coverage cov = CoverageManager.getCoverage("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/pf_2018_10m.tif");
		EnteteRaster enteteRef = cov.getEntete();
		
		Envelope env = new Envelope(362500, 364500, 6830000 ,6832000);
		Rectangle roi = EnteteRaster.getROI(enteteRef, env);
		
		float[] data = cov.getData(roi);
		EnteteRaster entete = EnteteRaster.getEntete(enteteRef, env);
		cov.dispose();
		
		CoverageManager.write("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/mini/landscape2.tif", data, entete);
		
	}
	
	private static void scriptMultipleSelected(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.addRasterFile(path+"mini/landscape1.tif");
		builder.addRasterFile(path+"mini/landscape2.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addMetric("NP");
		builder.addMetric("LPI");
		builder.addMetric("MPS");
		
		// analyse avec une seule taille de fenêtre
		//builder.setWindowSize(31);
		// analyse avec plusieurs tailles de fenêtre
		builder.setWindowSizes(new int[]{31,51});
		
		// 1
		builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf.csv");
		builder.addCsvOutput(path+"mini/output/selected_pixels.csv");
		
		// 2
		//builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf_ID.csv");
		//builder.addCsvOutput(path+"mini/output/selected_pixels_ID.csv");
		
		// 3
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf.csv");
		//builder.addCsvOutput(path+"mini/output/selected_points.csv");
		
		// 4
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_decal.csv");
		//builder.addCsvOutput(path+"mini/output/selected_points_decal.csv");
		
		// 5
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID.csv");
		//builder.addCsvOutput(path+"mini/output/selected_points_ID.csv");
	
		// 6
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID_decal.csv");
		//builder.addCsvOutput(path+"mini/output/selected_points_ID_decal.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
}
