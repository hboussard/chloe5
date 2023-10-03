package fr.inrae.act.bagap.chloe.script;

import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptSearchAndReplace {

	public static void main(String[] args) {
		scriptSearchAndReplace();
	}
	
	private static void scriptSearchAndReplace(){
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";

		Coverage cov1 = CoverageManager.getCoverage(path+"pf_2018_10m.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		float[] data = new float[data1.length];
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(61f, 6f);
		sarMap.put(700f, 17f);
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, data1, sarMap);
		cal.run();
		
		CoverageManager.write(path+"sar/sar_2018_10m.tif", data, entete);	
	}
}
