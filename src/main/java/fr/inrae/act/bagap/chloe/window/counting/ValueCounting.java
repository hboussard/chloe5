package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

public class ValueCounting extends Counting implements ValueCountingInterface {

	/** the count of values */
	private Map<Integer, Float> countValues;
	
	private int[] values;
	
	private float totalCountValues;
	
	private int countClass;
	
	public ValueCounting(int[] values, float theoreticalSize){
		super(theoreticalSize);
		this.values = values;
		this.countValues = new HashMap<Integer, Float>();
	}
	
	public ValueCounting(int[] values){
		this(values, 0);
	}
	
	/**
	 * partie specifique :
	 * 4 : nombre de "0"
	 * à partir de 5 jusqu'au nombre de valeurs + 5 : les occurences de valeurs dans l'ordre numérique
	 */
	@Override
	public void doSetCounts(float[] counts){
	
		totalCountValues = validValues() - counts[4];
		
		countClass = 0;
		countValues.clear();
		for(int i=5; i<counts.length; i++){
			if(counts[i] > 0){
				countClass++;
			}
			countValues.put(values[i-5], counts[i]);
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
	
}
