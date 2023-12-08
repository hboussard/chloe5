package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.cluster.ClusterAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceType;

public class ChloeUtilAnalysisFactory {

	public static ChloeUtilAnalysis create(ChloeUtilAnalysisBuilder builder) throws IOException {

		if(builder.getAnalysisType() == ChloeAnalysisType.COMBINE){
			
			ChloeUtilAnalysis analysis = new CombineAnalysis(builder.getOutputRaster(), builder.getNamesAndRasters(), builder.getCombination());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.SEARCHANDREPLACE){
			
			ChloeUtilAnalysis analysis = new SearchAndReplaceAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getNoDataValue(), builder.getChanges());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.CLASSIFICATION){
			
			ChloeUtilAnalysis analysis = new ClassificationAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getDomains());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.RASTER_FROM_CSV){
			
			ChloeUtilAnalysis analysis = null;
			
			if(builder.getVariables().size() == 1){
				
				analysis = new RasterFromCsvAnalysis(builder.getOutputRaster(), builder.getCsvFile(), builder.getVariables().iterator().next(), 
						builder.getWidth(), builder.getHeight(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
				
			}else{
				
				analysis = new MultipleRasterFromCsvAnalysis(builder.getOutputFolder(), builder.getOutputPrefix(), builder.getOutputSuffix(), builder.getTypeMime(), builder.getCsvFile(), builder.getVariables(), 
						builder.getWidth(), builder.getHeight(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
				
			}
			
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.RASTER_FROM_SHAPEFILE){
			
			ChloeUtilAnalysis analysis = new RasterFromShapefileAnalysis(builder.getOutputRaster(), builder.getShapefile(), builder.getAttribute(),
					builder.getXMin(), builder.getXMax(), builder.getYMin(), builder.getYMax(), builder.getCellSize(), builder.getNoDataValue(), builder.getFillValue());
			
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.DISTANCE){
			
			ChloeUtilAnalysis analysis = new DistanceAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getRasterFile2(), builder.getDistanceType(), builder.getSources(), builder.getMaxDistance());
			
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.CLUSTER){
			
			ChloeUtilAnalysis analysis = new ClusterAnalysis(builder.getOutputRaster(), builder.getOutputCsv(), builder.getRasterFile(), builder.getRasterFile2(), builder.getClusterType(), builder.getSources(), builder.getMaxDistance());
			
			return analysis;
		}

		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognized analysis type");
	}

}
