package fr.inrae.act.bagap.chloe.util.analysis;

import java.util.Map;

import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ClassificationAnalysis extends ChloeUtilAnalysis {

	private String inputRaster;
	
	private String outputRaster;
	
	private float[] inData;
	
	private float[] outData;
	
	private EnteteRaster entete;
	
	private Map<Domain<Float, Float>, Integer> domains;
	
	public ClassificationAnalysis(String outputRaster, String inputRaster, Map<Domain<Float, Float>, Integer> domains){
		this.outputRaster = outputRaster;
		this.inputRaster = inputRaster;
		this.domains = domains;
	}
	
	@Override
	protected void doInit() {
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		entete = cov.getEntete();
		inData = cov.getData();
		cov.dispose();
		
		outData = new float[entete.width()*entete.height()];
	}

	@Override
	protected void doRun() {

		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(outData, inData, entete.noDataValue(), domains);
		cal.run();
	}

	@Override
	protected void doClose() {

		CoverageManager.write(outputRaster, outData, entete); // export sur fichier

		inputRaster = null;
		outputRaster = null;
		entete = null;
		domains.clear();
		domains = null;
		inData = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}

}
