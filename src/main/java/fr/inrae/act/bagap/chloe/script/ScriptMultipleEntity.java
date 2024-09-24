package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMultipleEntity {

	public static void main(String[] args) {
		analyseEntity();
		
	}
	
	private static void analyseEntity(){
		
		String path = "E:/FRC_AURA/data/grain2d/CVB/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.addRasterFile(path+"avant/avant_grain_bocager_5m_4classes.tif");
		builder.setEntityRasterFile(path+"communes.tif");
		builder.addMetric("Nclass");
		builder.addMetric("Majority");	
		builder.addMetric("NV_1");
		builder.addMetric("NV_2");
		builder.addMetric("NV_3");
		builder.addMetric("NV_4");
		builder.addMetric("pNV_1");
		builder.addMetric("pNV_2");
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.setGeoTiffOutputFolder(path+"avant/communes2/");
		builder.setCsvOutputFolder(path+"avant/communes2/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
