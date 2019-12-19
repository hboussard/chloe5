package fr.inra.sad.bagap.chloe;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Variable;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public abstract class Metric {
	
	/** the refereed variable */
	private Variable variable;
	
	/** the metric observers */
	private Set<MetricObserver> observers;
	
	/** the result value */
	protected float value;
	
	/**
	 * constructor
	 * @param v the refereed variable
	 */
	public Metric(Variable v){
		//System.out.println("création de la métrique "+v);
		this.variable = v;
		observers = new HashSet<MetricObserver>();
	}
	
	@Override
	public String toString(){
		return variable.getName();
	}
	
	/**
	 * to set the refereed variable
	 * @param v the refereed variable
	 */
	public void setVariable(Variable v){
		this.variable = v;
	}
	
	/** @return the name of the variable */
	public String getName(){
		return variable.getName();
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
	public void notifyObservers(float value, String pref){
		for(MetricObserver o : observers){
			o.notify(this, pref+variable.getName(), value);
		}
	}
	
	public void close(){
		for(MetricObserver mo : observers){
			mo.close();
		}
	}
	
}
