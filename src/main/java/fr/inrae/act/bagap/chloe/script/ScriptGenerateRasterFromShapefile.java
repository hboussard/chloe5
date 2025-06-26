package fr.inrae.act.bagap.chloe.script;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptGenerateRasterFromShapefile {

	public static void main(String[] args) {
		
		long begin = System.currentTimeMillis();
		
		//scriptGenerateRasterFromShapefile();
		scriptGenerateRasterFromShapefileSebastien();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}

	private static void scriptGenerateRasterFromShapefile(){
		
		String outputRaster = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/from_shapefile_c5/raster_2018.tif";
		String shapefile = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/OCS_PUMA_ZA_2018.shp";
		String attribute = "OS_2018";
		int fillValue = 0;
		CoordinateReferenceSystem crs = ShapeFile2CoverageConverter.getCoordinateReferenceSystem(shapefile);
		try {
			if(!CRS.toSRS(crs).startsWith("EPSG")){
				 crs = CRS.decode("EPSG:2154");
			}
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		double minx = 356062.7355;
		double maxx = 370102.7355;
		double miny = 6824050.142;
		double maxy = 6839430.142;
		Envelope envelope = new Envelope(minx, maxx, miny, maxy);
		float cellSize = 10;
		int noDataValue = -1;
		EnteteRaster entete = EnteteRaster.getEntete(envelope, cellSize, noDataValue, crs);
		
		Util.createAccess(outputRaster);
				
		ShapeFile2CoverageConverter.rasterize(outputRaster, shapefile, attribute, fillValue, entete);
	
	}
	
	private static void scriptGenerateRasterFromShapefileSebastien(){
		
		String outputRaster = "C:/Data/temp/sebastien/data/raster_5m.tif";
		String shapefile = "C:/Data/temp/sebastien/data/Land_cover_map_UKCEH_2023.shp";
		String attribute = "LC_ID";
		int fillValue = -1;
		CoordinateReferenceSystem crs = ShapeFile2CoverageConverter.getCoordinateReferenceSystem(shapefile);
		System.out.println(crs);
		/*
		try {
			if(!CRS.toSRS(crs).startsWith("EPSG")){
				 crs = CRS.decode("EPSG:2154");
			}
		} catch (FactoryException e) {
			e.printStackTrace();
		}*/
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(shapefile);
		
		float cellSize = 5;
		int noDataValue = -1;
		EnteteRaster entete = EnteteRaster.getEntete(envelope, cellSize, noDataValue, crs);
		
		System.out.println(entete);
		
		Util.createAccess(outputRaster);
				
		ShapeFile2CoverageConverter.rasterize(outputRaster, shapefile, attribute, fillValue, entete);
	
	}
	
}
