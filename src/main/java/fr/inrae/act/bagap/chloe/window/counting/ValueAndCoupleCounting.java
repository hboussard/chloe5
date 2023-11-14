package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

public class ValueAndCoupleCounting extends Counting implements ValueCountingInterface, CoupleCountingInterface {

	/** the count of values */
	private Map<Integer, Float> countValues;
	
	private int[] values;
	
	private float totalCountValues;
	
	private int countClass;
	
	/** the count of couples */
	private Map<Float, Float> countCouples;
	
	private float[] couples;
	
	private int theoreticalCoupleSize;
	
	private float totalCouples;
	
	private float validCouples;
	
	private float totalCountCouples;
	
	private float homogeneousCouples;
	
	private float heterogeneousCouples;
	
	private short countCoupleClass;
	
	public ValueAndCoupleCounting(int[] values, float[] couples, float theoreticalSize, int theoreticalCoupleSize){
		super(theoreticalSize);
		this.values = values;
		this.countValues = new HashMap<Integer, Float>();
		this.couples = couples;
		this.countCouples = new HashMap<Float, Float>();
		this.theoreticalCoupleSize = theoreticalCoupleSize;
	}
	
	public ValueAndCoupleCounting(int[] values, float[] couples){
		this(values, couples, 0, 0);
	}
	
	/**
	 * partie specifique :
	 * 4 : nombre de valeur "0"
	 * 5 : nombre de couples pris en compte
	 * 6 : nombre de couple noDataValue
	 * 7 : nombre de couple "0"
	 * à partir de 8 jusqu'au nombre de valeurs + 8 : les occurences de valeurs dans l'ordre numérique
	 * à partir de 8 + nombre de valeurs jusqu'au nombre de couples + 8 + nombre de valeurs : les occurences de couples de pixels dans l'ordre numérique, couples homogènes d'abords
	 */
	@Override
	public void doSetCounts(float[] counts) {

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
	public float countValues() {
		return totalCountValues;
	}
	
	@Override
	public int[] values(){
		return values;
	}
	
	@Override
	public float countValue(int v){
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
	public int theoreticalCoupleSize(){
		return theoreticalCoupleSize;
	}
	
	@Override
	public float totalCouples() {
		return totalCouples;
	}

	@Override
	public float validCouples() {
		return validCouples;
	}
	
	@Override
	public float countCouples() {
		return totalCountCouples;
	}
	
	@Override
	public float[] couples(){
		return couples;
	}
	
	@Override
	public float countCouple(float c){
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
	public float countHomogeneousCouples() {
		return homogeneousCouples;
	}

	@Override
	public float countHeterogenousCouples() {
		return heterogeneousCouples;
	}
	
}
