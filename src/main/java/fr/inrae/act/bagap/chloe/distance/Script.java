package fr.inrae.act.bagap.chloe.distance;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.TabCoverage;

public class Script {

	public static void main(String[] args){
		
		//String inGO2021 = "G:/data/sig/grand_ouest/GO_2021_ebr.tif";
		//String outGO2021 = "H:/temp/cont/GO_2021_ebr.tif";
		//CoverageManager.retile(inGO2021, outGO2021, 240862.1895, 305697.9458, 6790989.3038, 6827821.8997);
		
		/*
		String inGO2021 = "H:/temp/cont/GO_2021_ebr.tif";
		String permGO2021 = "H:/temp/cont/permeability.tif";
		Coverage covGO = CoverageManager.getCoverage(inGO2021);
		float[] dataGo = covGO.getDatas();
		EnteteRaster entete = covGO.getEntete();
		covGo.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Map<Float, Float> sarMap = new HashMap<Float, Float>();
		sarMap.put(0f, 10f);
		sarMap.put(1f, 10f);
		sarMap.put(2f, 10f);
		sarMap.put(3f, 10f);
		sarMap.put(4f, 1f);
		sarMap.put(5f, 1f);
		sarMap.put(6f, 5f);
		sarMap.put(7f, 5f);
		sarMap.put(9f, 1f);
		sarMap.put(10f, 5f);
		sarMap.put(12f, 5f);
		sarMap.put(13f, 0.1f);
		sarMap.put(14f, 1f);
		sarMap.put(15f, 1f);
		sarMap.put(16f, 10f);
		sarMap.put(17f, 10f);
		sarMap.put(18f, 10f);
		sarMap.put(19f, 5f);
		sarMap.put(20f, 5f);
		sarMap.put(22f, 10f);
		sarMap.put(23f, 1f);
		
		SearchAndReplacePixel2PixelTabCalculation pptc = new SearchAndReplacePixel2PixelTabCalculation(data, dataGo, sarMap);
		pptc.run();

		CoverageManager.writeGeotiff(permGO2021, data, entete);
		*/
		
		String inGO2021 = "H:/temp/cont/GO_2021_ebr.tif";
		Coverage covGO = CoverageManager.getCoverage(inGO2021);
		float[] dataGo = covGO.getData();
		EnteteRaster entete = covGO.getEntete();
		covGO.dispose();
		
		String permGO2021 = "H:/temp/cont/permeability.tif";
		Coverage covPerm = CoverageManager.getCoverage(permGO2021);
		float[] dataPerm = covPerm.getData();
		covPerm.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, dataGo, dataPerm, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{13});
		analysis.allRun();
		
		String rasterCont = "H:/temp/cont/continuity.tif";
		CoverageManager.writeGeotiff(rasterCont, data, entete);
		
		
		//scriptHeterogeneity();
	}
	
	private static void scriptHeterogeneity(){

		String inGO2021 = "H:/temp/cont/GO_2021_ebr.tif";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(inGO2021);
		builder.setWindowSize(201);
		builder.setDisplacement(20);
	
		builder.addMetric("SHDI");
		builder.addGeoTiffOutput("SHDI", "H:/temp/cont/shdi.tif"); 
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
}
