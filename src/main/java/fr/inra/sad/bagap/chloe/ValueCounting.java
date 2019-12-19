package fr.inra.sad.bagap.chloe;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ValueCounting extends Counting implements ValueCountingInterface {

	/** the count of values */
	private Map<Short, Float> countValues;
	
	private short[] values;
	
	//private float[] counts;
	
	private short theoriticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private float totalCountValues;
	
	private short countClass;
	
	public ValueCounting(short[] values, short theoriticalSize){
		this.values = values;
		this.countValues = new HashMap<Short, Float>();
		this.theoriticalSize = theoriticalSize;
	}
	
	/*public void setCountValue(short v, float c){
		//System.out.println(v+" --> "+c);
		//countValues.put(v, c);
	}*/
	
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
	public short theoreticalSize(){
		return theoriticalSize;
	}
	
	
	@Override
	public float totalValues() {
		return totalValues;
	}

	@Override
	public float validValues() {
		return validValues;
	}
	
	@Override
	public float countValues() {
		return totalCountValues;
	}
	
	@Override
	public short[] values(){
		return values;
	}
	
	@Override
	public float countValue(short v){
		return countValues.get(v);
	}

	@Override
	public short countClass() {
		return countClass;
	}
	
	/*
	@Override
	public float totalValues() {
		float count = 0;
		Iterator<Float> ite = countValues.values().iterator();
		while(ite.hasNext()){
			count += ite.next();
		}
		return count;
	}

	@Override
	public float validValues() {
		float count = 0;
		Iterator<Float> ite = countValues.values().iterator();
		ite.next();
		while(ite.hasNext()){
			count += ite.next();
		}
		return count;
	}
	
	@Override
	public float countValues() {
		float count = 0;
		Iterator<Float> ite = countValues.values().iterator();
		ite.next();
		ite.next();
		while(ite.hasNext()){
			count += ite.next();
		}
		return count;
	}
	
	@Override
	public Set<Short> values(){
		return countValues.keySet();
	}
	
	@Override
	public float countValue(short v){
		return countValues.get(v);
	}

	@Override
	public short countClass() {
		short count = 0;
		Iterator<Float> ite = countValues.values().iterator();
		ite.next();
		ite.next();
		while(ite.hasNext()){
			if(ite.next() > 0){
				count++;
			}
		}
		return count;
	}*/
	
}
