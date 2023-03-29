package fr.inrae.act.bagap.chloe.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class ValueAndCoupleCounting extends Counting implements ValueCountingInterface, CoupleCountingInterface {

	/** the count of values */
	private Map<Integer, Double> countValues;
	
	private int[] values;
	
	private int theoreticalSize;
	
	private float centralValue;
	
	private float totalValues;
	
	private float validValues;
	
	private float totalCountValues;
	
	private int countClass;
	
	/** the count of couples */
	private Map<Float, Double> countCouples;
	
	private float[] couples;
	
	private int theoreticalCoupleSize;
	
	private double totalCouples;
	
	private double validCouples;
	
	private double totalCountCouples;
	
	private double homogeneousCouples;
	
	private double heterogeneousCouples;
	
	private short countCoupleClass;
	
	public ValueAndCoupleCounting(int[] values, float[] couples){
		super();
		this.values = values;
		this.countValues = new HashMap<Integer, Double>();
		this.couples = couples;
		this.countCouples = new HashMap<Float, Double>();
	}
	
	public ValueAndCoupleCounting(int[] values, float[] couples, int theoreticalSize, int theoreticalCoupleSize){
		this(values, couples);
		this.theoreticalSize = theoreticalSize;
		this.theoreticalCoupleSize = theoreticalCoupleSize;
	}
	
	public void setTheoriticalSize(int theoreticalSize){
		this.theoreticalSize = theoreticalSize;
	}
	
	@Override
	protected void doCalculate(){
		if(validCouples()/theoreticalCoupleSize() >= minRate){
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
		totalCountValues = 0;
		countClass = 0;
		totalCouples = 0;
		validCouples = 0;
		totalCountCouples = 0;
		countCoupleClass = 0;
		homogeneousCouples = 0;
		heterogeneousCouples = 0;
		countCouples.clear();
		
		totalValues += counts[0];
		
		totalValues += counts[1];
		validValues += counts[1];
		
		centralValue = (float) counts[2];
		
		countValues.clear();
		
		for(int i=3; i<values.length+3; i++){
			totalValues += counts[i];
			validValues += counts[i];
			totalCountValues += counts[i];
			if(counts[i] > 0){
				countClass++;
			}
			countValues.put(values[i-3], counts[i]);
		}
		
		totalCouples += counts[values.length+3];
		
		totalCouples += counts[values.length+4];
		validCouples += counts[values.length+4];
		
		//System.out.println(counts.length+" "+values.length+" "+couples.length);
		
		for(int i=values.length+5; i<values.length+5+couples.length; i++){
			totalCouples += counts[i];
			validCouples += counts[i];
			totalCountCouples += counts[i];
			
			if(i-(values.length+5) < values.length) {
				homogeneousCouples += counts[i];
			}else {
				heterogeneousCouples += counts[i];
			}
			
			if(counts[i] > 0){
				countCoupleClass++;
			}
			//System.out.println(counts.length+" "+values.length+" "+couples.length+" "+i+" "+nValues+" "+((i-nValues)-3));
			//System.out.println(couples[(i-nValues)-3]);
			//System.out.println(counts[i]);
			countCouples.put(couples[i-(values.length+5)], counts[i]);
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
	public void setTheoriticalCoupleSize(int theoreticalCoupleSize){
		this.theoreticalCoupleSize = theoreticalCoupleSize;
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
