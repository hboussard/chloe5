package fr.inrae.act.bagap.chloe.script;

import java.io.File;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptMotiver {

	private static String pathOccsol = "E:/data/caphaie/occsol/raster/france/";
	
	private static String pathMotiver = "C:/Data/projet/motiver/data/analyse/Sites_motiver/";
	
	public static void main(String[] args) {

		//recuperationOccSol();
		//calculateEcolandscape();
	}
	
	private static void calculateEcolandscape() {

		int[] ks = new int[] {5, 6, 7, 8 , 9, 10};
		
		//for(int index=0; index<6; index++) {
		for(int index=3; index<6; index++) {
			
			String occsol = pathMotiver+"OSO_site_"+index+".tif";
			
			
			ecolandscape(occsol, ks, new int[] {1000});
				
			ecolandscape(occsol, ks, new int[] {5000});
					
			ecolandscape(occsol, ks, new int[] {1000, 5000});
		}
		
	}
	
	private static void ecolandscape(String occsol, int[] ks, int... sizes) {
		
		System.out.println(occsol+" "+sizes[0]+" "+sizes.length);
		
		String completeName = new File(occsol).getName().replace(".tif", "");
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.addInputRaster(occsol);
		epManager.setScales(sizes);
		epManager.setOutputFolder(pathMotiver+"ecolandscape_"+completeName+"/");
		epManager.setClasses(ks);
		epManager.setUnfilters(new int[] {-1});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38});
		epManager.setCompositionMetrics();
		epManager.setConfigurationMetrics();
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}

	private static void recuperationOccSol() {
		
		for(int index=0; index<6; index++) {
			
			Coverage cov = CoverageManager.getCoverage(pathMotiver+"OS_site_"+index+".tif");
			EnteteRaster entete = cov.getEntete();
			cov.dispose();
			
			Coverage covOccsol = CoverageManager.getCoverage(pathOccsol);
			EnteteRaster enteteOcsol = covOccsol.getEntete();
			float[] dataOccsol = covOccsol.getData(EnteteRaster.getROI(enteteOcsol, entete.getEnvelope()));
			covOccsol.dispose();
			
			CoverageManager.write(pathMotiver+"OSO_site_"+index+".tif", dataOccsol, EnteteRaster.getEntete(enteteOcsol, entete.getEnvelope()));
		}
	}
	
}
