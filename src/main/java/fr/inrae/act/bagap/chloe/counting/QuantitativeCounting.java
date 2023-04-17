package fr.inrae.act.bagap.chloe.counting;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {

	private double theoriticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private double sum, squareSum, minimum, maximum;
	
	private float centralValue;
	
	public QuantitativeCounting(int minRange, int maxRange) {	
		super(minRange, maxRange);
	}
	
	public QuantitativeCounting(int minRange, int maxRange, double theoriticalSize) {
		this(minRange, maxRange);
		this.theoriticalSize = theoriticalSize;
	}
	
	public void setTheoriticalSize(int theoriticalSize){
		this.theoriticalSize = theoriticalSize;
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
		
		setValidCounting(true);
			
		totalValues = 0;
		validValues = 0;
		sum = 0;
			
		totalValues += counts[1];
			
		totalValues += counts[2];
		validValues += counts[2];
			
		sum = counts[3];
			
		squareSum = counts[4];
			
		minimum = counts[5];
			
		maximum = counts[6];
			
		centralValue = (float) counts[7];
			
	}
	
	@Override
	public double theoreticalSize() {
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
		//System.out.println(sum+" "+validValues+" "+(sum / validValues));
		return (float) (sum / validValues);
	}

	@Override
	public double sum() {
		return sum;
	}
	
	public double squareSum() {
		return squareSum;
	}
	
	@Override
	public double standardDeviation() {
		/*
		Double v = Math.sqrt((squareSum / validValues) - Math.pow(sum / validValues, 2));
		if(v.isNaN()){
			System.out.println(squareSum+" "+validValues+" "+sum+" "+Math.sqrt((squareSum / validValues) - Math.pow(sum / validValues, 2)));
			System.out.println((squareSum / validValues));
			System.out.println(Math.pow(sum / validValues, 2));
		}*/
		if(((squareSum / validValues) - Math.pow(sum / validValues, 2)) < 0){
			return 0;
		}
		return Math.sqrt((squareSum / validValues) - Math.pow(sum / validValues, 2));
	}
	
	@Override
	public double minimum(){
		return minimum;
	}
	
	@Override
	public double maximum(){
		return maximum;
	}

	
	@Override 
	public float centralValue(){
		return centralValue;
	}

}
