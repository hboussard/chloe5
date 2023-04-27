package fr.inrae.act.bagap.chloe.window.output;

import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class TileAsciiGridOutput  extends TileRasterOutput {

	public TileAsciiGridOutput(String folder, Metric metric, Tile tile, int width, int height, double minX, double maxX,
			double minY, double maxY, double cellSize, int noDataValue) {
		super(folder, metric, tile, width, height, minX, maxX, minY, maxY, cellSize, noDataValue);
	}

	@Override
	protected void writeRaster(String file, float[] data, EnteteRaster entete) {
		CoverageManager.writeAsciiGrid(file+".asc", data, entete);
	}

}