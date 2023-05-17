package fr.inrae.act.bagap.chloe.distance.output;

import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class TileGeoTiffDistanceOutput extends TileRasterDistanceOutput {

	public TileGeoTiffDistanceOutput(String folder, String name, Tile tile, int width, int height, int maxTile, double minX, double maxX, double minY, double maxY, double cellSize, int noDataValue){
		super(folder, name, tile, width, height, maxTile, minX, maxX, minY, maxY, cellSize, noDataValue);
	}

	@Override
	protected void writeRaster(String file, float[] data, EnteteRaster entete) {
		CoverageManager.writeGeotiff(file+".tif", data, entete);
	}
	
}
