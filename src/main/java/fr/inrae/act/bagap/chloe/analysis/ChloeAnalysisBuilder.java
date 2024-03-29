package fr.inrae.act.bagap.chloe.analysis;

import java.util.Map;
import java.util.Set;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.chloe.api.RasterTypeMime;
import fr.inrae.act.bagap.chloe.cluster.ClusterType;
import fr.inrae.act.bagap.chloe.distance.analysis.DistanceType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.chloe.window.output.DataOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public abstract class ChloeAnalysisBuilder {

	private ChloeAnalysisType analysisType;
	
	public ChloeAnalysisBuilder(){
		reset();
	}
	
	public abstract ChloeAnalysis build();
	
	public abstract void reset();
	
	public void setAnalysisType(ChloeAnalysisType analysisType){
		this.analysisType = analysisType;
	}
	
	public ChloeAnalysisType getAnalysisType() {
		return analysisType;
	}
	
	// setters
	
	public void addObserver(CountingObserver observer){
		throw new UnsupportedOperationException();
	}
	
	public void setObservers(Set<CountingObserver> observers){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowShapeType(WindowShapeType shapeType){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowDistanceType(WindowDistanceType distanceType){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowDistanceFunction(String function){
		throw new UnsupportedOperationException();
	}
	
	public void addRasterFile(String rasterFile){
		throw new UnsupportedOperationException();
	}
	
	public void setCoverage(Coverage coverage){
		throw new UnsupportedOperationException();
	}
	
	public void setCoverage2(Coverage coverage2){
		throw new UnsupportedOperationException();
	}
	
	public void setCoverage3(Coverage coverage3){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTile(String rasterTile){
		throw new UnsupportedOperationException();
	}

	public void setRasterFile(String rasterFile){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterFile2(String rasterFile2){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterFile3(String rasterFile3){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTab(float[] inputDatas){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTab2(float[] inputDatas2){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTab3(float[] inputDatas3){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTabs(float[]... dataTabs) {
		throw new UnsupportedOperationException();
	}
	
	public void setShapefile(String shapefile){
		throw new UnsupportedOperationException();
	}
	
	public void setEntete(EnteteRaster entete){
		throw new UnsupportedOperationException();
	}
	
	public void setEntityRasterFile(String entityRasterFile){
		throw new UnsupportedOperationException();
	}
	
	public void setEntityRasterTab(float[] entityRasterTab){
		throw new UnsupportedOperationException();
	}

	public void setValues(String sValues){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowSize(int windowSize){
		throw new UnsupportedOperationException();
	}

	public void setWindowRadius(double radius){
		throw new UnsupportedOperationException();
	}
	
	public void addWindowSize(int windowSize){
		throw new UnsupportedOperationException();
	}
	
	public void addWindowRadius(double radius){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowSizes(int[] windowSizes){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowRadius(double[] radius){
		throw new UnsupportedOperationException();
	}
	
	public void setDisplacement(int displacement){
		throw new UnsupportedOperationException();
	}
	
	public void setInterpolation(boolean interpolation){
		throw new UnsupportedOperationException();
	}

	public void setUnfilters(int[] unfilters){
		throw new UnsupportedOperationException();
	}
	
	public void setFilters(int[] filters){
		throw new UnsupportedOperationException();
	}
	
	public void addMetric(Metric metric){
		throw new UnsupportedOperationException();
	}
	
	public void addMetric(String metric){
		throw new UnsupportedOperationException();
	}
	
	public void setMetrics(Set<Metric> metrics){
		throw new UnsupportedOperationException();
	}
	
	public void addVariable(String variable){
		throw new UnsupportedOperationException();
	}
	
	public void addCsvOutput(String csv){
		throw new UnsupportedOperationException();
	}
	
	public void setCsvOutputFolder(String csvFolder){
		throw new UnsupportedOperationException();
	}
	
	public void addCoverageOutput(CoverageOutput coverageOutput){
		throw new UnsupportedOperationException();
	}
	
	public void setAsciiGridOutputFolder(String asciiGridFolder){
		throw new UnsupportedOperationException();
	}
	
	public void setGeoTiffOutputFolder(String geoTiffFolder){
		throw new UnsupportedOperationException();
	}
	
	public void addAsciiGridOutput(String ascii){
		throw new UnsupportedOperationException();
	}
	
	public void addAsciiGridOutput(String metric, String ascii){
		throw new UnsupportedOperationException();
	}

	public void addAsciiGridOutput(int size, String metric, String ascii){
		throw new UnsupportedOperationException();
	}
	
	public void addTabOutput(float[] tab){
		throw new UnsupportedOperationException();
	}

	public void addTabOutput(String metric, float[] tab){
		throw new UnsupportedOperationException();
	}
	
	public void addTabOutput(int size, String metric, float[] tab){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(String metric, String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(int size, String metric, String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addDataOutput(String metric, Map<RefPoint, Float> datas){
		throw new UnsupportedOperationException();
	}
	
	public void addDataOutput(DataOutput dout){
		throw new UnsupportedOperationException();
	}
	
	public void addTileAsciiGridOutput(String metric, String pathTile, Tile tile){
		throw new UnsupportedOperationException();
	}
	
	public void addTileGeoTiffOutput(String metric, String pathTile, Tile tile){
		throw new UnsupportedOperationException();
	}
	
	public void setPointsFilter(String points){
		throw new UnsupportedOperationException();
	}
	
	public void setPointsFilter(Set<RefPoint> points){
		throw new UnsupportedOperationException();
	}
	
	public void setPixelsFilter(String pixels){
		throw new UnsupportedOperationException();
	}
	
	public void setPixelsFilter(Set<? extends Pixel> pixels){
		throw new UnsupportedOperationException();
	}

	public void setWindowsPath(String windowsPath){
		throw new UnsupportedOperationException();
	}
	
	public void setBufferSize(int buffSize){
		throw new UnsupportedOperationException();
	}
	 
	public void setROIX(int roiX){
		throw new UnsupportedOperationException();
	}
	
	public void setROIY(int roiY){
		throw new UnsupportedOperationException();
	}
	
	public void setROIWidth(int roiWidth){
		throw new UnsupportedOperationException();
	}
	
	public void setROIHeight(int roiHeight){
		throw new UnsupportedOperationException();
	}
	
	public void setMinRate(double min){
		throw new UnsupportedOperationException();
	}
	
	public void setNamesAndRasters(Map<String, String> factors){
		throw new UnsupportedOperationException();
	}
	
	public void setCombination(String combination){
		throw new UnsupportedOperationException();
	}
	
	public void setChanges(Map<Float, Float> changes) {
		throw new UnsupportedOperationException();
	}

	public void setNoDataValue(int noDataValue) {
		throw new UnsupportedOperationException();
	}
	
	public void setFillValue(float fillValue) {
		throw new UnsupportedOperationException();
	}
	
	public void setDomains(Map<Domain<Float, Float>, Integer> domains) {
		throw new UnsupportedOperationException();
	}

	public void setCsvFile(String prop) {
		throw new UnsupportedOperationException();
	}
	
	public void setCellSize(float parseFloat) {
		throw new UnsupportedOperationException();
	}
	
	public void setWidth(int parseInt) {
		throw new UnsupportedOperationException();
	}
	
	public void setHeight(int parseInt) {
		throw new UnsupportedOperationException();
	}
	
	public void setXMin(double xMin) {
		throw new UnsupportedOperationException();
	}
	
	public void setXMax(double xMax) {
		throw new UnsupportedOperationException();
	}
	
	public void setYMin(double yMin) {
		throw new UnsupportedOperationException();
	}
	
	public void setYMax(double yMax) {
		throw new UnsupportedOperationException();
	}
	
	public void setOutputPrefix(String prefix) {
		throw new UnsupportedOperationException();
	}
	
	public void setOutputFolder(String folder) {
		throw new UnsupportedOperationException();
	}
	
	public void setOutputSuffix(String suffix) {
		throw new UnsupportedOperationException();
	}
	
	public void setTypeMime(String typeMime) {
		throw new UnsupportedOperationException();
	}
	
	public void setTypeMime(RasterTypeMime typeMime) {
		throw new UnsupportedOperationException();
	}

	public void setAttribute(String attribute) {
		throw new UnsupportedOperationException();
	}
	
	public void addSource(int source) {
		throw new UnsupportedOperationException();
	}
	
	public void setDistanceType(DistanceType distanceType) {
		throw new UnsupportedOperationException();
	}
	
	public void setMaxDistance(float maxDistance) {
		throw new UnsupportedOperationException();
	}
	
	public void setClusterType(ClusterType clusterType) {
		throw new UnsupportedOperationException();
	}
	
	// getters
	
	public WindowShapeType getWindowShapeType() {
		throw new UnsupportedOperationException();
	}
	
	public int getWindowSize(){
		throw new UnsupportedOperationException();
	}
	
	public int getDisplacement() {
		throw new UnsupportedOperationException();
	}

	public String getRasterFile() {
		throw new UnsupportedOperationException();
	}
	
	public RasterTypeMime getTypeMime(){
		throw new UnsupportedOperationException();
	}

}
