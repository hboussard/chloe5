package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;

public class ChloeUtilAnalysisFactory {

	public static ChloeUtilAnalysis create(ChloeUtilAnalysisBuilder builder) throws IOException {

		if(builder.getAnalysisType() == ChloeAnalysisType.COMBINE){
			
			CombineAnalysis analysis = new CombineAnalysis(builder.getCombination(), builder.getNamesAndRasters(), builder.getOutputRaster());
			return analysis;
		}

		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognized analysis type");
	}

}
