package fr.inrae.act.bagap.chloe.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class CoupleCounting extends Counting implements CoupleCountingInterface {

	/** the count of couples */
	private Map<Float, Double> countCouples;
	
	private int nValues;
	
	private float[] couples;
	
	private int theoreticalCoupleSize;
	
	private double totalCouples;
	
	private double validCouples;
	
	private double totalCountCouples;
	
	private double homogeneousCouples;
	
	private double heterogeneousCouples;
	
	private short countCoupleClass;
	
	public CoupleCounting(int minRange, int maxRange, int nValues, float[] couples){
		super(minRange, maxRange);
		this.nValues = nValues;
		this.couples = couples;
		this.countCouples = new HashMap<Float, Double>();
	}
	
	public CoupleCounting(int minRange, int maxRange, int nValues, float[] couples, int theoreticalCoupleSize){
		this(minRange, maxRange, nValues, couples);
		this.theoreticalCoupleSize = theoreticalCoupleSize;
	}
	
	public void setTheoriticalCoupleSize(int theoreticalCoupleSize){
		this.theoreticalCoupleSize = theoreticalCoupleSize;
	}
	
	@Override
	protected void doCalculate(){
		if(validCounting() && validCouples()/theoreticalCoupleSize() >= minRate){
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
		
			totalCouples = 0;
			validCouples = 0;
			totalCountCouples = 0;
			countCoupleClass = 0;
			homogeneousCouples = 0;
			heterogeneousCouples = 0;
			countCouples.clear();
			
			totalCouples += counts[1];
			
			totalCouples += counts[2];
			validCouples += counts[2];
			
			for(int i=3; i<counts.length; i++){
				totalCouples += counts[i];
				validCouples += counts[i];
				totalCountCouples += counts[i];
				
				if(i-3 < nValues) {
					homogeneousCouples += counts[i];
				}else {
					heterogeneousCouples += counts[i];
				}
				
				if(counts[i] > 0){
					countCoupleClass++;
				}
				countCouples.put(couples[i-3], counts[i]);
			}
		}else{
			setValidCounting(false);
		}
	}
	
	/*
	private boolean isHomogeneous(float c) {
		short s1 = (short) Math.floor((double) c);
		return c == getCouple(s1, s1);
	}
	
	private static float getCouple(short v1, short v2) {
		if(v1 < v2) {
			return (float) (v2 * 0.0001 + v1);
		}else {
			return (float) (v1 * 0.0001 + v2);
		}
	}
	*/
	
	@Override
	public int theoreticalCoupleSize(){
		return theoreticalCoupleSize;
	}
	
	@Override
	public double totalCouples() {
		return totalCouples;
	}

	@Override
	public double validCouples() {
		return validCouples;
	}
	
	@Override
	public double countCouples() {
		return totalCountCouples;
	}
	
	@Override
	public float[] couples(){
		return couples;
	}
	
	@Override
	public double countCouple(float c){
		if(countCouples.containsKey(c)){
			return countCouples.get(c);
		}
		return 0;
	}

	@Override
	public short countCoupleClass() {
		return countCoupleClass;
	}
	
	@Override
	public double countHomogeneousCouples() {
		return homogeneousCouples;
	}

	@Override
	public double countHeterogenousCouples() {
		return heterogeneousCouples;
	}
	
}
