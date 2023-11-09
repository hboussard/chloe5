package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class MultipleSelectedLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput;
	
	private Set<Integer> coherences;
	
	private List<String> csvOutputs;
	
	private List<String> suffixMetrics;
	
	public MultipleSelectedLandscapeMetricAnalysis(LandscapeMetricAnalysisBuilder builder) {
		super(builder);
	}
	
	@Override
	protected void doInit() {
		try {
			totalMetrics = builder.getMetrics();
			totalCsvOutput = builder.getCsv();
			
			String path = null;
			if(totalCsvOutput != null){
				path = new File(totalCsvOutput).getParent();
				csvOutputs = new ArrayList<String>();
				suffixMetrics = new ArrayList<String>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics = new HashSet<Metric>();
			
			for(String rasterFile : builder.getRasterFiles()){
				String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
				builder.setRasterFile(rasterFile);
				builder.setPixelsFilter((Set<? extends Pixel>) null); 
				for(int ws : builder.getWindowSizes()){
					builder.setWindowSize(ws);
					
					for(int coherence : coherences){
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						if(coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null){
							builder.addCsvOutput(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
							csvOutputs.add(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
							suffixMetrics.add("_"+ws);
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
					
						metrics = new HashSet<Metric>();
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() {
		for(LandscapeMetricAnalysis analysis : analyses){
			analysis.allRun();
		}
	}

	@Override
	protected void doClose() {
		
		if(totalCsvOutput != null){
			
			EnteteRaster entete = builder.getEntete();
			Set<Pixel> pixels = builder.getRefPixels();
			SpatialCsvManager.mergeFromPixels(totalCsvOutput, csvOutputs.toArray(new String[csvOutputs.size()]), suffixMetrics.toArray(new String[suffixMetrics.size()]), entete, pixels);
			
			for(String csvOut : csvOutputs){
				new File(csvOut).delete();
			}
			
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
