package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptFlorence {

	public static void main(String[] args) {
		
		scriptSliding();
	}

	private static void scriptSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.addRasterFile(path+"os_za_2016.tif");
		builder.setRasterFile2(path+"friction.tif");
		builder.addMetric("SHDI");
		builder.addWindowSize(21);
		//builder.setUnfilters(new int[]{3});
		builder.setFilters(new int[]{3});
		builder.addGeoTiffOutput("SHDI", path+"test/SHDI_F3.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
}
