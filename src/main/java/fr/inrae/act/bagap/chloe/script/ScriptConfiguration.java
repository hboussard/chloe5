package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptConfiguration {

	public static void main(String[] args) {
		/*
		sliding("HET-config", 1, 100);
		sliding("HET-config", 2, 100);
		sliding("HET-config", 5, 100);
		sliding("HET-config", 10, 100);
		
		sliding("HET-config", 1, 500);
		sliding("HET-config", 2, 500);
		sliding("HET-config", 5, 500);
		sliding("HET-config", 10, 500);
		*/
		/*
		sliding("SHDI", 1, 100);
		sliding("SHDI", 2, 100);
		sliding("SHDI", 5, 100);
		sliding("SHDI", 10, 100);
		
		sliding("SHDI", 1, 500);
		sliding("SHDI", 2, 500);
		sliding("SHDI", 5, 500);
		sliding("SHDI", 10, 500);
		
		sliding("MPS", 1, 100);
		sliding("MPS", 2, 100);
		sliding("MPS", 5, 100);
		sliding("MPS", 10, 100);
		
		sliding("MPS", 1, 500);
		sliding("MPS", 2, 500);
		sliding("MPS", 5, 500);
		sliding("MPS", 10, 500);
		
		sliding("HET-frag", 1, 100);
		sliding("HET-frag", 2, 100);
		sliding("HET-frag", 5, 100);
		sliding("HET-frag", 10, 100);
		
		sliding("HET-frag", 1, 500);
		sliding("HET-frag", 2, 500);
		sliding("HET-frag", 5, 500);
		sliding("HET-frag", 10, 500);
		*/
		sliding("EMS", 1, 100);
		sliding("EMS", 2, 100);
		sliding("EMS", 5, 100);
		sliding("EMS", 10, 100);
		
		sliding("EMS", 1, 500);
		sliding("EMS", 2, 500);
		sliding("EMS", 5, 500);
		sliding("EMS", 10, 500);
		
		/*
		sliding("HET", 1, 100);
		sliding("HET", 2, 100);
		sliding("HET", 5, 100);
		sliding("HET", 10, 100);
		
		sliding("HET", 1, 500);
		sliding("HET", 2, 500);
		sliding("HET", 5, 500);
		sliding("HET", 10, 500);
		*/
	}

	private static void sliding(String metric, int resolution, int scale){
		
		int ws = (scale*2/resolution)+1;
		int dep = ws / 10;
		
		String path = "D:/sig/data_ZA/PF_OS_L93/PF_2018/raster/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"pf_2018_"+resolution+"m.tif");
		builder.addMetric(metric);
		builder.addWindowSize(ws);
		builder.setDisplacement(dep);
		builder.setUnfilters(new int[]{-1});
		builder.addGeoTiffOutput(metric, path+"test2/"+metric+"_"+resolution+"m_"+scale+"m.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
	}
	
}
