package fr.inrae.act.bagap.chloe.util.analysis;

import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class RasterFromShapefileAnalysis extends ChloeUtilAnalysis {
	
	private String outputRaster;
	
	private String inputShapefile;
	
	private String attribute;
	
	private double minx, maxx, miny, maxy;
	
	private float cellSize;
	
	private int noDataValue;
	
	private float fillValue;
	
	private EnteteRaster entete;

	public RasterFromShapefileAnalysis(String outputRaster, String inputShapefile, String attribute, double minx, double maxx, double miny, double maxy, float cellSize, int noDataValue, float fillValue){
		this.outputRaster = outputRaster;
		this.inputShapefile = inputShapefile;
		this.attribute = attribute;
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.fillValue = fillValue;
	}
	
	@Override
	protected void doInit() {
		
		CoordinateReferenceSystem crs = ShapeFile2CoverageConverter.getCoordinateReferenceSystem(inputShapefile);
		
		try {
			if(!CRS.toSRS(crs).startsWith("EPSG")){
				 crs = CRS.decode("EPSG:2154");
			}
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		entete = EnteteRaster.getEntete(new Envelope(minx, maxx, miny, maxy), cellSize, noDataValue, crs);
		
		Util.createAccess(outputRaster);
	}

	@Override
	protected void doRun() {
		
		ShapeFile2CoverageConverter.rasterize(outputRaster, inputShapefile, attribute, fillValue, entete);

	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
