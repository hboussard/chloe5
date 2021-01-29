package fr.inrae.act.bagap.chloe.metric;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;

public abstract class Metric implements Comparable<Metric> {
	
	/** the metric observers */
	private Set<MetricObserver> observers;
	
	/** the name of the metric */
	private String name;
	
	/** the result value */
	protected double value;
	
	/**
	 * constructor
	 */
	public Metric(){
		this.observers = new HashSet<MetricObserver>();
	}
	
	public Metric(String name) {
		this();
		this.name = name;		
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public int compareTo(Metric m) {
		return this.name.compareTo(m.getName());
	}
	
	/** @return the name of the variable */
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * to add an observer
	 * @param o the observer
	 */
	public void addObserver(MetricObserver o){
		observers.add(o);
	}

	/**
	 * to calculate the metric on a specific process
	 * @param wp the specific process
	 */
	public final void calculate(Counting c, String pref){
		value = Raster.getNoDataValue();
		doCalculate(c);
		notifyObservers(value, pref);
	}
	
	public final void unCalculate(String pref){
		value = Raster.getNoDataValue();
		notifyObservers(value, pref);
	}

	protected abstract void doCalculate(Counting co);

	/**
	 * to notify the observers
	 * @param <AnalysisProcess>
	 * @param wp the calling process
	 * @param pref the prefix of the metric name
	 */
	public void notifyObservers(double value, String pref){
		for(MetricObserver o : observers){
			o.notify(this, value);
		}
	}
	
	public void closeObservers(){
		for(MetricObserver o : observers){
			o.close(this);
		}
	}
	
}
