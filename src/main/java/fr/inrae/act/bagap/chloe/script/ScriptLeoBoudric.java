package fr.inrae.act.bagap.chloe.script;

import java.util.Map;
import java.util.TreeMap;

import org.geotools.referencing.CRS;

import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptLeoBoudric {

	public static void main(String[] args) {
		//scriptSearchAndReplace();
		//scriptSlidingLeo();
		scriptTest();
	}
	
	private static void scriptTest(){
		/*
		String path = "H:/temp/leo_boudric/data/INDICATEURS/";
		Coverage cov = CoverageManager.getCoverage(path+"BENTHOS GLOBAL_SG.tif");
		//float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		//for(float v : data){
		//	if( v != -3.4E38f){
		//		System.out.println(v);	
		//	}
		//}
		
		System.out.println(CRS.toSRS(entete.crs()));
		*/
		
		//for(float v : data){
		//	if( v != -3.4E38f){
		//		System.out.println(v);	
		//	}
		//}

	}
	
	private static void scriptSlidingLeo(){
		
		long begin = System.currentTimeMillis();
		
		String path = "H:/temp/leo_boudric/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif");
		builder.addMetric("average");
		builder.addWindowSize(101);
		builder.setUnfilters(new int[]{-1});
		//builder.setDisplacement(20);
		builder.addGeoTiffOutput("average", path+"sliding/average.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptSearchAndReplace(){
		String path = "H:/temp/leo_boudric/";

		Coverage cov1 = CoverageManager.getCoverage(path+"data/INDICATEURS/BENTHOS GLOBAL_SG.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] data = new float[data1.length];
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(-3.4E38f, -1f);
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, data1, sarMap);
		cal.run();
		
		CoverageManager.write(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif", data, entete);	
	}
	
}
