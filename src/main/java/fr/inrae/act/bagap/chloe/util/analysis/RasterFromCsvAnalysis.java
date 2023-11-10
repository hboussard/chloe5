package fr.inrae.act.bagap.chloe.util.analysis;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class RasterFromCsvAnalysis extends ChloeUtilAnalysis {
	
	private String csvFile;
	
	private String variable;
	
	private float[] outData;
	
	private String outputRaster;
	
	private  int width, height, noDataValue;
	
	private float cellSize;
	
	private double xMin, yMin;
	
	private EnteteRaster entete;

	public RasterFromCsvAnalysis(String outputRaster, String csvFile, String variable, int width, int height, double xMin, double yMin, float cellSize, int noDataValue){
		this.csvFile = csvFile;
		this.variable = variable;
		this.outputRaster = outputRaster;
		this.width = width;
		this.height = height;
		this.xMin = xMin;
		this.yMin = yMin;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	@Override
	protected void doInit() {
		
		double xMax = xMin + width*cellSize;
		double yMax = yMin + height*cellSize;
		entete = new EnteteRaster(width, height, xMin, xMax, yMin, yMax, cellSize, noDataValue);
		
		outData = new float[entete.width()*entete.height()];
		
	}

	@Override
	protected void doRun() {
		
		SpatialCsvManager.exportTab(outData, csvFile, variable, entete);
		
	}

	@Override
	protected void doClose() {
		
		CoverageManager.write(outputRaster, outData, entete);
		
		csvFile = null;
		variable = null;
		outputRaster = null;
		entete = null;
		outData = null; // a voir, peut-etre a mettre dans setResult()
	}
	

}
