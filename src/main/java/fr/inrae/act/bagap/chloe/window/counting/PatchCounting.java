package fr.inrae.act.bagap.chloe.window.counting;

import java.util.HashMap;
import java.util.Map;

public class PatchCounting extends Counting implements PatchCountingInterface {
	
	private int[] values;
	
	private double totalSurface;
	
	private int nbPatch;
	
	private double maxSurface; 
	
	private double totalSurfaceCarre;
	
	private Map<Integer, Integer> nbPatches;
	
	private Map<Integer, Double> totalSurfaces;
	
	private Map<Integer, Double> maxSurfaces;
	
	private Map<Integer, Double> totalSurfacesCarres;
	
 	public PatchCounting(double resolution, int[] values, double theoreticalSize){
		super(resolution, theoreticalSize);
		this.values = values;
		nbPatches = new HashMap<Integer, Integer>();
		totalSurfaces = new HashMap<Integer, Double>();
		maxSurfaces = new HashMap<Integer, Double>();
		totalSurfacesCarres = new HashMap<Integer, Double>();
	}
 	
 	public PatchCounting(double resolution, int[] values){
		this(resolution, values, 0);
	}
 	
 	/**
	 * partie specifique :
	 * 4 : nombre de pathes
	 * 5 : surface totale
	 * 6 : surface max
	 * 7 : somme carre des surfaces
	 * a partir de 8 jusqu'au nombre de valeurs + 8 : les nombres de patch par classe
	 * a partir de nombre de valeurs + 8 jusqu'2*nombre de valeurs + 8 : les surfaces totales par classe
	 * a partir de 2*nombre de valeurs + 8 jusqu'3*nombre de valeurs + 8 : les maximum de surfaces par classe
	 * a partir de 3*nombre de valeurs + 8 jusqu'4*nombre de valeurs + 8 : les carres des surfaces par classe
	 */
 	@Override
	public void doSetCounts(double[] counts) {
		
 		nbPatch = (int) counts[4];
 		totalSurface = counts[5];
 		maxSurface = counts[6];
		totalSurfaceCarre = counts[7];	
 		
 		for(int i=0; i<values.length; i++){
 			nbPatches.put(values[i], (int) counts[i+8]);
 		}
			
 		for(int i=0; i<values.length; i++){
 			totalSurfaces.put(values[i], counts[i+8+values.length]);
 		}
			
 		for(int i=0; i<values.length; i++){
 			maxSurfaces.put(values[i], counts[i+8+2*values.length]);
 		}	
 		
 		for(int i=0; i<values.length; i++){
 			totalSurfacesCarres.put(values[i], counts[i+8+3*values.length]);
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
	public double totalSurfaceCarre() {
		return totalSurfaceCarre;
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
	
	@Override
	public double totalSurfaceCarre(int v) {
		return totalSurfacesCarres.get(v);
	}
	
}
