package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.FileCoverage;

public class Test {

	public static void main(String[] args) {

		//Coverage cov = CoverageManager.getCoverage("H:/temp/test/za.tif");
		
		Coverage cov = CoverageManager.getCoverage("H:/temp/test/ascii1.asc");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		System.out.println(entete);
		
		CoverageManager.writeGeotiff("H:/temp/test/ascii1.tif", data, entete);
		
		Coverage cov2 = CoverageManager.getCoverage("H:/temp/test/ascii1.tif");
		EnteteRaster entete2 = cov2.getEntete();
		System.out.println(entete2.noDataValue());
		cov2.dispose();
	}

}
