package fr.inrae.act.bagap.chloe.script;

import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptBoisnot {

	public static void main(String[] args) {
		rechercherRemplacer();
	}

	private static void rechercherRemplacer() {

		Coverage cov = CoverageManager.getCoverage("C:/Data/temp/boisnot/gb2019lcm25m_band1_bis.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		//CoverageManager.write("C:/Data/temp/boisnot/gb2019lcm25m_band1_bis.tif", data, entete);
		
		float[] outData = new float[data.length]; 
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(5f,4f);
		sarMap.put(6f,5f);
		sarMap.put(7f,5f);
		sarMap.put(8f,6f);
		sarMap.put(9f,6f);
		sarMap.put(10f,6f);
		sarMap.put(11f,6f);
		sarMap.put(12f,6f);
		sarMap.put(13f,7f);
		sarMap.put(14f,7f);
		sarMap.put(15f,8f);
		sarMap.put(16f,8f);
		sarMap.put(17f,8f);
		sarMap.put(18f,8f);
		sarMap.put(19f,8f);
		sarMap.put(20f,9f);
		sarMap.put(21f,9f);
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(outData, data, sarMap);
		cal.run();
		
		CoverageManager.write("C:/Data/temp/boisnot/gb2019lcm25m_band1_ter.tif", outData, entete);
		
	}

}
