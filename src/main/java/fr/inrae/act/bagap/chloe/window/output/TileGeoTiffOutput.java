package fr.inrae.act.bagap.chloe.window.output;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class TileGeoTiffOutput extends TileRasterOutput {

	public TileGeoTiffOutput(String folder, Metric metric, Tile tile, int width, int height, double minX, double maxX,
			double minY, double maxY, double cellSize, int noDataValue, CoordinateReferenceSystem crs) {
		super(folder, metric, tile, width, height, minX, maxX, minY, maxY, cellSize, noDataValue, crs);
	}

	@Override
	protected void writeRaster(String file, float[] data, EnteteRaster entete) {
		CoverageManager.writeGeotiff(file+".tif", data, entete);
	}

}
