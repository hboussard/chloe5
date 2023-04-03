package fr.inrae.act.bagap.chloe.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inrae.act.bagap.chloe.WindowDistanceType;
import fr.inrae.act.bagap.chloe.WindowShapeType;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.DataOutput;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class LandscapeMetricAnalysisBuilder {

	private WindowAnalysisType analysisType;
	
	private WindowShapeType shapeType;
	
	private WindowDistanceType distanceType;
	
	private String distanceFunction;
	
	private String rasterFile, rasterFile2, areaRasterFile;
	
	private float[] rasterTab, rasterTab2, areaRasterTab;
	
	private EnteteRaster entete;
	
	private int windowSize, displacement, gridSize;
	
	private double windowRadius;
	
	private Set<Metric> metrics;
	
	private Set<CountingObserver> observers;

	private String csv, points, pixels, exportWindowPath, asciiGridFolder;
	
	private Set<RefPoint> refPoints;
	
	private Set<? extends Pixel> refPixels;
	
	private Map<String, String> asciiOutputs;
	
	private Map<String, String> geotiffOutputs;
	
	private Map<String, float[]> tabOutputs;
	
	private boolean interpolation;
	
	private int roiX, roiY, roiWidth, roiHeight;
	
	//private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private int[] values, unfilters;
	
	private Map<RefPoint, Float> datas;
	
	public LandscapeMetricAnalysisBuilder(){
		this.metrics = new HashSet<Metric>();
		this.observers = new HashSet<CountingObserver>();
		reset();
	}
	
	public void reset(){
		this.analysisType = WindowAnalysisType.SLIDING;
		this.shapeType = WindowShapeType.CIRCLE;
		this.distanceType = WindowDistanceType.THRESHOLD;
		this.distanceFunction = "exp(-pow(distance, 2)/pow(dmax/2, 2))";
		this.metrics.clear();
		this.observers.clear();
		this.displacement = 1;
		this.interpolation = false;
		this.windowSize = -1;
		this.gridSize = -1;
		this.windowRadius = -1;
		this.roiX = 0;
		this.roiY = 0;
		this.roiWidth = -1;
		this.roiHeight = -1;
		//this.bufferROIXMin = 0;
		//this.bufferROIXMax = 0;
		//this.bufferROIYMin = 0;
		//this.bufferROIYMax = 0;
		this.rasterFile = null;
		this.rasterFile2 = null;
		this.rasterTab = null;
		this.rasterTab2 = null;
		this.entete = null;
		this.areaRasterFile = null;
		this.areaRasterTab = null;
		this.csv = null;
		this.asciiGridFolder = null;
		this.points = null;
		this.pixels = null;
		this.refPoints = null;
		this.refPixels = null;
		this.exportWindowPath = null;
		this.values = null;
		this.asciiOutputs = new HashMap<String, String>();
		this.geotiffOutputs = new HashMap<String, String>();
		this.tabOutputs = new HashMap<String, float[]>();
		this.unfilters = null;
		this.datas = null;
	}

	public void setAnalysisType(WindowAnalysisType analysisType) {
		this.analysisType = analysisType;
	}
	
	public void setWindowShapeType(WindowShapeType shapeType) {
		this.shapeType = shapeType;
	}
	
	public void setWindowDistanceType(WindowDistanceType distanceType) {
		this.distanceType = distanceType;
	}
	
	public void setWindowDistanceFunction(String function) {
		this.distanceFunction = function;
	}

	public void setRasterFile(String rasterFile) {
		this.rasterFile = rasterFile;
	}
	
	public void setRasterFile2(String rasterFile2) {
		this.rasterFile2 = rasterFile2;
	}
	
	public void setRasterTab(float[] inputDatas) {
		this.rasterTab = inputDatas;
	}
	
	public void setRasterTab2(float[] inputDatas) {
		this.rasterTab2 = inputDatas;
	}
	
	public void setEntete(EnteteRaster entete){
		this.entete = entete;
	}
	
	public void setAreaRasterFile(String areaRasterFile) {
		this.areaRasterFile = areaRasterFile;
	}
	
	public void setAreaRasterTab(float[] areaRasterTab) {
		this.areaRasterTab = areaRasterTab;
	}

	public void setValues(String sValues){
		String[] s = sValues.split(",");
		values = new int[s.length];
		for(int i = 0; i<s.length; i++){
			values[i] = Integer.parseInt(s[i].replace(" ", ""));
		}
	}
	
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}
	
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public void setWindowRadius(double radius){
		this.windowRadius = radius;
	}
	
	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}
	
	public void setInterpolation(boolean interpolation){
		this.interpolation = interpolation;
	}

	public void setUnfilters(int[] unfilters){
		this.unfilters = unfilters;
	}
	
	public void addMetric(Metric metric){
		this.metrics.add(metric);
	}
	
	public void addMetric(String metric){
		if(MetricManager.hasMetric(metric)){
			this.metrics.add(MetricManager.get(metric));
		}else{
			System.out.println("metric '"+metric+"' does not exist");
		}
	}
	
	public void setMetrics(Set<Metric> metrics) {
		this.metrics = metrics;
	}

	public void addCsvOutput(String csv){
		this.csv = csv;
	}
	
	public void addAsciiGridFolderOutput(String asciiGridFolder){
		this.asciiGridFolder = asciiGridFolder;
	}
	
	public void addAsciiGridOutput(String metric, String ascii){
		//addMetric(metric);
		this.asciiOutputs.put(metric, ascii);
	}
	
	public void addTabOutput(String metric, float[] tab){
		this.tabOutputs.put(metric, tab);
	}
	
	public void addGeoTiffOutput(String metric, String geotiff){
		this.geotiffOutputs.put(metric, geotiff);
	}
	
	public void addDataOutput(String metric, Map<RefPoint, Float> datas){
		this.datas = datas;
	}
	
	public void addDataOutput(DataOutput dout){
		observers.add(dout);
	}
	
	public void setPointsFilter(String points) {
		this.points = points;
	}
	
	public void setPointsFilter(Set<RefPoint> points){
		this.refPoints = points;
	}
	
	public void setPixelsFilter(String pixels) {
		this.pixels = pixels;
	}
	
	public void setPixelsFilter(Set<? extends Pixel> pixels) {
		this.refPixels = pixels;
	}

	public void addAscExportWindowOutput(String windowPath) {
		this.exportWindowPath = windowPath;
	}
	
	public void addObserver(CountingObserver observer){
		this.observers.add(observer);
	}
	
	public void setObservers(Set<CountingObserver> observers) {
		this.observers = observers;
	}
	
	/*
	public void setBufferROI(int bufferROI){
		this.bufferROIXMin = bufferROI;
		this.bufferROIXMax = bufferROI;
		this.bufferROIYMin = bufferROI;
		this.bufferROIYMax = bufferROI;
	}
	
	public void setBufferROIXMin(int bufferROI){
		this.bufferROIXMin = bufferROI;
	}
	
	public void setBufferROIXMax(int bufferROI){
		this.bufferROIXMax = bufferROI;
	}
	
	public void setBufferROIYMin(int bufferROI){
		this.bufferROIYMin = bufferROI;
	}
	
	public void setBufferROIYMax(int bufferROI){
		this.bufferROIYMax = bufferROI;
	}
	*/
	
	public void setBufferSize(int buffSize){
		LandscapeMetricAnalysis.setBufferSize(buffSize);
	}
	 
	public void setROIX(int roiX){
		this.roiX = roiX;
	}
	
	public void setROIY(int roiY){
		this.roiY = roiY;
	}
	
	public void setROIWidth(int roiWidth){
		this.roiWidth = roiWidth;
	}
	
	public void setROIHeight(int roiHeight){
		this.roiHeight = roiHeight;
	}
	
	public WindowAnalysisType getAnalysisType() {
		return analysisType;
	}
	
	public WindowShapeType getWindowShapeType() {
		return shapeType;
	}
	
	public WindowDistanceType getWindowDistanceType() {
		return distanceType;
	}
	
	public String getWindowDistanceFunction() {
		return distanceFunction;
	}

	public String getRasterFile() {
		return rasterFile;
	}
	
	public String getRasterFile2() {
		return rasterFile2;
	}
	
	public float[] getRasterTab() {
		return rasterTab;
	}
	
	public float[] getRasterTab2() {
		return rasterTab2;
	}
	
	public EnteteRaster getEntete() {
		return entete;
	}
	
	public String getAreaRasterFile() {
		return areaRasterFile;
	}
	
	public float[] getAreaRasterTab() {
		return areaRasterTab;
	}
	
	public int[] getValues(){
		return values;
	}

	public int getWindowSize() {
		return windowSize;
	}
	
	public int getGridSize() {
		return gridSize;
	}

	public double getWindowRadius() {
		return windowRadius;
	}

	public int getDisplacement() {
		return displacement;
	}
	
	public boolean getInterpolation(){
		return interpolation;
	}

	public Set<Metric> getMetrics() {
		return metrics;
	}

	public Set<CountingObserver> getObservers() {
		return observers;
	}

	public String getCsv() {
		return csv;
	}
	
	public String getAsciiGridFolder() {
		return asciiGridFolder;
	}
	
	public Map<String, String> getAsciiOutputs(){
		return asciiOutputs;
	}
	
	public Map<String, String> getGeoTiffOutputs(){
		return geotiffOutputs;
	}
	
	public Map<String, float[]> getTabOutputs(){
		return tabOutputs;
	}
	
	public Map<RefPoint, Float> getDatas(){
		return datas;
	}
	
	/*
	public int getBufferROIXMin(){
		return bufferROIXMin;
	}
	
	public int getBufferROIXMax(){
		return bufferROIXMax;
	}
	
	public int getBufferROIYMin(){
		return bufferROIYMin;
	}
	
	public int getBufferROIYMax(){
		return bufferROIYMax;
	}
	*/
	
	public int getROIX(){
		return this.roiX;
	}
	
	public int getROIY(){
		return this.roiY;
	}
	
	public int getROIWidth(){
		return this.roiWidth;
	}
	
	public int getROIHeight(){
		return this.roiHeight;
	}

	public int[] getUnfilters(){
		return unfilters;
	}
	
	public String getPixelsFilter(){
		return pixels;
	}
	
	public String getPointsFilter(){
		return points;
	}
	
	public Set<RefPoint> getRefPoints(){
		return refPoints;
	}
	
	public Set<Pixel> getRefPixels(){
		return (Set<Pixel>) refPixels;
	}
	
	public LandscapeMetricAnalysis build(){
		LandscapeMetricAnalysis analysis = null;
		try{
			analysis = LandscapeMetricAnalysisFactory.create(this);
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			//reset();
		}
		return analysis;
	}
	
}
