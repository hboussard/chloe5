package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;

public class ChloeUtilAnalysisFactory {

	public static ChloeUtilAnalysis create(ChloeUtilAnalysisBuilder builder) throws IOException {

		if(builder.getAnalysisType() == ChloeAnalysisType.COMBINE){
			
			CombineAnalysis analysis = new CombineAnalysis(builder.getOutputRaster(), builder.getNamesAndRasters(), builder.getCombination());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.SEARCHANDREPLACE){
			
			SearchAndReplaceAnalysis analysis = new SearchAndReplaceAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getNoDataValue(), builder.getChanges());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.CLASSIFICATION){
			
			ClassificationAnalysis analysis = new ClassificationAnalysis(builder.getOutputRaster(), builder.getRasterFile(), builder.getDomains());
			return analysis;
		}
		if(builder.getAnalysisType() == ChloeAnalysisType.RASTER_FROM_CSV){
			
			ChloeUtilAnalysis analysis = null;
			
			if(builder.getVariables().size() == 1){
				
				analysis = new RasterFromCsvAnalysis(builder.getOutputRaster(), builder.getCsvFile(), builder.getVariables().iterator().next(), 
						builder.getNCols(), builder.getNRows(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
				
			}else{
				
				analysis = new MultipleRasterFromCsvAnalysis(builder.getOutputFolder(), builder.getOutputPrefix(), builder.getOutputSuffix(), builder.getTypeMime(), builder.getCsvFile(), builder.getVariables(), 
						builder.getNCols(), builder.getNRows(), builder.getXMin(), builder.getYMin(), builder.getCellSize(), builder.getNoDataValue());
				
			}
			
			return analysis;
		}

		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognized analysis type");
	}

}
