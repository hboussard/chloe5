package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ValueCounting extends Counting implements ValueCountingInterface {

	/** the count of values */
	private Map<Integer, Double> countValues;
	
	private int[] values;
	
	private double theoreticalSize;
	
	private float centralValue;
	
	private float totalValues;
	
	private float validValues;
	
	private float totalCountValues;
	
	private int countClass;
	
	public ValueCounting(int minRange, int maxRange, int[] values){
		super(minRange, maxRange);
		this.values = values;
		this.countValues = new HashMap<Integer, Double>();
	}
	
	public ValueCounting(int minRange, int maxRange, int[] values, double theoreticalSize){
		this(minRange, maxRange, values);
		this.theoreticalSize = theoreticalSize;
	}
	
	public void setTheoriticalSize(int theoreticalSize){
		this.theoreticalSize = theoreticalSize;
	}
	
	@Override
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
	}
	
	@Override
	public void setCounts(double[] counts){
	
		if(counts[0] == 1){
			
			setValidCounting(true);
			
			totalValues = 0;
			validValues = 0;
			totalCountValues = 0;
			countClass = 0;
			
			totalValues += counts[1];
			
			totalValues += counts[2];
			validValues += counts[2];
			
			centralValue = (float) counts[3];
			
			countValues.clear();
			
			for(int i=4; i<counts.length; i++){
				totalValues += counts[i];
				validValues += counts[i];
				totalCountValues += counts[i];
				if(counts[i] > 0){
					countClass++;
				}
				countValues.put(values[i-4], counts[i]);
			}
		}else{
			setValidCounting(false);
		}
		
	}
	
	@Override
	public double theoreticalSize(){
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
	public double countValues() {
		return totalCountValues;
	}
	
	@Override
	public int[] values(){
		return values;
	}
	
	@Override
	public double countValue(int v){
		
		return countValues.get(v);	
		/*
		if(countValues.containsKey(v)){
			return countValues.get(v);	
		}
		return 0;
		*/
	}

	@Override
	public int countClass() {
		return countClass;
	}
	
	@Override
	public float centralValue(){
		return centralValue;
	}
}
