package fr.inrae.act.bagap.chloe.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class ValueCounting extends Counting implements ValueCountingInterface {

	/** the count of values */
	private Map<Short, Float> countValues;
	
	private short[] values;
	
	private int theoreticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private float totalCountValues;
	
	private short countClass;
	
	public ValueCounting(short[] values, int theoreticalSize){
		this.values = values;
		this.countValues = new HashMap<Short, Float>();
		this.theoreticalSize = theoreticalSize;
	}
	
	@Override
	protected void doCalculate(){
		if(validValues()/theoreticalSize() >= minRate){
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
	public void setCounts(float[] counts){
		totalValues = 0;
		validValues = 0;
		totalCountValues = 0;
		countClass = 0;
		
		totalValues += counts[0];
		
		totalValues += counts[1];
		validValues += counts[1];
		
		countValues.clear();
		
		for(int i=2; i<counts.length; i++){
			totalValues += counts[i];
			validValues += counts[i];
			totalCountValues += counts[i];
			if(counts[i] > 0){
				countClass++;
			}
			countValues.put(values[i-2], counts[i]);
		}
	}
	
	@Override
	public int theoreticalSize(){
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
	public short[] values(){
		return values;
	}
	
	@Override
	public double countValue(short v){
		return countValues.get(v);
	}

	@Override
	public short countClass() {
		return countClass;
	}
	
}
