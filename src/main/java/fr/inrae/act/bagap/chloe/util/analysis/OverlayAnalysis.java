package fr.inrae.act.bagap.chloe.util.analysis;

import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.tab.OverlayPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class OverlayAnalysis extends ChloeUtilAnalysis {

	private String outputRaster;
	
	private Set<String> inputRasters;
	
	private float[][] inDatas;
	
	private float[] outData;
	
	private EnteteRaster entete;
	
	public OverlayAnalysis(String outputRaster, Set<String> inputRasters) {
		this.outputRaster = outputRaster;
		this.inputRasters = inputRasters;
	}
	
	@Override
	protected void doInit() {
		
		inDatas = new float[inputRasters.size()][];
		
		Coverage cov;
		int index = 0;
		for(String inputRaster: inputRasters) {
			cov = CoverageManager.getCoverage(inputRaster);
			entete = cov.getEntete();
			inDatas[index++] = cov.getData();
			cov.dispose();
		}
		
		outData = new float[entete.width()*entete.height()];
	}

	@Override
	protected void doRun() {

		OverlayPixel2PixelTabCalculation cal = new OverlayPixel2PixelTabCalculation(outData, entete.noDataValue(), inDatas);
		cal.run();
	}

	@Override
	protected void doClose() {
		
		CoverageManager.write(outputRaster, outData, entete); // export sur fichier

		inputRasters.clear();
		inputRasters = null;
		outputRaster = null;
		entete = null;
		inDatas = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}
	

}
