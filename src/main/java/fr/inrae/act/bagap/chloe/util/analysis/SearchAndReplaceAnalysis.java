package fr.inrae.act.bagap.chloe.util.analysis;

import java.util.Map;

import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class SearchAndReplaceAnalysis extends ChloeUtilAnalysis {

	private String inputRaster;
	
	private String outputRaster;
	
	private Map<Float, Float> sarMap;
	
	private int noDataValue;
	
	private float[] inData;
	
	private float[] outData;
	
	private EnteteRaster entete;
	
	public SearchAndReplaceAnalysis(String outputRaster, String inputRaster, int noDataValue, Map<Float, Float> sarMap){
		this.outputRaster = outputRaster;
		this.inputRaster = inputRaster;
		this.noDataValue = noDataValue;
		this.sarMap = sarMap;
	}
	
	@Override
	protected void doInit() {
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		entete = cov.getEntete();
		inData = cov.getData();
		cov.dispose();

		entete.setNoDataValue(noDataValue); // changement du noDataValue
		
		outData = new float[entete.width()*entete.height()];
	}

	@Override
	protected void doRun() {

		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(outData, inData, sarMap);
		cal.run();
	}

	@Override
	protected void doClose() {

		CoverageManager.write(outputRaster, outData, entete); // export sur fichier

		inputRaster = null;
		outputRaster = null;
		entete = null;
		sarMap.clear();
		sarMap = null;
		inData = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}

}
