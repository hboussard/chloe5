package fr.inrae.act.bagap.chloe.util.analysis;

import java.util.Set;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.api.RasterTypeMime;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class MultipleRasterFromCsvAnalysis extends ChloeUtilAnalysis {
	
	private String csvFile;
	
	private Set<String> variables;
	
	private float[][] outDatas;
	
	private String outputFolder;
	
	private String outputPrefix, outputSuffix;
	
	private  int ncols, nrows, noDataValue;
	
	private float cellSize;
	
	private double xMin, yMin;
	
	private EnteteRaster entete;
	
	private RasterTypeMime typeMime;

	public MultipleRasterFromCsvAnalysis(String outputFolder, String outputPrefix, String outputSuffix, RasterTypeMime typeMime,
			String csvFile, Set<String> variables, int ncols, int nrows, double xMin, double yMin, float cellSize, int noDataValue){
		this.csvFile = csvFile;
		this.variables = variables;
		this.outputFolder = outputFolder;
		this.outputPrefix = outputPrefix;
		this.outputSuffix = outputSuffix;
		this.typeMime = typeMime;
		this.ncols = ncols;
		this.nrows = nrows;
		this.xMin = xMin;
		this.yMin = yMin;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	@Override
	protected void doInit() {
		
		double xMax = xMin + ncols*cellSize;
		double yMax = yMin + nrows*cellSize;
		entete = new EnteteRaster(ncols, nrows, xMin, xMax, yMin, yMax, cellSize, noDataValue);
		
		outDatas = new float[variables.size()][entete.width()*entete.height()];
		
		outputPrefix = outputPrefix.endsWith("_")||outputPrefix.equalsIgnoreCase("")?outputPrefix:outputPrefix+"_";
		outputSuffix = outputSuffix.startsWith("_")||outputSuffix.equalsIgnoreCase("")?outputSuffix:"_"+outputSuffix;
	}

	@Override
	protected void doRun() {
		
		SpatialCsvManager.exportTabs(outDatas, csvFile, variables.toArray(new String[variables.size()]), entete);
		
	}

	@Override
	protected void doClose() {
		
		int var = 0;
		for(String variable : variables){
			if(typeMime == RasterTypeMime.ASCII_GRID){
				CoverageManager.write(outputFolder+outputPrefix+variable+outputSuffix+".asc", outDatas[var++], entete);
			}else{
				CoverageManager.write(outputFolder+outputPrefix+variable+outputSuffix+".tif", outDatas[var++], entete);
			}
		}
		
		csvFile = null;
		variables = null;
		outputFolder = null;
		entete = null;
		outDatas = null; // a voir, peut-etre a mettre dans setResult()
	}
	

}
