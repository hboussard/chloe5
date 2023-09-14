package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public abstract class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface,
	CoupleCountingInterface,
	QuantitativeCountingInterface,
	PatchCountingInterface{//,
	//MetricObserver {
	
	/**
	 * minimum rate of valid values
	 */
	private static double minRate = 0;
	
	/**
	 * the validity of the counting
	 */
	private boolean validCounting;
	
	private double theoreticalSize;
	
	private double centralValue;
	
	private double totalValues;
	
	private double validValues;
	
	/**
	 * the metrics and values
	 */
	//private Map<Metric, Double> metrics;
	
	/**
	 * the metrics
	 */
	private Set<Metric> metrics;
	
	/**
	 * the observers
	 */
	private Set<CountingObserver> observers;
	
	public Counting(double theoreticalSize){
		this.theoreticalSize = theoreticalSize;
		//metrics = new TreeMap<Metric, Double>();
		metrics = new TreeSet<Metric>();
		observers = new HashSet<CountingObserver>();
	}
	
	/**
	 * partie commune :
	 * 0 : validite du kernel selon les filtres
	 * 1 : valeur pixel central (uniquement pour SLIDING et SELECTED, sinon 0)
	 * 2 : nombre de pixels pris en compte
	 * 3 : nombre de noDataValue
	 */
	public void setCounts(double[] counts){
		
		if(counts[0] == 1){
			
			totalValues = counts[2];
			validValues = totalValues - counts[3];
			
			if(validValues/theoreticalSize >= minRate){
				setValidCounting(true);
			}else{
				setValidCounting(false);
				return;
			}
			
			doSetCounts(counts);
			
			centralValue = counts[1];
			
		}else{
			setValidCounting(false);
		}
	}
	
	// partie specifique
	public abstract void doSetCounts(double[] counts);
	
	public void addMetric(Metric m){
		//metrics.put(m, (double) Raster.getNoDataValue());
		//m.addObserver(this);
		metrics.add(m);
	}
	
	public Set<Metric> metrics(){
		//return metrics.keySet();
		return metrics;
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
		//for(Metric m : metrics()) {
		//	m.closeObservers();
		//}
		for(CountingObserver co : observers) {
			co.close(this, metrics());
		}
	}
	
	//@Override
	//public void init(Metric m) {
	//	metrics.put(m, (double) Raster.getNoDataValue());
	//}

	//@Override
	//public void notify(Metric m, double value) {
	//	metrics.put(m, value);
	//}

	//@Override
	//public void close(Metric m) {
	//	// do nothing
	//}

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
	
	//protected abstract void doCalculate();
	
	private void doCalculate(){
		if(validCounting()){
			for(Metric m : metrics()){
				m.calculate(this, "");
			}
		}else{
			for(Metric m : metrics()){
				m.unCalculate("");
			}
		}
	}
	
	/*
	protected void doCalculate(){
		if(validCounting() && validValues()/theoreticalSize() >= minRate){
			for(Metric m : metrics()){
				m.calculate(this, "");
			}
		}else{
			for(Metric m : metrics()){
				m.unCalculate("");
			}
		}
	}*/
	
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
	
	public boolean validCounting(){
		return validCounting;
	}
	
	public void setValidCounting(boolean vc){
		this.validCounting = vc;
	}
	
	@Override
	public double theoreticalSize(){
		if(theoreticalSize == 0){
			return totalValues;
		}
		return theoreticalSize;
	}
	
	@Override
	public double totalValues() {
		return totalValues;
	}

	@Override
	public double validValues() {
		return validValues;
	}
	
	@Override
	public double centralValue(){
		return centralValue;
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
	public double squareSum() {
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

	@Override
	public int nbPatches(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double totalSurface(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double maxSurface(){
		throw new UnsupportedOperationException();
	}

	@Override
	public int nbPatches(int v){
		throw new UnsupportedOperationException();
	}

	@Override
	public double totalSurface(int v){
		throw new UnsupportedOperationException();
	}

	@Override
	public double maxSurface(int v){
		throw new UnsupportedOperationException();
	}
}
