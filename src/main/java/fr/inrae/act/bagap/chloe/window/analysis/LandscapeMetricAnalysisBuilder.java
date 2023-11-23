package fr.inrae.act.bagap.chloe.window.analysis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
//import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.map.MultipleMapLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.selected.MultipleSelectedLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.sliding.MultipleSlidingLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
//import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
//import fr.inrae.act.bagap.chloe.window.output.DataOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class LandscapeMetricAnalysisBuilder extends ChloeAnalysisBuilder /*implements Cloneable*/ {
	
	private WindowShapeType shapeType;
	
	private WindowDistanceType distanceType;
	
	private String distanceFunction;
	
	private Coverage coverage, coverage2;
	
	private Set<String> rasterFiles;
	
	private String rasterFile, rasterFile2, entityRasterFile, rasterTile;
	
	private float[] rasterTab, rasterTab2, entityRasterTab;
	
	private EnteteRaster entete;
	
	private int displacement, windowSize;
	
	private double radius;
	
	private Set<Integer> windowSizes;
	
	private Set<Double> windowRadius;
	
	private double dMax;
	
	private Set<Metric> metrics;
	
	//private Set<CountingObserver> observers;

	private String csv, points, pixels, windowsPath, asciiGridFolder, geoTiffFolder;
	
	private Set<RefPoint> refPoints;
	
	private Set<? extends Pixel> refPixels;
	
	private Map<Integer, Map<String, String>> asciiOutputs;
	
	private Map<Integer, Map<String, String>> geotiffOutputs;
	
	private Map<Integer, Map<String, float[]>> tabOutputs;
	
	private Map<Tile, Map<String, String>> tileAsciiOutputs;
	
	private Map<Tile, Map<String, String>> tileGeoTiffOutputs;
	
	private boolean interpolation;
	
	private int roiX, roiY, roiWidth, roiHeight;
	
	//private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax;
	
	private int[] values, filters, unfilters;
	
	private Map<RefPoint, Float> datas;
	
	private Set<CoverageOutput> coverageOutputs;
	
	/*
	@Override
	public LandscapeMetricAnalysisBuilder clone() {
		LandscapeMetricAnalysisBuilder builder = null;
	    try {
	    	builder = (LandscapeMetricAnalysisBuilder) super.clone();
	    } catch(CloneNotSupportedException cnse) {
	        cnse.printStackTrace(System.err);
	    }
	    return builder;
	}
	*/
	
	@Override
	public void reset(){
		setAnalysisType(ChloeAnalysisType.SLIDING);
		this.shapeType = WindowShapeType.CIRCLE;
		this.distanceType = WindowDistanceType.THRESHOLD;
		this.distanceFunction = "exp(-pow(distance, 2)/pow(dmax/2, 2))";
		this.metrics = new HashSet<Metric>();
		//this.observers = new HashSet<CountingObserver>();
		this.displacement = 1;
		this.interpolation = false;
		this.windowSize = 0;
		this.radius = 0;
		this.dMax = -1;
		this.windowSizes = new TreeSet<Integer>();
		this.windowRadius = new TreeSet<Double>();
		this.roiX = 0;
		this.roiY = 0;
		this.roiWidth = -1;
		this.roiHeight = -1;
		//this.bufferROIXMin = 0;
		//this.bufferROIXMax = 0;
		//this.bufferROIYMin = 0;
		//this.bufferROIYMax = 0;
		this.rasterFiles = new HashSet<String>();
		this.coverage = null;
		this.coverage2 = null;
		this.rasterTile = null;
		this.rasterFile = null;
		this.rasterFile2 = null;
		this.rasterTab = null;
		this.rasterTab2 = null;
		this.entete = null;
		this.entityRasterFile = null;
		this.entityRasterTab = null;
		this.csv = null;
		this.asciiGridFolder = null;
		this.points = null;
		this.pixels = null;
		this.refPoints = null;
		this.refPixels = null;
		this.windowsPath = null;
		this.values = null;
		this.asciiOutputs = new TreeMap<Integer, Map<String, String>>();
		this.geotiffOutputs = new TreeMap<Integer, Map<String, String>>();
		this.tabOutputs = new TreeMap<Integer, Map<String, float[]>>();
		this.tileAsciiOutputs = null;
		this.tileGeoTiffOutputs = null;
		this.unfilters = null;
		this.filters = null;
		this.datas = null;
		this.coverageOutputs = new HashSet<CoverageOutput>();
	}
	
	@Override
	public void setWindowShapeType(WindowShapeType shapeType) {
		this.shapeType = shapeType;
	}
	
	@Override
	public void setWindowDistanceType(WindowDistanceType distanceType) {
		this.distanceType = distanceType;
	}
	
	@Override
	public void setWindowDistanceFunction(String function) {
		this.distanceFunction = function;
	}
	
	@Override
	public void addRasterFile(String rasterFile) {
		this.rasterFiles.add(rasterFile);
		setRasterFile(rasterFile);
	}
	
	@Override
	public void setCoverage(Coverage coverage){
		this.coverage = coverage;
	}
	
	@Override
	public void setCoverage2(Coverage coverage2){
		this.coverage2 = coverage2;
	}
	
	@Override
	public void setRasterTile(String rasterTile) {
		this.rasterTile = rasterTile;
	}

	@Override
	public void setRasterFile(String rasterFile) {
		this.rasterFile = rasterFile;
	}
	
	@Override
	public void setRasterFile2(String rasterFile2) {
		this.rasterFile2 = rasterFile2;
	}
	
	@Override
	public void setRasterTab(float[] inputDatas) {
		this.rasterTab = inputDatas;
	}
	
	@Override
	public void setRasterTab2(float[] inputDatas) {
		this.rasterTab2 = inputDatas;
	}
	
	@Override
	public void setEntete(EnteteRaster entete){
		this.entete = entete;
	}
	
	@Override
	public void setEntityRasterFile(String entityRasterFile) {
		this.entityRasterFile = entityRasterFile;
	}
	
	@Override
	public void setEntityRasterTab(float[] entityRasterTab) {
		this.entityRasterTab = entityRasterTab;
	}

	@Override
	public void setValues(String sValues){
		String[] s = sValues.split(",");
		values = new int[s.length];
		for(int i = 0; i<s.length; i++){
			values[i] = Integer.parseInt(s[i].replace(" ", ""));
		}
	}
	
	@Override
	public void setWindowSize(int windowSize) {
		addWindowSize(windowSize);
	}

	@Override
	public void setWindowRadius(double radius){
		addWindowRadius(radius);
	}
	
	@Override
	public void addWindowSize(int windowSize) {
		this.windowSizes.add(windowSize);
		this.windowSize = windowSize;
	}
	
	@Override
	public void addWindowRadius(double radius){
		this.windowRadius.add(radius);
		this.radius = radius;
	}
	
	@Override
	public void setWindowSizes(int[] windowSizes) {
		for(int ws : windowSizes){
			addWindowSize(ws);
		}
	}
	
	@Override
	public void setWindowRadius(double[] radius){
		for(double r : radius){
			addWindowRadius(r);	
		}
	}
	
	public void setDMax(double dMax) {
		this.dMax = dMax;
	}
	
	@Override
	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}
	
	@Override
	public void setInterpolation(boolean interpolation){
		this.interpolation = interpolation;
	}

	@Override
	public void setUnfilters(int[] unfilters){
		this.unfilters = unfilters;
	}
	
	@Override
	public void setFilters(int[] filters){
		this.filters = filters;
	}
	
	@Override
	public void addMetric(Metric metric){
		this.metrics.add(metric);
	}
	
	@Override
	public void addMetric(String metric){
		//System.out.println(metric);
		if(MetricManager.hasMetric(metric)){
			this.metrics.add(MetricManager.get(metric));
		}else{
			System.out.println("metric '"+metric+"' does not exist");
		}
	}
	
	@Override
	public void setMetrics(Set<Metric> metrics) {
		this.metrics = metrics;
	}

	@Override
	public void addCsvOutput(String csv){
		Util.createAccess(csv);
		this.csv = csv;
	}
	
	@Override
	public void addCoverageOutput(CoverageOutput coverageOutput){
		this.coverageOutputs.add(coverageOutput);
	}
	
	@Override
	public void setAsciiGridFolderOutput(String asciiGridFolder){
		Util.createAccess(asciiGridFolder);
		this.asciiGridFolder = new File(asciiGridFolder).getAbsolutePath()+"/";
	}
	
	@Override
	public void setGeoTiffFolderOutput(String geoTiffFolder){
		Util.createAccess(geoTiffFolder);
		this.geoTiffFolder = new File(geoTiffFolder).getAbsolutePath()+"/";
	}
	
	@Override
	public void addAsciiGridOutput(String ascii){
		Metric metric = this.metrics.iterator().next();
		addAsciiGridOutput(metric.getName(), ascii);
	}
	
	@Override
	public void addAsciiGridOutput(String metric, String ascii){
		int size = this.windowSizes.iterator().next();
		addAsciiGridOutput(size, metric, ascii);
	}

	@Override
	public void addAsciiGridOutput(int size, String metric, String ascii){
		Util.createAccess(ascii);
		if(!this.asciiOutputs.containsKey(size)){
			this.asciiOutputs.put(size, new HashMap<String, String>());
		}
		this.asciiOutputs.get(size).put(metric, ascii);
	}
	
	@Override
	public void addTabOutput(float[] tab){
		Metric metric = this.metrics.iterator().next();
		addTabOutput(metric.getName(), tab);
	}

	@Override
	public void addTabOutput(String metric, float[] tab){
		int size = this.windowSizes.iterator().next();
		addTabOutput(size, metric, tab);
	}
	
	@Override
	public void addTabOutput(int size, String metric, float[] tab){
		if(!this.tabOutputs.containsKey(size)){
			this.tabOutputs.put(size, new HashMap<String, float[]>());
		}
		this.tabOutputs.get(size).put(metric, tab);
	}
	
	@Override
	public void addGeoTiffOutput(String geotiff){
		Metric metric = this.metrics.iterator().next();
		addGeoTiffOutput(metric.getName(), geotiff);
	}
	
	@Override
	public void addGeoTiffOutput(String metric, String geotiff){
		int size = 0;
		if(this.windowSizes.size() > 0){
			size = this.windowSizes.iterator().next();
		}
		addGeoTiffOutput(size, metric, geotiff);
	}
	
	@Override
	public void addGeoTiffOutput(int size, String metric, String geotiff){
		Util.createAccess(geotiff);
		if(!this.geotiffOutputs.containsKey(size)){
			this.geotiffOutputs.put(size, new HashMap<String, String>());
		}
		this.geotiffOutputs.get(size).put(metric, geotiff);
	}
	
	@Override
	public void addDataOutput(String metric, Map<RefPoint, Float> datas){
		this.datas = datas;
	}
	
	/*
	@Override
	public void addDataOutput(DataOutput dout){
		observers.add(dout);
	}
	*/
	
	@Override
	public void addTileAsciiGridOutput(String metric, String pathTile, Tile tile){
		Util.createAccess(pathTile);
		if(this.tileAsciiOutputs == null){
			this.tileAsciiOutputs = new HashMap<Tile, Map<String, String>>();
		}
		if(!this.tileAsciiOutputs.containsKey(tile)){
			this.tileAsciiOutputs.put(tile, new HashMap<String, String>());
		}
		this.tileAsciiOutputs.get(tile).put(metric, new File(pathTile).getAbsolutePath()+"/");
	}
	
	@Override
	public void addTileGeoTiffOutput(String metric, String pathTile, Tile tile){
		Util.createAccess(pathTile);
		if(this.tileGeoTiffOutputs == null){
			this.tileGeoTiffOutputs = new HashMap<Tile, Map<String, String>>();
		}
		if(!this.tileGeoTiffOutputs.containsKey(tile)){
			this.tileGeoTiffOutputs.put(tile, new HashMap<String, String>());
		}
		this.tileGeoTiffOutputs.get(tile).put(metric, new File(pathTile).getAbsolutePath()+"/");
	}
	
	@Override
	public void setPointsFilter(String points) {
		this.points = points;
	}
	
	@Override
	public void setPointsFilter(Set<RefPoint> points){
		this.refPoints = points;
	}
	
	@Override
	public void setPixelsFilter(String pixels) {
		this.pixels = pixels;
	}
	
	@Override
	public void setPixelsFilter(Set<? extends Pixel> pixels) {
		this.refPixels = pixels;
	}

	@Override
	public void setWindowsPath(String windowsPath) {
		Util.createAccess(windowsPath);
		this.windowsPath = new File(windowsPath).getAbsolutePath()+"/";
	}
	
	/*
	@Override
	public void addObserver(CountingObserver observer){
		this.observers.add(observer);
	}
	
	@Override
	public void setObservers(Set<CountingObserver> observers) {
		this.observers = observers;
	}
	*/
	
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
	
	@Override
	public void setBufferSize(int buffSize){
		LandscapeMetricAnalysis.setBufferSize(buffSize);
	}
	
	@Override
	public void setROIX(int roiX){
		this.roiX = roiX;
	}
	
	@Override
	public void setROIY(int roiY){
		this.roiY = roiY;
	}
	
	@Override
	public void setROIWidth(int roiWidth){
		this.roiWidth = roiWidth;
	}
	
	@Override
	public void setROIHeight(int roiHeight){
		this.roiHeight = roiHeight;
	}
	
	@Override
	// affectation statique
	public void setMinRate(double min){
		Counting.setMinRate(min/100.0);
	}
	
	@Override
	public WindowShapeType getWindowShapeType() {
		return shapeType;
	}
	
	public WindowDistanceType getWindowDistanceType() {
		return distanceType;
	}
	
	public String getWindowDistanceFunction() {
		return distanceFunction;
	}
	
	public Set<String> getRasterFiles(){
		return rasterFiles;
	}
	
	public Coverage getCoverage(){
		return coverage;
	}
	
	public Coverage getCoverage2(){
		return coverage2;
	}
	
	public String getRasterTile() {
		return rasterTile;
	}

	@Override
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
	
	public String getEntityRasterFile() {
		return entityRasterFile;
	}
	
	public float[] getEntityRasterTab() {
		return entityRasterTab;
	}
	
	public int[] getValues(){
		return values;
	}

	@Override
	public int getWindowSize(){
		return this.windowSize;
	}
	
	public Set<Integer> getWindowSizes() {
		return windowSizes;
	}
	
	public double getWindowRadius(){
		return this.radius;
	}

	private Set<Double> getWindowsRadius() {
		return windowRadius;
	}
	
	public double getDMax() {
		return dMax;
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

	/*
	public Set<CountingObserver> getObservers() {
		return observers;
	}
	*/

	public String getCsv() {
		return csv;
	}
	
	public Set<CoverageOutput> getCoverageOutputs(){
		return coverageOutputs;
	}
	
	public String getAsciiGridFolder() {
		return asciiGridFolder;
	}
	
	public String getGeoTiffFolder() {
		return geoTiffFolder;
	}
	
	public Map<String, String> getAsciiOutputs(int ws){
		return asciiOutputs.get(ws);
	}
	
	//public Map<String, String> getAsciiOutputs(){
	//	return asciiOutputs.entrySet().iterator().next().getValue();
	//}
	
	public Map<String, String> getGeoTiffOutputs(int ws){
		return geotiffOutputs.get(ws);
	}
	
	//public Map<String, String> getGeoTiffOutputs(){
	//	return geotiffOutputs.entrySet().iterator().next().getValue();
	//}
	
	public Map<String, float[]> getTabOutputs(int ws){
		return tabOutputs.get(ws);
	}
	
	//public Map<String, float[]> getTabOutputs(){
	//	return tabOutputs.entrySet().iterator().next().getValue();
	//}
	
	public Map<Tile, Map<String, String>> getTileAsciiGridOutputs(){
		return tileAsciiOutputs;
	}
	
	public Map<Tile, Map<String, String>> getTileGeoTiffOutputs(){
		return tileGeoTiffOutputs;
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
	
	public int[] getFilters(){
		return filters;
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
	
	public String getWindowsPath(){
		return windowsPath;
	}
	
	@Override
	public LandscapeMetricAnalysis build(){
		LandscapeMetricAnalysis analysis = null;
		try{
			
			if(getAnalysisType().equals(ChloeAnalysisType.SLIDING)){
				
				if((windowSizes.size() == 1 || windowRadius.size() == 1)
						&& MetricManager.hasCoherence(metrics)){
					
					analysis = LandscapeMetricAnalysisFactory.create(this);
					
				}else{
					
					analysis = new MultipleSlidingLandscapeMetricAnalysis(this);
					
				}
			}else if(getAnalysisType().equals(ChloeAnalysisType.SELECTED)){
				
				if((windowSizes.size() == 1 || windowRadius.size() == 1)
					&& MetricManager.hasCoherence(metrics)
					&& rasterFiles.size() == 0){
					
					analysis = LandscapeMetricAnalysisFactory.create(this);
					
				}else{
					
					analysis = new MultipleSelectedLandscapeMetricAnalysis(this);
					
				}
				
			}else if(getAnalysisType().equals(ChloeAnalysisType.MAP)){
			
				if(MetricManager.hasCoherence(metrics) && rasterFiles.size() == 0){
					
					analysis = LandscapeMetricAnalysisFactory.create(this);
					
				}else{
					
					analysis = new MultipleMapLandscapeMetricAnalysis(this);
					
				}
				
			}else{
				
				analysis = LandscapeMetricAnalysisFactory.create(this);
			}

		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			//reset();
		}
		return analysis;
	}

	

	
}
