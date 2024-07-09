package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

public class ValueAndCoupleCounting extends Counting implements ValueCountingInterface, CoupleCountingInterface {

	/** the count of values */
	private Map<Integer, Double> countValues;
	
	private int[] values;
	
	private double totalCountValues;
	
	private int countClass;
	
	/** the count of couples */
	private Map<Float, Double> countCouples;
	
	private float[] couples;
	
	private double theoreticalCoupleSize;
	
	private double totalCouples;
	
	private double validCouples;
	
	private double totalCountCouples;
	
	private double homogeneousCouples;
	
	private double heterogeneousCouples;
	
	private short countCoupleClass;
	
	public ValueAndCoupleCounting(double resolution, int[] values, float[] couples, double theoreticalSize, double theoreticalCoupleSize){
		super(resolution, theoreticalSize);
		this.values = values;
		this.countValues = new HashMap<Integer, Double>();
		this.couples = couples;
		this.countCouples = new HashMap<Float, Double>();
		this.theoreticalCoupleSize = theoreticalCoupleSize;
	}
	
	public ValueAndCoupleCounting(double resolution, int[] values, float[] couples){
		this(resolution, values, couples, 0, 0);
	}
	
	/**
	 * partie specifique :
	 * 4 : nombre de valeur "0"
	 * 5 : nombre de couples pris en compte
	 * 6 : nombre de couple noDataValue
	 * 7 : nombre de couple "0"
	 * � partir de 8 jusqu'au nombre de valeurs + 8 : les occurences de valeurs dans l'ordre num�rique
	 * � partir de 8 + nombre de valeurs jusqu'au nombre de couples + 8 + nombre de valeurs : les occurences de couples de pixels dans l'ordre num�rique, couples homog�nes d'abords
	 */
	@Override
	public void doSetCounts(double[] counts) {

		totalCountValues = validValues() - counts[4];
		
		countClass = 0;
		countValues.clear();
		for(int i=5; i<values.length+5; i++){
			if(counts[i] > 0){
				countClass++;
			}
			countValues.put(values[i-5], counts[i]);
		}
		
		totalCouples = counts[values.length+5];
		validCouples = totalCouples - counts[values.length+6];
		totalCountCouples = validCouples - counts[values.length+7];
		
		countCoupleClass = 0;
		homogeneousCouples = 0;
		heterogeneousCouples = 0;
		countCouples.clear();
		for(int i=values.length+8; i<values.length+8+couples.length; i++){
			if(i-(values.length+8) < values.length) {
				homogeneousCouples += counts[i];
			}else {
				heterogeneousCouples += counts[i];
			}
				
			if(counts[i] > 0){
				countCoupleClass++;
			}
			countCouples.put(couples[i-(values.length+8)], counts[i]);
		}
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
	}

	@Override
	public int countClass() {
		return countClass;
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
	public double theoreticalCoupleSize(){
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
