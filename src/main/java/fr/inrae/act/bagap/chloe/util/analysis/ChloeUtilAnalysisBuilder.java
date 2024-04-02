package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.api.RasterTypeMime;
import fr.inrae.act.bagap.chloe.cluster.ClusterType;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceType;
import fr.inrae.act.bagap.chloe.util.Util;

public class ChloeUtilAnalysisBuilder extends ChloeAnalysisBuilder {
	
	private Map<String, String> factors;
	
	private String outputRaster;
	
	private String outputCsv;
	
	private String outputFolder;
	
	private String inputRaster, inputRaster2;
	
	private String inputShapefile;
	
	private String outputPrefix, outputSuffix;
	
	private String csv;
	
	private String attribute;
	
	private String combination;
	
	private Map<Float, Float> changes;
	
	private float cellSize, fillValue;
	
	private int width, height, noDataValue;
	
	private CoordinateReferenceSystem crs;
	
	private double xMin, xMax, yMin, yMax;
	
	private Map<Domain<Float, Float>, Integer> domains;
	
	private Set<String> variables;
	
	private RasterTypeMime typeMime;
	
	private DistanceType distanceType;
	
	private ClusterType clusterType;
	
	private float maxDistance;
	
	private Set<Integer> sources;
	
	@Override
	public void reset() {
		this.factors = null;
		this.outputRaster = null;
		this.outputCsv = null;
		this.outputFolder = null;
		this.inputRaster = null;
		this.inputRaster2 = null;
		this.inputShapefile = null;
		this.attribute = null;
		this.outputPrefix = "";
		this.outputSuffix = "";
		this.typeMime = RasterTypeMime.GEOTIFF;
		this.csv = null;
		this.combination = null;
		this.changes = null;
		this.cellSize = -1;
		this.width = -1;
		this.height = -1;
		this.xMin = 0;
		this.xMax = 0;
		this.yMin = 0;
		this.yMax = 0;
		this.noDataValue = Raster.getNoDataValue();
		this.fillValue = Raster.getNoDataValue();
		this.domains = null;
		this.variables = new HashSet<String>();
		this.distanceType = DistanceType.EUCLIDIAN;
		this.clusterType = ClusterType.QUEEN;
		this.maxDistance = -1;
		this.sources = new HashSet<Integer>();
		try {
			this.crs = CRS.decode("EPSG:2154");
		} catch (FactoryException e) {
			e.printStackTrace();
		}
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
	public void addCsvOutput(String outputCsv){
		this.outputCsv = outputCsv;
	}
	
	@Override
	public void addRasterFile(String rasterFile){
		this.inputRaster = rasterFile;
	}
	
	@Override
	public void setRasterFile(String rasterFile){
		this.inputRaster = rasterFile;
	}

	public void setRasterFile2(String rasterFile2){
		this.inputRaster2 = rasterFile2;
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
	public void setFillValue(float fillValue) {
		this.fillValue = fillValue;
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
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void setXMin(double xMin) {
		this.xMin = xMin;
	}
	
	@Override
	public void setXMax(double xMax) {
		this.xMax = xMax;
	}
	
	@Override
	public void setYMin(double yMin) {
		this.yMin = yMin;
	}
	
	@Override
	public void setYMax(double yMax) {
		this.yMax = yMax;
	}
	
	@Override
	public void setCRS(CoordinateReferenceSystem crs) {
		this.crs = crs;
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
	
	@Override
	public void setShapefile(String shapefile){
		this.inputShapefile = shapefile;
	}
	
	@Override
	public void setAttribute(String attribute){
		this.attribute = attribute;
	}
	
	@Override
	public void addSource(int source) {
		this.sources.add(source);
	}
	
	@Override
	public void setDistanceType(DistanceType distanceType) {
		this.distanceType = distanceType;
	}
	
	@Override
	public void setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	@Override
	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}
	
	// getters
	
	public Set<Integer> getSources(){
		return sources;
	}
	
	public DistanceType getDistanceType(){
		return distanceType;
	}
	
	public ClusterType getClusterType(){
		return clusterType;
	}
	
	public float getMaxDistance(){
		return maxDistance;
	}
	
	public Map<String, String> getNamesAndRasters(){
		return factors;
	}
	
	public String getOutputRaster(){
		return outputRaster;
	}
	
	public String getOutputCsv(){
		return outputCsv;
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
	
	public String getRasterFile2() {
		return inputRaster2;
	}
	
	public String getShapefile(){
		return inputShapefile;
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
	
	public float getFillValue(){
		return fillValue;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public double getXMin(){
		return xMin;
	}
	
	public double getXMax(){
		return xMax;
	}

	public double getYMin(){
		return yMin;
	}
	
	public double getYMax(){
		return yMax;
	}
	
	public String getAttribute(){
		return attribute;
	}
	
	public float getCellSize(){
		return cellSize;
	}
	
	public Map<Domain<Float, Float>, Integer> getDomains(){
		return domains;
	}
	
	@Override
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
