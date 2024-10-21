package fr.inrae.act.bagap.chloe.script;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;

import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptSliding {

	public static void main(String[] args) {
		
		//rasterize();
		scriptSliding();
	}
	
	private static void rasterize() {
		try {
			ShapeFile2CoverageConverter.rasterize("D:/data/sig/data_ZA/PF_OS_L93/PF_2018/OS_2018_5m.tif", "D:/data/sig/data_ZA/PF_OS_L93/PF_2018/OCS_PUMA_ZA_2018_L93.shp", "OS_2018", 5, -1, CRS.decode("EPSG:2154"));
			
		} catch (FactoryException e) {
			e.printStackTrace();
		}
	}
	
	private static void convert(String input, String output){
		
		Coverage cov = CoverageManager.getCoverage(input);
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write(output, data, entete);
	}
	
	private static void scriptSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"OS_2018_5m.tif");
		builder.addMetric("SHDI");
		builder.addWindowSize(201);
		builder.setUnfilters(new int[]{-1});
		builder.addGeoTiffOutput("SHDI", path+"test3/SHDI_fast_201p.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
}
