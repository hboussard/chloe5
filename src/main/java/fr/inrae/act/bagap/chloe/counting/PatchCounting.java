package fr.inrae.act.bagap.chloe.counting;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class PatchCounting extends Counting implements PatchCountingInterface {
	
	private int[] values;
	
	private double totalSurface;
	
	private int nbPatch;
	
	private double maxSurface; 
	
	private Map<Integer, Integer> nbPatches;
	
	private Map<Integer, Double> totalSurfaces;
	
	private Map<Integer, Double> maxSurfaces;
	
 	public PatchCounting(int[] values) {
		super();
		this.values = values;
		nbPatches = new HashMap<Integer, Integer>();
		totalSurfaces = new HashMap<Integer, Double>();
		maxSurfaces = new HashMap<Integer, Double>();
	}
 	
	@Override
	public void setCounts(double[] counts) {
		nbPatch = (int) counts[0];
		totalSurface = counts[1];
		maxSurface = counts[2];
		
		for(int i=0; i<values.length; i++){
			nbPatches.put(values[i], (int) counts[i+3]);
		}
		
		for(int i=0; i<values.length; i++){
			totalSurfaces.put(values[i], counts[i+3+values.length]);
		}
		
		for(int i=0; i<values.length; i++){
			maxSurfaces.put(values[i], counts[i+3+2*values.length]);
		}
	}

	@Override
	protected void doCalculate() {
		for(Metric m : metrics()){
			m.calculate(this, "");
		}
	}

	@Override
	public double getTotalSurface() {
		return totalSurface;
	}

	@Override
	public int getNbPatches() {
		return nbPatch;
	}
	
	@Override
	public double getMaxSurface(){
		return maxSurface;
	}
	
	@Override
	public int getNbPatches(int v){
		return nbPatches.get(v);
	}

	@Override
	public double getTotalSurface(int v){
		return totalSurfaces.get(v);
	}

	@Override
	public double getMaxSurface(int v){
		return maxSurfaces.get(v);
	}
	
}
