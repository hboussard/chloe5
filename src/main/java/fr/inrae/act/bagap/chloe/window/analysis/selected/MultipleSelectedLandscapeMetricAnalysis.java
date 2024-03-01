package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.MultipleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class MultipleSelectedLandscapeMetricAnalysis extends MultipleLandscapeMetricAnalysis {

	private Set<Metric> totalMetrics;
	
	private String totalCsvOutput;
	
	private Set<Integer> coherences;
	
	//private List<String> csvOutputs;
	
	//private List<String> suffixMetrics;
	
	private Map<String, List<String>> csvOutputs;
	
	private Map<String, List<String>> suffixMetrics;
	
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
				//csvOutputs = new ArrayList<String>();
				//suffixMetrics = new ArrayList<String>();
				csvOutputs = new LinkedHashMap<String, List<String>>();
				suffixMetrics = new LinkedHashMap<String, List<String>>();
			}
			
			coherences = MetricManager.getCoherences(totalMetrics);
			Set<Metric> metrics;
			
			if(builder.getRasterFiles().size() == 0 || builder.getRasterFiles().size() == 1){
				
				String rasterFile = builder.getRasterFile();
			
				System.out.println("ici");
				
				if(totalCsvOutput != null){
					csvOutputs.put(rasterFile, new ArrayList<String>());
					suffixMetrics.put(rasterFile, new ArrayList<String>());
				}
				
				for(int ws : builder.getWindowSizes()){
					builder.setWindowSize(ws);
					
					for(int coherence : coherences){
						metrics = new HashSet<Metric>();
						metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
						if(coherence == 0){
							continue;
						}
						builder.setMetrics(metrics);
						
						if(totalCsvOutput != null){
							builder.addCsvOutput(path+"/selected_"+coherence+"_"+ws+".csv");
							csvOutputs.get(rasterFile).add(path+"/selected_"+coherence+"_"+ws+".csv");
							suffixMetrics.get(rasterFile).add("_"+ws);
						}
						
						add(LandscapeMetricAnalysisFactory.create(builder));
					}
				}
				
			}else {
				
				for(String rasterFile : builder.getRasterFiles()){
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					builder.setRasterFile(rasterFile);
					builder.setPixelsFilter((Set<? extends Pixel>) null); 
					
					if(totalCsvOutput != null){
						csvOutputs.put(rasterFile, new ArrayList<String>());
						suffixMetrics.put(rasterFile, new ArrayList<String>());
					}					
					
					for(int ws : builder.getWindowSizes()){
						builder.setWindowSize(ws);
						
						for(int coherence : coherences){
							metrics = new HashSet<Metric>();
							metrics.addAll(MetricManager.getMetricsByCoherence(totalMetrics, coherence));
							if(coherence == 0){
								continue;
							}
							builder.setMetrics(metrics);
							
							if(totalCsvOutput != null){
								builder.addCsvOutput(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
								//csvOutputs.add(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
								//suffixMetrics.add("_"+ws);
								csvOutputs.get(rasterFile).add(path+"/"+name+"_selected_"+coherence+"_"+ws+".csv");
								suffixMetrics.get(rasterFile).add("_"+ws);
							}
							
							add(LandscapeMetricAnalysisFactory.create(builder));
						}
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doClose() {
		
		if(totalCsvOutput != null){
			
			if(builder.getRasterFiles().size() == 0 || builder.getRasterFiles().size() == 1){
				
				String rasterFile = builder.getRasterFile();
			
				EnteteRaster entete = builder.getEntete();
				Set<Pixel> pixels = builder.getRefPixels();
				SpatialCsvManager.mergeFromPixels(totalCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.get(rasterFile).size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), entete, pixels);
				
				for(String csvOut : csvOutputs.get(rasterFile)){
					new File(csvOut).delete();
				}
			}else {
				
				int noDataValue = -1;
				Set<Pixel> pixels = null; 
				Map<String, String> localCsv = new TreeMap<String, String>();
				for(String rasterFile : builder.getRasterFiles()){
					
					
					String name = new File(rasterFile).getName().replace(".tif", "").replace(".asc", "");
					String localCsvOutput = totalCsvOutput.replace(".csv", "")+"_"+name+".csv";
					
					Coverage cov = CoverageManager.getCoverage(rasterFile);
					EnteteRaster entete = cov.getEntete();
					noDataValue = entete.noDataValue();
					cov.dispose();
					pixels = builder.getRefPixels();
					SpatialCsvManager.mergeFromPixels(localCsvOutput, csvOutputs.get(rasterFile).toArray(new String[csvOutputs.get(rasterFile).size()]), suffixMetrics.get(rasterFile).toArray(new String[suffixMetrics.get(rasterFile).size()]), entete, pixels);
					
					for(String csvOut : csvOutputs.get(rasterFile)){
						new File(csvOut).delete();
					}
					
					localCsv.put(localCsvOutput, new File(rasterFile).getName());	
					
				}
				
				SpatialCsvManager.mergeMapPixels(totalCsvOutput, localCsv, noDataValue, pixels);
				
				for(String csv : localCsv.keySet()){
					new File(csv).delete();
				}
			}
		}
		
		totalMetrics = null;
		coherences = null; 
		csvOutputs = null;
	}

}
