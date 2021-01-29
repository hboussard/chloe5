package fr.inrae.act.bagap.chloe.counting;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {

	private final int theoriticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private double sum;
	
	public QuantitativeCounting(int theoriticalSize) {
		this.theoriticalSize = theoriticalSize;
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
		sum = 0;
		
		totalValues += counts[0];
		
		totalValues += counts[1];
		validValues += counts[1];
		
		sum = counts[2];
	}
	
	@Override
	public int theoreticalSize() {
		return theoriticalSize;
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
	public double average() {
		return (float) (sum / validValues);
	}

	@Override
	public double sum() {
		return sum;
	}

}
