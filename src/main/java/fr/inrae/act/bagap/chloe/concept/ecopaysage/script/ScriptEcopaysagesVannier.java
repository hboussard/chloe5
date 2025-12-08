package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class ScriptEcopaysagesVannier {

	public static void main(String[] args) {
		ecolandscape();
		
		//clean();
	}
	
	private static void clean() {
		
		Coverage covLCDB = CoverageManager.getCoverage("C:/Data/projet/vannier/data/LCDB_100m_reclass7_clean.tif");
		EnteteRaster entete = covLCDB.getEntete();
		covLCDB.dispose();
		
		System.out.println(entete);
		
		Coverage cov = CoverageManager.getCoverage("C:/Data/projet/vannier/data/ecopaysages/ecopaysages_LCDB_100m_reclass7_clean_15classes_5000m.tif");
		EnteteRaster entete2 = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		System.out.println(entete2);
	
	}
	
	private static void ecolandscape() {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setForce(true);
		epManager.addInputRaster("C:/Data/projet/vannier/data/LCDB_100m_reclass7_clean.tif");
		epManager.setScales(new int[]{5000});
		epManager.setClasses(new int[]{15});
		epManager.setUnfilters(new int[]{-1});
		epManager.setOutputFolder("C:/Data/projet/vannier/data/ecopaysages/");
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
