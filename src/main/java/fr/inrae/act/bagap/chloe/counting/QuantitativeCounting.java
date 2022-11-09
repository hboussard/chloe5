package fr.inrae.act.bagap.chloe.counting;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {

	private int theoriticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private double sum, squareSum, minimum, maximum;
	
	private float centralValue;
	
	public QuantitativeCounting(int minRange, int maxRange) {	
		super(minRange, maxRange);
	}
	
	public QuantitativeCounting(int minRange, int maxRange, int theoriticalSize) {
		this(minRange, maxRange);
		this.theoriticalSize = theoriticalSize;
	}
	
	public void setTheoriticalSize(int theoriticalSize){
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
	public void setCounts(double[] counts){
		totalValues = 0;
		validValues = 0;
		sum = 0;
		
		totalValues += counts[0];
		
		totalValues += counts[1];
		validValues += counts[1];
		
		sum = counts[2];
		
		squareSum = counts[3];
		
		minimum = counts[4];
		
		maximum = counts[5];
		
		centralValue = (float) counts[6];
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
