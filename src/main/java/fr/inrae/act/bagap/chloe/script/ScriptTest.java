package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptTest {

	private static String path = "H:/temp/leo_boudric/";
	
	public static void main(String[] args){
		
		Coverage cov1 = CoverageManager.getCoverage(path+"data/INDICATEURS/BENTHOS GLOBAL_SG_clean.tif");
		EnteteRaster entete = cov1.getEntete();
		//float[] data1 = cov1.getData();
		cov1.dispose();
		
		System.out.println(entete);
		
		EnteteRaster.export(entete, path+"data/INDICATEURS/entete.txt");
		
	}
	
}
