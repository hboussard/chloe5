package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.api.RasterTypeMime;
import fr.inrae.act.bagap.chloe.util.Util;

public class ChloeUtilAnalysisBuilder extends ChloeAnalysisBuilder {
	
	private Map<String, String> factors;
	
	private String outputRaster;
	
	private String outputFolder;
	
	private String inputRaster;
	
	private String outputPrefix, outputSuffix;
	
	private String csv;
	
	private String combination;
	
	private Map<Float, Float> changes;
	
	private float cellSize;
	
	private int ncols, nrows, noDataValue;
	
	private double xMin, yMin;
	
	private Map<Domain<Float, Float>, Integer> domains;
	
	private Set<String> variables;
	
	private RasterTypeMime typeMime;
	
	@Override
	public void reset() {
		factors = null;
		outputRaster = null;
		outputFolder = null;
		inputRaster = null;
		outputPrefix = "";
		outputSuffix = "";
		typeMime = RasterTypeMime.GEOTIFF;
		csv = null;
		combination = null;
		changes = null;
		cellSize = -1;
		ncols = -1;
		nrows = -1;
		xMin = 0;
		yMin = 0;
		noDataValue = Raster.getNoDataValue();
		domains = null;
		variables = new HashSet<String>();
	}
	
	@Override
	public void setNamesAndRasters(Map<String, String> factors){
		this.factors = factors;
	}
	
	@Override
	public void addAsciiGridOutput(String ascii){
		addRasterOutput(ascii);
	}
	
	@Override
	public void addGeoTiffOutput(String geotiff){
		addRasterOutput(geotiff);
	}
	
	private void addRasterOutput(String outputRaster){
		this.outputRaster = outputRaster;
	}
	
	@Override
	public void setRasterFile(String rasterFile){
		this.inputRaster = rasterFile;
	}
	
	@Override
	public void setCombination(String combination){
		this.combination = combination;
	}
	
	@Override
	public void setChanges(Map<Float, Float> changes) {
		this.changes = changes;
	}
	
	@Override
	public void setNoDataValue(int noDataValue) {
		this.noDataValue = noDataValue;
	}
	
	@Override
	public void setDomains(Map<Domain<Float, Float>, Integer> domains) {
		this.domains= domains;
	}
	
	@Override
	public void setCsvFile(String csvFile) {
		this.csv = csvFile;
	}
	
	@Override
	public void addVariable(String variable){
		this.variables.add(variable);
	}
	
	@Override
	public void setCellSize(float cellSize) {
		this.cellSize = cellSize;
	}
	
	@Override
	public void setNCols(int ncols) {
		this.ncols = ncols;
	}
	
	@Override
	public void setNRows(int nrows) {
		this.nrows = nrows;
	}
	
	@Override
	public void setXMin(double xMin) {
		this.xMin = xMin;
	}
	
	@Override
	public void setYMin(double yMin) {
		this.yMin = yMin;
	}
	
	@Override
	public void setOutputPrefix(String outputPrefix) {
		this.outputPrefix = outputPrefix;
	}
	
	@Override
	public void setOutputFolder(String folder) {
		Util.createAccess(folder);
		this.outputFolder = new File(folder).getAbsolutePath()+"/";
	}
	
	@Override
	public void setOutputSuffix(String outputSuffix) {
		this.outputSuffix = outputSuffix;
	}
	
	@Override
	public void setTypeMime(String typeMime) {
		this.typeMime = RasterTypeMime.valueOf(typeMime);
	}
	
	@Override
	public void setTypeMime(RasterTypeMime typeMime) {
		this.typeMime = typeMime;
	}
	
	// getters
	
	public Map<String, String> getNamesAndRasters(){
		return factors;
	}
	
	public String getOutputRaster(){
		return outputRaster;
	}
	
	public String getOutputPrefix(){
		return outputPrefix;
	}
	
	public String getOutputFolder(){
		return outputFolder;
	}
	
	public String getOutputSuffix(){
		return outputSuffix;
	}
	
	@Override
	public String getRasterFile() {
		return inputRaster;
	}
	
	public String getCsvFile(){
		return csv;
	}
	
	public Set<String> getVariables(){
		return variables;
	}
	
	public String getCombination(){
		return combination;
	}
	
	public Map<Float, Float> getChanges(){
		return changes;
	}
	
	public int getNoDataValue(){
		return noDataValue;
	}
	
	public int getNCols(){
		return ncols;
	}
	
	public int getNRows(){
		return nrows;
	}
	
	public double getXMin(){
		return xMin;
	}

	public double getYMin(){
		return yMin;
	}
	
	public float getCellSize(){
		return cellSize;
	}
	
	public Map<Domain<Float, Float>, Integer> getDomains(){
		return domains;
	}
	
	public RasterTypeMime getTypeMime(){
		return this.typeMime;
	}
	
	@Override
	public ChloeAnalysis build() {
		ChloeUtilAnalysis analysis = null;
		try{
			
			analysis = ChloeUtilAnalysisFactory.create(this);

		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			reset();
		}
		return analysis;
	}


}
