package fr.inrae.act.bagap.chloe;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;

public class LandscapeMetricAnalysisFactory {
	
	private static int maxTile = 100000000;
	
	public static int maxTile(){
		return maxTile;
	}
	
	private static int bufferSize = 100;
	
	public static int bufferSize(){
		return bufferSize;
	}

	public static LandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder) throws IOException {
		
		if(builder.getAnalysisType() == WindowAnalysisType.SLIDING){
			
			// coverage et infos associées
			GridCoverage2DReader reader;
			if(builder.getRaster().endsWith(".asc")){
				File file = new File(builder.getRaster());
				reader = new ArcGridReader(file);
			}else if(builder.getRaster().endsWith(".tif")){
				File file = new File(builder.getRaster());
				reader = new GeoTiffReader(file);
			}else{
				throw new IllegalArgumentException(builder.getRaster()+" is not a recognize raster");
			}
			GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			int inWidth = (Integer) coverage.getProperty("image_width");
			int inHeight = (Integer) coverage.getProperty("image_height");
			double inCellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			reader.dispose(); // à tester, ça va peut-être bloquer la lecture des données
			
			int roiWidth = builder.getROIWidth();
			int roiHeight = builder.getROIHeight();
			
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
			
			/*
			if(maxWidth * maxHeight <= maxTile){
				return SingleLandscapeMetricAnalysisFactory.create(builder, coverage);
			}else{
				return HugeLandscapeMetricAnalysisFactory.create(builder, coverage);
			}
			*/
			return HugeLandscapeMetricAnalysisFactory.create(builder, coverage);
			//return SingleLandscapeMetricAnalysisFactory.create(builder, coverage);
			
		}else if(builder.getAnalysisType() == WindowAnalysisType.SELECTED){
			/*
			// gestion du selected
			if(builder.getAnalysisType() == WindowAnalysisType.SELECTED){
				Set<Pixel> pixels = new TreeSet<Pixel>();
				// TODO
			}
			*/
		}
		
		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognize analysis type");
	}

	
}
