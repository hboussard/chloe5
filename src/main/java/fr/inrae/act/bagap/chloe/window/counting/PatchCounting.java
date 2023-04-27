package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

public class PatchCounting extends Counting implements PatchCountingInterface {
	
	private int[] values;
	
	private double totalSurface;
	
	private int nbPatch;
	
	private double maxSurface; 
	
	private Map<Integer, Integer> nbPatches;
	
	private Map<Integer, Double> totalSurfaces;
	
	private Map<Integer, Double> maxSurfaces;
	
 	public PatchCounting(int[] values, double theoreticalSize){
		super(theoreticalSize);
		this.values = values;
		nbPatches = new HashMap<Integer, Integer>();
		totalSurfaces = new HashMap<Integer, Double>();
		maxSurfaces = new HashMap<Integer, Double>();
	}
 	
 	public PatchCounting(int[] values){
		this(values, 0);
	}
 	
 	/**
	 * partie specifique :
	 * 4 : nombre de pathes
	 * 5 : surface totale
	 * 6 : surface max
	 * à partir de 7 jusqu'au nombre de valeurs + 7 : les nombres de patch par classe
	 * à partir de nombre de valeurs + 7 jusqu'à 2*nombre de valeurs + 7 : les surfaces totales par classe
	 * à partir de 2*nombre de valeurs + 7 jusqu'à 3*nombre de valeurs + 7 : les maximum de surfaces par classe
	 */
 	@Override
	public void doSetCounts(double[] counts) {
		
 		nbPatch = (int) counts[4];
 		totalSurface = counts[5];
 		maxSurface = counts[6];
			
 		for(int i=0; i<values.length; i++){
 			nbPatches.put(values[i], (int) counts[i+7]);
 		}
			
 		for(int i=0; i<values.length; i++){
 			totalSurfaces.put(values[i], counts[i+7+values.length]);
 		}
			
 		for(int i=0; i<values.length; i++){
 			maxSurfaces.put(values[i], counts[i+7+2*values.length]);
 		}	
		
	}

	@Override
	public double totalSurface() {
		return totalSurface;
	}

	@Override
	public int nbPatches() {
		return nbPatch;
	}
	
	@Override
	public double maxSurface(){
		return maxSurface;
	}
	
	@Override
	public int nbPatches(int v){
		return nbPatches.get(v);
	}

	@Override
	public double totalSurface(int v){
		return totalSurfaces.get(v);
	}

	@Override
	public double maxSurface(int v){
		return maxSurfaces.get(v);
	}
	
}
