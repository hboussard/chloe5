package fr.inrae.act.bagap.chloe.cluster;

import fr.inrae.act.bagap.apiland.analysis.Analysis;

public class TabFocalClusteringOutput extends Analysis {
	
	private int[] initValues;
	
	private float[] tabCluster;
	
	private float[] tabCover;
	
	private float[] tabCoeff;
	
	private double cellSize;
	
	private int noDataValue;
	
	private double nbPatch;
	
	private double totalSurface;
	
	private double totalSurfaceCarre;
	
	private double maxSurface;
	
	private double[] nbPatches;
	
	private double[] totalSurfaces;
	
	private double[] totalSurfacesCarres;
	
	private double[] maxSurfaces;

	public TabFocalClusteringOutput(float[] tabCluster, float[] tabCover, float[] tabCoeff, int[] initValues, double cellSize, int noDataValue){
		this.tabCluster = tabCluster;
		this.tabCover = tabCover;
		this.tabCoeff = tabCoeff;
		this.initValues = initValues;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	public double getNbPatch(){
		return nbPatch;
	}
	
	public double getTotalSurface(){
		return totalSurface;
	}
	
	public double getTotalSurfaceCarre(){
		return totalSurfaceCarre;
	}
	
	public double getMaxSurface(){
		return maxSurface;
	}
	
	public double getNbPatch(int v){
		return nbPatches[v];
	}
	
	public double getTotalSurface(int v){
		return totalSurfaces[v];
	}
	
	public double getTotalSurfaceCarre(int v){
		return totalSurfacesCarres[v];
	}
	
	public double getMaxSurface(int v){
		return maxSurfaces[v];
	}
	
	@Override
	protected void doInit() {
		nbPatch = 0;
		totalSurface = 0;
		totalSurfaceCarre = 0;
		int vMax = 0;
		for(float vC : tabCluster){
			if(vC != noDataValue && vC != 0){
				vMax = Math.max(vMax, (int) vC);
			}
		}
		
		vMax = 0;
		for(int iv : initValues){
			vMax = Math.max(vMax, iv);
		}
		
		nbPatches = new double[vMax+1];
		totalSurfaces = new double[vMax+1];
		totalSurfacesCarres = new double[vMax+1];
		maxSurfaces = new double[vMax+1];
	}
	
	@Override
	protected void doRun() {
		
		int vMax = 0;
		for(float vC : tabCluster){
			if(vC != noDataValue && vC != 0){
				vMax = Math.max(vMax, (int) vC);
			}
		}
		
		double size = 0.0;
		
		double[] nbPixels = new double[vMax+1];
		double[] sizes = new double[vMax+1];
		int[] values = new int[vMax+1];
		
		int ind = 0;
		int cover;
		float coeff;
		for(float vC : tabCluster){
			if(vC != noDataValue && vC != 0){

				coeff = tabCoeff[ind];
				
				nbPixels[(int) vC]++;
				
				size += coeff;
				sizes[(int) vC] += coeff;
				
				cover = (int) tabCover[ind];
				values[(int) vC] = cover;
				
				totalSurfaces[cover] += coeff;
			}
			ind++;
		}
		
		for(ind=0; ind<nbPixels.length; ind++){
			
			if(nbPixels[ind] != 0) {
				
				nbPatch += sizes[ind] / nbPixels[ind];
				
				nbPatches[values[ind]] += sizes[ind] / nbPixels[ind];
			}
		}
		
		totalSurface = (size*Math.pow(cellSize, 2))/10000.0;
		for(int i=0; i<totalSurfaces.length; i++){
			totalSurfaces[i] *= Math.pow(cellSize, 2)/10000.0;
		}
		
		maxSurface = 0;
		double surface;
		double surfaceCarre;
		ind = 0;
		for(double s : sizes){
			if(s > 0){
				
				surface = s*Math.pow(cellSize, 2)/10000.0;
				
				cover = values[ind];
				maxSurface = Math.max(maxSurface, surface);
				maxSurfaces[cover] = Math.max(maxSurfaces[cover], surface);
				
				surfaceCarre = Math.pow(surface, 2);
				totalSurfaceCarre += surfaceCarre;
				totalSurfacesCarres[cover] += surfaceCarre;
			}
			ind++;
		}
		
		setResult(true);
	}
	
	@Override
	protected void doClose() {
		tabCluster = null;
		tabCover = null;
		initValues = null;
	}

}
