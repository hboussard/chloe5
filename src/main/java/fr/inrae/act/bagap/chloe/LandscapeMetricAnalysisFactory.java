package fr.inrae.act.bagap.chloe;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.FileCoverage;
import fr.inrae.act.bagap.raster.TabCoverage;

public class LandscapeMetricAnalysisFactory {
	
	private static int maxTile = 500000;
	
	public static int maxTile(){
		return maxTile;
	}
	
	private static int bufferSize = 10;
	
	public static int bufferSize(){
		return bufferSize;
	}

	public static LandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder) throws IOException {
		
		//GridCoverage2D coverage;
		Coverage coverage;
		int inWidth;
		int inHeight;
		float inCellSize;
		
		if(builder.getRasterFile() != null){
			// coverage et infos associees
			GridCoverage2DReader reader;
			if(builder.getRasterFile().endsWith(".asc")){
				File file = new File(builder.getRasterFile());
				reader = new ArcGridReader(file);
			}else if(builder.getRasterFile().endsWith(".tif")){
				File file = new File(builder.getRasterFile());
				reader = new GeoTiffReader(file);
			}else{
				throw new IllegalArgumentException(builder.getRasterFile()+" is not a recognize raster");
			}
			GridCoverage2D coverage2D = (GridCoverage2D) reader.read(null);
			reader.dispose(); 
			
			inWidth = (Integer) coverage2D.getProperty("image_width");
			inHeight = (Integer) coverage2D.getProperty("image_height");
			double inMinX = coverage2D.getEnvelope().getMinimum(0);
			double inMinY = coverage2D.getEnvelope().getMinimum(1);
			double inMaxX = coverage2D.getEnvelope().getMaximum(0);
			double inMaxY = coverage2D.getEnvelope().getMaximum(1);
			inCellSize = (float) ((java.awt.geom.AffineTransform) coverage2D.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			EnteteRaster entete = new EnteteRaster(inWidth, inHeight, inMinX, inMaxX, inMinY, inMaxY, inCellSize, -1);
			coverage = new FileCoverage(coverage2D, entete);
			
		}else if(builder.getRasterTab() != null){
			
			inWidth = builder.getEntete().width();
			inHeight = builder.getEntete().height();
			inCellSize = builder.getEntete().cellsize();
			
			coverage = new TabCoverage(builder.getRasterTab(), builder.getEntete());
			
		}else{
			throw new IllegalArgumentException("no raster declared");
		}
				
		int roiWidth = builder.getROIWidth();
		int roiHeight = builder.getROIHeight();
		
		if(builder.getAnalysisType() == WindowAnalysisType.SLIDING){
				
			// windowSize
			int windowSize = -1;
			if(builder.getWindowSize() > 0){
				windowSize = builder.getWindowSize();
			}else if(builder.getWindowRadius() > 0){
				double v = (2 * builder.getWindowRadius() / inCellSize);
				windowSize = v%2==0?new Double(v+1).intValue():new Double(v).intValue();
			}else{
				throw new IllegalArgumentException("windowSize must be defined");
			}
			int midWindowSize = (int) (windowSize/2);
			
			int maxWidth = -1;
			int maxHeight = -1;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth + 2 * midWindowSize;
				maxHeight = roiHeight + 2 * midWindowSize;
			}
			
			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (maxTile/1000.0)){
				return SingleLandscapeMetricAnalysisFactory.create(builder, coverage);
			}else{
				return HugeLandscapeMetricAnalysisFactory.create(builder, coverage);
			}
			
			//return HugeLandscapeMetricAnalysisFactory.create(builder, coverage);
			//return SingleLandscapeMetricAnalysisFactory.create(builder, coverage);
			
		}else if(builder.getAnalysisType() == WindowAnalysisType.SELECTED){
			/*
			// gestion du selected
			if(builder.getAnalysisType() == WindowAnalysisType.SELECTED){
				Set<Pixel> pixels = new TreeSet<Pixel>();
				// TODO
			}
			*/
		}else if(builder.getAnalysisType() == WindowAnalysisType.AREA){
			return AreaLandscapeMetricAnalysisFactory.create(builder, coverage);
		}
		
		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognize analysis type");
	}

	
}
