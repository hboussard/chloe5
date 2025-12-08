package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptLaure {

	private static String path = "C:/Data/temp/laure/";
	private static String path_17 = "D:/grain_bocager/data/17/2021/";
	private static String path_79 = "D:/grain_bocager/data/79/2023/"; 
	
	public static void main(String[] args) {
		
		//recale();
		
		compile();
	}
	
	private static void compile() {
		
		Coverage cov79 = CoverageManager.getCoverage(path+"79_2023_type_boisement.tif");
		float[] data79 = cov79.getData();
		EnteteRaster entete = cov79.getEntete();
		cov79.dispose();
		
		Coverage cov17 = CoverageManager.getCoverage(path+"17_2021_type_boisement.tif");
		float[] data17 = cov17.getData();
		cov17.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data79, data17){

			@Override
			protected float doTreat(float[] v) {
				float v79 = v[0];
				float v17 = v[1];
				return Math.max(v79, v17);
			}
		};
		cal.run();
		
		CoverageManager.write(path+"type_boisement.tif", data, entete);
		
	}
	
	private static void recale() {
		
		Coverage covRef = CoverageManager.getCoverage(path+"final_vrai_bio23_conv23.tif");
		EnteteRaster enteteRef = covRef.getEntete();
		covRef.dispose();
		
		Coverage cov79 = CoverageManager.getCoverage(path_79+"79_2023_type_boisement.tif");
		EnteteRaster entete79 = cov79.getEntete();
		float[] data79 = cov79.getData(EnteteRaster.getROI(entete79, enteteRef.getEnvelope()));
		cov79.dispose();
		
		CoverageManager.write(path+"79_2023_type_boisement.tif", data79, enteteRef);
		
		Coverage cov17 = CoverageManager.getCoverage(path_17+"17_2021_type_boisement.tif");
		EnteteRaster entete17 = cov17.getEntete();
		float[] data17 = cov17.getData(EnteteRaster.getROI(entete17, enteteRef.getEnvelope()));
		cov17.dispose();
		
		CoverageManager.write(path+"17_2021_type_boisement.tif", data17, enteteRef);
	}
	
}
