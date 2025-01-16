package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.api.PluginProgression;
import fr.inrae.act.bagap.chloe.cluster.ClusterAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceAnalysis;

public class ChloeUtilAnalysisFactory {

	public static ChloeUtilAnalysis create(ChloeUtilAnalysisBuilder builder) throws IOException {

		ChloeUtilAnalysis analysis = null;
		PluginProgression pp;
		
		if(builder.getAnalysisType() == ChloeAnalysisType.COMBINE){
			
			analysis = new CombineAnalysis(builder.getOutputRaster(), builder.getNamesAndRasters(), builder.getCombination());
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.SEARCHANDREPLACE){
			
			analysis = new SearchAndReplaceAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getNoDataValue(), builder.getChanges());
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.CLASSIFICATION){
			
			analysis = new ClassificationAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getDomains());
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.OVERLAY){
			
			analysis = new OverlayAnalysis(builder.getOutputRaster(), builder.getRasterFiles());
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.RASTER_FROM_CSV){
			
			if(builder.getVariables().size() == 1){
				
				analysis = new RasterFromCsvAnalysis(builder.getOutputRaster(), builder.getCsvFile(), builder.getVariables().iterator().next(), 
						builder.getWidth(), builder.getHeight(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
				
			}else{
				
				analysis = new MultipleRasterFromCsvAnalysis(builder.getOutputFolder(), builder.getOutputPrefix(), builder.getOutputSuffix(), builder.getTypeMime(), builder.getCsvFile(), builder.getVariables(), 
						builder.getWidth(), builder.getHeight(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
			}
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.RASTER_FROM_SHAPEFILE){
			
			analysis = new RasterFromShapefileAnalysis(builder.getOutputRaster(), builder.getShapefile(), builder.getAttribute(),
					builder.getXMin(), builder.getXMax(), builder.getYMin(), builder.getYMax(), builder.getCellSize(), builder.getNoDataValue(), builder.getFillValue());
			
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.DISTANCE){
			
			analysis = new DistanceAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getRasterFile2(), builder.getDistanceType(), builder.getSources(), builder.getMaxDistance());
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.CLUSTER){
			
			analysis = new ClusterAnalysis(builder.getOutputRaster(), builder.getOutputCsv(), builder.getRasterFile(), builder.getRasterFile2(), builder.getClusterType(), builder.getSources(), builder.getMaxDistance());
			
		}else{
			
			throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognized analysis type");
		}
		
		return analysis;
	}

}
