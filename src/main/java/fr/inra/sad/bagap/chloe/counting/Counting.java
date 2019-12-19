package fr.inra.sad.bagap.chloe.counting;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.util.Couple;

public class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface,
	CoupleCountingInterface,
	QuantitativeCountingInterface{

	/**
	 * minimum rate of non missing values
	 */
	protected static double minRate = 0;
	
	/**
	 * the metrics
	 */
	private Set<Metric> metrics;
	
	/**
	 * the observers
	 */
	private Set<CountingObserver> observers;
	
	public Counting(){
		metrics = new TreeSet<Metric>();
		observers = new HashSet<CountingObserver>();
	}
	
	public void init() {
		for(CountingObserver co : observers) {
			co.init(this);
		}
	}
	
	public void close(){
		for(Metric m : metrics) {
			m.closeObservers();
		}
		for(CountingObserver co : observers) {
			co.close(this);
		}
	}
	
	public Set<Metric> metrics(){
		return metrics;
	}
	
	public void addMetric(Metric m){
		metrics.add(m);
	}
	
	public void addObserver(CountingObserver co) {
		observers.add(co);
	}
	
	public static void setMinRate(double min){
		minRate= min;
	}
	
	public static double minRate(){
		return minRate;
	}
	
	public void calculate(){
		
		for(CountingObserver co : observers) {
			co.prerun(this);
		}
		
		//System.out.println(validValues()+" "+theoreticalSize()+" "+(validValues()/theoreticalSize())+" "+minRate);
		//if(validValues()/theoreticalSize() >= minRate){
			for(Metric m : metrics){
				m.calculate(this, "");
			}
		//}else{
		//	for(Metric m : metrics){
		//		m.unCalculate("");
		//	}
		//}
	}
	
	public void export() {
		for(CountingObserver co : observers) {
			co.postrun(this);
		}
	}
	
	public int theoreticalSize(){
		throw new UnsupportedOperationException();
	}
	
	public float totalValues() {
		throw new UnsupportedOperationException();
	}

	public float validValues() {
		throw new UnsupportedOperationException();
	}
	
	public float countValues() {
		throw new UnsupportedOperationException();
	}
	
	public short[] values() {
		throw new UnsupportedOperationException();
	}

	public float countValue(short v) {
		throw new UnsupportedOperationException();
	}
	
	public short countClass() {
		throw new UnsupportedOperationException();
	}

	public int theoreticalCoupleSize() {
		throw new UnsupportedOperationException();
	}

	public float totalCouples() {
		throw new UnsupportedOperationException();
	}

	public float validCouples() {
		throw new UnsupportedOperationException();
	}

	public float countCouples() {
		throw new UnsupportedOperationException();
	}

	public float countHomogeneousCouples() {
		throw new UnsupportedOperationException();
	}

	public float countHeterogenousCouples() {
		throw new UnsupportedOperationException();
	}

	public float[] couples() {
		throw new UnsupportedOperationException();
	}

	public float countCouple(float c) {
		throw new UnsupportedOperationException();
	}
	
	public float countCouple(short v1, short v2) {
		return countCouple(Couple.getCouple(v1, v2));
	}

	public short countCoupleClass() {
		throw new UnsupportedOperationException();
	}

	public float average() {
		throw new UnsupportedOperationException();
	}

	public double sum() {
		throw new UnsupportedOperationException();
	}

	
	
}
