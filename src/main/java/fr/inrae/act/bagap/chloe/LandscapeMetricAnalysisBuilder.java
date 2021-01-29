package fr.inrae.act.bagap.chloe;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;

public class LandscapeMetricAnalysisBuilder {

	private WindowAnalysisType analysisType;
	
	private WindowShapeType shapeType;
	
	private WindowDistanceType distanceType;
	
	private String raster;
	
	private int windowSize, displacement;
	
	private double windowRadius;
	
	private Set<Metric> metrics;
	
	private Set<CountingObserver> observers;

	private String csv, points, exportWindowPath;
	
	private Map<String, String> asciiOutputs;
	
	private boolean interpolation;
	
	private int bufferROI;
	
	public LandscapeMetricAnalysisBuilder(){
		this.metrics = new HashSet<Metric>();
		this.observers = new HashSet<CountingObserver>();
		reset();
	}
	
	private void reset(){
		this.analysisType = WindowAnalysisType.SLIDING;
		this.shapeType = WindowShapeType.CIRCLE;
		this.distanceType = WindowDistanceType.THRESHOLD;
		this.metrics.clear();
		this.observers.clear();
		this.displacement = 1;
		this.interpolation = false;
		this.windowSize = -1;
		this.windowRadius = -1;
		this.bufferROI = 0;
		this.csv = null;
		this.points = null;
		this.exportWindowPath = null;
		this.asciiOutputs = new HashMap<String, String>();
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

	public void setRaster(String raster) {
		this.raster = raster;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
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
	
	public void addAsciiGridOutput(String metric, String ascii){
		//addMetric(metric);
		this.asciiOutputs.put(metric, ascii);
	}
	
	public void setPointFilter(String points) {
		this.points = points;
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
	
	public void setBufferROI(int bufferROI){
		this.bufferROI = bufferROI;
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

	public String getRaster() {
		return raster;
	}

	public int getWindowSize() {
		return windowSize;
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
	
	public Map<String, String> getAsciiOutputs(){
		return asciiOutputs;
	}
	
	public int getBufferROI(){
		return bufferROI;
	}

	public LandscapeMetricAnalysis build(){
		LandscapeMetricAnalysis analysis = null;
		try{
			analysis = LandscapeMetricAnalysisFactory.create(this);
			//analysis = HugeLandscapeMetricAnalysisFactory.create(this);
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			//reset();
		}
		return analysis;
	}
	
}
