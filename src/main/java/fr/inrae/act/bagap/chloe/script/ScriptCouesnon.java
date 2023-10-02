package fr.inrae.act.bagap.chloe.script;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptCouesnon {

	public static void main(String[] args){
		
		convert();
		
	}
	
	private static void convert() {
		/*
		Coverage cov = CoverageManager.getCoverage("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_BAU_2050.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_BAU_2050.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_Biomass_2050.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_Biomass_2050.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_OS_2018.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_OS_2018.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_UtopiaBGIN_2050.asc");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("G:/Couesnon/analyse3/data/global/sum_proba/clean/sum_continuites_UtopiaBGIN_2050.tif", data, entete);
		*/
		
		
	}
	
}
