package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.FileCoverage;

public class Test {

	public static void main(String[] args) {

		// coverage et infos associees
		GridCoverage2DReader reader;
		GridCoverage2D coverage2D = null;
		try {
			reader = new GeoTiffReader(new File("H:/amazonie/data/mapbiomas-brazil-collection-70-amazonia-2019.tif"));
			coverage2D =  (GridCoverage2D) reader.read(null);
			reader.dispose(); 
		} catch (DataSourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int inWidth = (Integer) coverage2D.getProperty("image_width");
		int inHeight = (Integer) coverage2D.getProperty("image_height");
		double inMinX = coverage2D.getEnvelope().getMinimum(0);
		double inMinY = coverage2D.getEnvelope().getMinimum(1);
		double inMaxX = coverage2D.getEnvelope().getMaximum(0);
		double inMaxY = coverage2D.getEnvelope().getMaximum(1);
		float inCellSize = (float) ((java.awt.geom.AffineTransform) coverage2D.getGridGeometry().getGridToCRS2D()).getScaleX();
		
		EnteteRaster entete = new EnteteRaster(inWidth, inHeight, inMinX, inMaxX, inMinY, inMaxY, inCellSize, -1);
		Coverage coverage = new FileCoverage(coverage2D, entete);
		
		int midWindowSize = 833;
		
		// ROI
		int roiX = 0;
		int roiY = 0;
		int roiWidth = inWidth;
		int roiHeight = inHeight;
					
		// buffer ROI
		int bufferROIXMin = Math.min(roiX, midWindowSize);
		int bufferROIXMax = Math.min(inWidth-(roiX+roiWidth), midWindowSize);
		int bufferROIYMin = Math.min(roiY, midWindowSize);
		int bufferROIYMax = Math.min(inHeight-(roiY+roiHeight), midWindowSize);
		
		int tileYSize = 1000;
		Rectangle roi;
		int localBufferROIYMin, localBufferROIYMax, tYs;
		
		for(int localROIY=roiY; localROIY<roiY+roiHeight; localROIY+=tileYSize){
			
			localBufferROIYMin = Math.min(localROIY+bufferROIYMin, midWindowSize);
			localBufferROIYMax = Math.min(Math.max(bufferROIYMax, roiHeight+bufferROIYMax-(localROIY+tileYSize)), midWindowSize);
			tYs = Math.min(tileYSize, roiHeight + roiY - localROIY );
			
			System.out.println("local ROI "+localROIY+" "+roiHeight+" "+localBufferROIYMin+" "+localBufferROIYMax);
			
			// recuperation des donnees depuis le coverage
			roi = new Rectangle(roiX - bufferROIXMin, localROIY - localBufferROIYMin, roiWidth + bufferROIXMin + bufferROIXMax, tYs + localBufferROIYMin + localBufferROIYMax);
			coverage.getDatas(roi);
			
		}
		
		coverage.dispose();
	}

}
