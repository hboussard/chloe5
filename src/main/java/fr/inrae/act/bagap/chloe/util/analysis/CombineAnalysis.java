package fr.inrae.act.bagap.chloe.util.analysis;

import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.tab.CombinationExpressionPixel2PixelTabCalculation;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class CombineAnalysis extends ChloeUtilAnalysis {

	private String combination;
	
	private Map<String, String> factors;
	
	private EnteteRaster entete;
	
	private String[] names;
	
	private float[][] tabs;
	
	private float[] outData;
	
	private String outputRaster;
	
	public CombineAnalysis(String outputRaster, Map<String, String> factors, String combination){
		this.combination = combination;
		this.factors = factors;
		this.outputRaster = outputRaster;
	}
	
	@Override
	protected void doInit() {
		
		names = new String[factors.size()];
		tabs = new float[factors.size()][];
		Coverage cov;
		int ind = 0;
		for(Entry<String, String> factor : factors.entrySet()){
			names[ind] = factor.getKey();
			cov = CoverageManager.getCoverage(factor.getValue());
			if(entete == null){
				entete = cov.getEntete();
			}
			tabs[ind] = cov.getData();
			cov.dispose();
			ind++;
		}
		
		outData = new float[entete.width()*entete.height()];
		
		factors.clear();
		factors = null;
	}

	@Override
	protected void doRun() {
		CombinationExpressionPixel2PixelTabCalculation cal = CombinationExpressionFactory.createPixel2PixelTabCalculation(outData, combination, entete.noDataValue(), names, tabs);
		cal.run();
	}

	@Override
	protected void doClose() {
		
		CoverageManager.write(outputRaster, outData, entete); // export sur fichier
		
		combination = null;
		outputRaster = null;
		entete = null;
		names = null;
		for(int i=0; i<tabs.length; i++){
			tabs[i] = null;
		}
		tabs = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}

}
