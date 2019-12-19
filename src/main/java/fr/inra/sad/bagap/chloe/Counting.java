package fr.inra.sad.bagap.chloe;

import java.util.HashSet;
import java.util.Set;

public abstract class Counting implements 
	BasicCountingInterface, 
	ValueCountingInterface {

	/**
	 * minimum rate of non missing values
	 */
	protected static double minRate = 0;
	
	/**
	 * the metrics
	 */
	private Set<Metric> metrics;
	
	public Counting(){
		metrics = new HashSet<Metric>();
	}
	
	public void close(){
		for(Metric m : metrics){
			m.close();
		}
	}
	
	public void addMetric(Metric m){
		metrics.add(m);
	}
	
	public static void setMinRate(double min){
		minRate= min;
	}
	
	public static double minRate(){
		return minRate;
	}
	
	public void calculate(){
		if(validValues()/theoreticalSize() >= minRate){
			for(Metric m : metrics){
				m.calculate(this, "");
			}
		}else{
			for(Metric m : metrics){
				m.unCalculate("");
			}
		}
	}
	
	@Override
	public short theoreticalSize(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public float totalValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float validValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public float countValues() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public short[] values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float countValue(short v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public short countClass() {
		throw new UnsupportedOperationException();
	}
}
