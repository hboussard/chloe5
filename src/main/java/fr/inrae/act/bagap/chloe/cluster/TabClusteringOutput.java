package fr.inrae.act.bagap.chloe.cluster;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class TabClusteringOutput extends Analysis {
	
	private int[] initValues;
	
	private double totalSurface;
	
	private int[] counts;
	
	private int[] sizes;
	
	private int[] values;
	
	private double maxSurface;
	
	private int nbPatch;
	
	private int[] tabCluster;
	
	private int[] tabCover;
	
	private double cellSize;
	
	private int[] nbPatches;
	
	private double[] totalSurfaces;
	
	private double[] maxSurfaces;
	
	public TabClusteringOutput(int[] tabCluster, int[] tabCover, int[] initValues, double cellSize){
		this.tabCluster = tabCluster;
		this.tabCover = tabCover;
		this.initValues = initValues;
		this.cellSize = cellSize;
	}
	
	public TabClusteringOutput(int[] tabCluster, float[] tabCover, int[] initValues, double cellSize){
		this.tabCluster = tabCluster;
		this.tabCover = new int[tabCover.length];
		int ind = 0;
		for(float tc : tabCover){
			this.tabCover[ind++] = (int) tc;
		}
		this.initValues = initValues;
		this.cellSize = cellSize;
	}
	
	public int getNbPatch(){
		return nbPatch;
	}
	
	public double getTotalSurface(){
		return totalSurface;
	}
	
	public double getMaxSurface(){
		return maxSurface;
	}
	
	public int getNbPatch(int v){
		return nbPatches[v];
	}
	
	public double getTotalSurface(int v){
		return totalSurfaces[v];
	}
	
	public double getMaxSurface(int v){
		return maxSurfaces[v];
	}
	
	@Override
	protected void doInit() {
		nbPatch = 0;
		totalSurface = 0;
		int vMax = 0;
		for(int vC : tabCluster){
			if(vC != 0){
				vMax = Math.max(vMax, vC);
			}
		}
		
		counts = new int[vMax+1];
		sizes = new int[vMax+1];
		values = new int[vMax+1];
		
		vMax = 0;
		for(int iv : initValues){
			vMax = Math.max(vMax, iv);
		}
		
		nbPatches = new int[vMax+1];
		totalSurfaces = new double[vMax+1];
		maxSurfaces = new double[vMax+1];
	}
	
	@Override
	protected void doRun() {
		
		int nbPixel = 0;
		int ind = 0;
		int cover;
		for(int vC : tabCluster){
			if(vC != 0){
				counts[vC] = 1;
				nbPixel++;
				sizes[vC]++;
				cover = tabCover[ind];
				values[vC] = cover;
				
				totalSurfaces[cover]++;
			}
			ind++;
		}
		
		for(int c : counts){
			if(c == 1){
				nbPatch++;
			}
		}
		for(int v : values){
			if(v != 0){
				nbPatches[v]++;
			}
		}
		
		totalSurface = (nbPixel*Math.pow(cellSize, 2))/10000.0;
		for(int i=0; i<totalSurfaces.length; i++){
			totalSurfaces[i] *= Math.pow(cellSize, 2)/10000.0;
		}
		
		maxSurface = 0; 
		ind = 0;
		for(int s : sizes){
			maxSurface = Math.max(maxSurface, s);
			if(s > 0){
				cover = values[ind];
				maxSurfaces[cover] = Math.max(maxSurfaces[cover], s);
			}
			ind++;
		}
		maxSurface *= Math.pow(cellSize, 2)/10000.0;
		
		setResult(true);
	}
	
	@Override
	protected void doClose() {
		tabCluster = null;
		tabCover = null;
		counts = null;
		sizes = null;
		initValues = null;
		values = null;
	}

}
