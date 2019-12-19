package fr.inra.sad.bagap.chloe.counting;

import java.util.HashMap;
import java.util.Map;

public class CoupleCounting extends Counting implements CoupleCountingInterface {

	/** the count of couples */
	private Map<Float, Float> countCouples;
	
	private short nValues;
	
	private float[] couples;
	
	private int theoriticalCoupleSize;
	
	private float totalCouples;
	
	private float validCouples;
	
	private float totalCountCouples;
	
	private float homogeneousCouples;
	
	private float heterogeneousCouples;
	
	private short countCoupleClass;
	
	public CoupleCounting(short nValues, float[] couples, int theoriticalCoupleSize){
		this.nValues = nValues;
		this.couples = couples;
		this.countCouples = new HashMap<Float, Float>();
		this.theoriticalCoupleSize = theoriticalCoupleSize;
	}
	
	public void setCounts(float[] counts){
		totalCouples = 0;
		validCouples = 0;
		totalCountCouples = 0;
		countCoupleClass = 0;
		homogeneousCouples = 0;
		heterogeneousCouples = 0;
		
		totalCouples += counts[0];
		
		totalCouples += counts[1];
		validCouples += counts[1];
		
		countCouples.clear();
		
		for(int i=2; i<counts.length; i++){
			totalCouples += counts[i];
			validCouples += counts[i];
			totalCountCouples += counts[i];
			
			if(i-2 < nValues) {
				homogeneousCouples += counts[i];
			}else {
				heterogeneousCouples += counts[i];
			}
			
			if(counts[i] > 0){
				countCoupleClass++;
			}
			countCouples.put(couples[i-2], counts[i]);
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
		return theoriticalCoupleSize;
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
		return countCouples.get(c);
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
