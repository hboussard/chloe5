package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

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
	
	public CoupleCounting(int nValues, float[] couples, double theoreticalSize, int theoreticalCoupleSize){
		super(theoreticalCoupleSize);
		this.nValues = nValues;
		this.couples = couples;
		this.countCouples = new HashMap<Float, Double>();
	}
	
	public CoupleCounting(int nValues, float[] couples){
		this(nValues, couples, 0, 0);
	}
	
	/**
	 * partie specifique :
	 * 4 : nombre de couples pris en compte
	 * 5 : nombre de couple noDataValue
	 * 6 : nombre de couple "0"
	 * à partir de 7 jusqu'au nombre de couples + 7 : les occurences de couples de pixels dans l'ordre numérique, couples homogènes d'abords
	 */
	@Override
	public void doSetCounts(double[] counts){
		
		
		totalCouples = counts[4];
		validCouples = totalCouples - counts[5];
		totalCountCouples = validCouples - counts[6];
		
		countCoupleClass = 0;
		homogeneousCouples = 0;
		heterogeneousCouples = 0;
		countCouples.clear();
		for(int i=7; i<counts.length; i++){
			if(i-7 < nValues) {
				homogeneousCouples += counts[i];
			}else {
				heterogeneousCouples += counts[i];
			}
				
			if(counts[i] > 0){
				countCoupleClass++;
			}
			countCouples.put(couples[i-7], counts[i]);
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
