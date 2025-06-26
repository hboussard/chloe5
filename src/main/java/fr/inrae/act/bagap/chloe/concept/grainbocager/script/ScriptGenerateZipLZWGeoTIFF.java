package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptGenerateZipLZWGeoTIFF {

	public static void main(String [] args) {
		
		//convert();
	}

	private static void convert() {
		
		String path = "D:\\grain_bocager\\data\\01\\2021/";
		String outputPath = "C:\\Data\\temp\\grain_bocager/";
		
		File folder = new File(path);
		for(String f : folder.list()) {
			System.out.println(path+f);
			if(f.endsWith(".tif")) {
				
				Coverage cov = CoverageManager.getCoverage(path+f);
				float[] data = cov.getData();
				EnteteRaster entete = cov.getEntete();
				cov.dispose();
				
				//CoverageManager.writeGeotiff(outputPath+f, data, entete, "LZW");
				
			}else if(f.endsWith(".qml")) {
				
			}
		}
	}
	
	private static void compress() {
		
		
	}
}
