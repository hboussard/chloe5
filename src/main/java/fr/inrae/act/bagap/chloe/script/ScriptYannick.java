package fr.inrae.act.bagap.chloe.script;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptYannick {

	public static void main(String[] args) {
		rasterize2();
		//test();
	}
	
	public static void rasterize2() {
		String path = "F:/data/sig/data_ZA/";
		String inputShape = path+"Haies_conforme/Haies_conforme/PF_Haies_2006.shp";
		
		String refRaster = path+"PF_OS_L93/raster_5m/os_za_2015.tif";
		Coverage cov = CoverageManager.getCoverage(refRaster);
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		System.out.println(entete);
		
		String outputRaster = path+"PF_OS_L93/raster_5m/PF_Haies_2006.tif";
		
		ShapeFile2CoverageConverter.rasterize(outputRaster, inputShape, "RASTER", -1, entete);
		
	}

	public static void rasterize() {
		String path = "C:/Data/temp/yannick/";
		String inputShape = path+"Donnee_Com_com/90_elts_arbores_type.shp";
		
		/*
		String refRaster = path+"Raster-Test/raster_test_5m.tif";
		Coverage cov = CoverageManager.getCoverage(refRaster);
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		System.out.println(entete);
		
		String outputRaster = path+"Raster-Test/test.tif";
		
		ShapeFile2CoverageConverter.rasterize(outputRaster, inputShape, "RASTER", -1, entete);
		*/
		
		String outputRaster = path+"Donnee_Com_com/90_elts_arbores_type.tif";
		
		ShapeFile2CoverageConverter.rasterize(outputRaster, inputShape, "RASTER", 5.0f, -1, null);
		
	}
	
	public static void test() {
		String path = "C:\\Data\\temp\\jacques\\CARTES_CLASS_ndvi\\CARTES_CLASS_ndvi_clean/";
		String inputRaster = path+"20230215TC.tif";
		
		String outputRaster = path+"test2.tif";
		
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
	
		CoverageManager.write(outputRaster, data, entete);		
	}
	
}
