package fr.inrae.act.bagap.chloe.counting;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricObserver;
import fr.inrae.act.bagap.chloe.util.Couple;

public abstract class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface,
	CoupleCountingInterface,
	QuantitativeCountingInterface,
	MetricObserver {

	/**
	 * minimum rate of non missing values
	 */
	protected static double minRate = 0;
	
	private final int minRange, maxRange;
	
	/**
	 * the metrics and values
	 */
	private Map<Metric, Double> metrics;
	
	/**
	 * the observers
	 */
	private Set<CountingObserver> observers;
	
	public Counting(int minRange, int maxRange){
		this.minRange = minRange;
		this.maxRange = maxRange;
		metrics = new TreeMap<Metric, Double>();
		observers = new HashSet<CountingObserver>();
	}
	
	public int minRange(){
		return this.minRange;
	}
	
	public int maxRange(){
		return this.maxRange;
	}
	
	public abstract void setCounts(double[] counts);
	
	public void addMetric(Metric m){
		metrics.put(m, (double) Raster.getNoDataValue());
		m.addObserver(this);
	}
	
	public Set<Metric> metrics(){
		return metrics.keySet();
	}
	
	public Set<CountingObserver> observers(){
		return observers;
	}
	
	public void init() {
		for(CountingObserver co : observers) {
			co.init(this, metrics());
		}
	}
	
	public void close(){
		for(Metric m : metrics()) {
			m.closeObservers();
		}
		for(CountingObserver co : observers) {
			co.close(this, metrics());
		}
	}
	
	@Override
	public void init(Metric m) {
		metrics.put(m, (double) Raster.getNoDataValue());
	}

	@Override
	public void notify(Metric m, double value) {
		metrics.put(m, value);
	}

	@Override
	public void close(Metric m) {
		// do nothing
	}

	public void addObserver(CountingObserver co) {
		observers.add(co);
	}
	
	public static void setMinRate(double min){
		minRate = min;
	}
	
	public static double minRate(){
		return minRate;
	}
	
	public void calculate(){
		for(CountingObserver co : observers) {
			co.prerun(this);
		}
		
		//System.out.println(validValues()+" "+theoreticalSize()+" "+(validValues()/theoreticalSize())+" "+minRate);
		doCalculate();
	}
	
	protected abstract void doCalculate();
	
	public void export(int x, int y) {
		for(CountingObserver co : observers) {
			co.postrun(this, x, y, metrics);
		}
	}
	
	public void export(int id) {
		for(CountingObserver co : observers) {
			co.postrun(this, id, metrics);
		}
	}
	
	@Override
	public int theoreticalSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double totalValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double validValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int[] values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countValue(int v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int countClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int theoreticalCoupleSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double totalCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double validCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countCouples() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countHomogeneousCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countHeterogenousCouples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float[] couples() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double countCouple(float c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double countCouple(short v1, short v2) {
		return countCouple(Couple.getCouple(v1, v2));
	}

	@Override
	public short countCoupleClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double average() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double standardDeviation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double sum() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double minimum(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double maximum(){
		throw new UnsupportedOperationException();
	}

	
}
