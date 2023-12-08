package fr.inrae.act.bagap.chloe.distance.analysis;

import java.util.Set;

import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.analysis.ChloeUtilAnalysis;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class DistanceAnalysis extends ChloeUtilAnalysis {

	private String outputRaster;
	
	private String inputRaster;
	
	private String frictionRaster;
	
	private DistanceType distanceType;
	
	private int[] distanceSources;
	
	private double threshold;
	
	private float[] inData;
	
	private float[] frictionData;
	
	private float[] outData;
	
	private EnteteRaster entete;
	
	public DistanceAnalysis(String outputRaster, String inputRaster, String frictionRaster, DistanceType distanceType, Set<Integer> distanceSources, double threshold){
		this.outputRaster = outputRaster;
		this.inputRaster = inputRaster;
		this.frictionRaster = frictionRaster;
		this.distanceType = distanceType;
		this.distanceSources = new int[distanceSources.size()];
		int index = 0;
		for(Integer s : distanceSources){
			this.distanceSources[index++] = s;
		}
		this.threshold = threshold;
	}
	
	@Override
	protected void doInit() {
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		entete = cov.getEntete();
		inData = cov.getData();
		cov.dispose();
		
		if(distanceType == DistanceType.FUNCTIONAL){
			Coverage covF = CoverageManager.getCoverage(frictionRaster);
			frictionData = covF.getData();
			covF.dispose();
		}
		
		outData = new float[entete.width()*entete.height()];
	}

	@Override
	protected void doRun() {
		
		if(distanceType == DistanceType.EUCLIDIAN){
			
			TabChamferDistanceAnalysis analysis = new TabChamferDistanceAnalysis(outData, inData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), distanceSources, threshold);
			analysis.allRun();
			
		}else if(distanceType == DistanceType.FUNCTIONAL){
			
			TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(outData, inData, frictionData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), distanceSources, threshold);
			analysis.allRun();
			
		}
		
	}

	@Override
	protected void doClose() {
		CoverageManager.write(outputRaster, outData, entete); // export sur fichier

		inputRaster = null;
		frictionRaster = null;
		outputRaster = null;
		entete = null;
		distanceSources = null;
		inData = null;
		frictionData = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}

}
