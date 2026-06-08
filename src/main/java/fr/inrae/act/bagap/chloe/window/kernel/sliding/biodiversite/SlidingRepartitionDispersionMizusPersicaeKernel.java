package fr.inrae.act.bagap.chloe.window.kernel.sliding.biodiversite;

import java.util.Arrays;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;

public class SlidingRepartitionDispersionMizusPersicaeKernel extends SlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster inEntete, outEntete;
	
	private float[] outDataPucerons;
	
	public SlidingRepartitionDispersionMizusPersicaeKernel(int windowSize, int displacement, float[] coeff, int noDataValue, int[] unfilters, EnteteRaster inEntete, EnteteRaster outEntete, float[] outDataPucerons) {
			
		super(windowSize, displacement, coeff, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.outDataPucerons = outDataPucerons;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
		
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			int pucerons = (int) inDatas(2)[(y * width()) + x];
			
			float propAiles = inDatas(4)[(y * width()) + x];
			
			int candidats = Math.round(pucerons * propAiles);
			int nonCandidats = pucerons - candidats;
			
			//System.out.println(inDatas(1)[(y * width()) + x]+" "+inDatas(2)[(y * width()) + x]+" "+inDatas(3)[(y * width()) + x]+" "+inDatas(4)[(y * width()) + x]);
			//System.out.println(pucerons+" "+candidats+" "+nonCandidats);
			
			if(candidats > 0){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = candidats; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				
				float[] dataQualiteHabitat = getQualite(x, y, mid);
				
				float[] dataVolumeRepartition = new float[windowSize()*windowSize()];
				float volumeTotal = calculateVolumeRepartition(dataVolumeRepartition, coeff(), dataQualiteHabitat);
				
				int ic, lind;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
				float localProba;
				float vl;
				int lx, ly; 
				if(volumeTotal > 0) {
					
					//int mind = ((windowSize()*windowSize())-1)/2;
					
					float[] dataLocalProba = new float[windowSize()*windowSize()];
					
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);	
									
									vl = dataVolumeRepartition[ic];
									if(vl != noDataValue()) {
										
										nb_total += 1;
										surface += localSurface;
										volume += volumeTotal;
										
										localProba = vl / volumeTotal;
										//System.out.println(localProba);
										dataLocalProba[ic] = localProba;
									}
								}
							}
						}
					}
					/*
					float somme = 0;
					for(float lp : dataLocalProba) {
						//System.out.println(lp);
						somme += lp;
					}
					System.out.println(somme);
					*/
					float[] dataLocalEffectif = new float[windowSize()*windowSize()];
					
					// dispersion des pucerons
					if(candidats > 0) {
						
						for(int c=1; c<=candidats; c++) {
							
							double r = Math.random();
							double cumulProba = 0;
							boolean affected = false;
							
							for (int dy = -mid; dy <= mid && affected == false; dy += 1) {
								if(((y + dy) >= 0) && ((y + dy) < height())){
									for (int dx = -mid; dx <= mid && affected == false; dx += 1) {
										if(((x + dx) >= 0) && ((x + dx) < width())){
											
											ic = ((dy+mid) * windowSize()) + (dx+mid);
											localProba = dataLocalProba[ic];
											if(localProba > 0) {
											
												if(r >= cumulProba && r < (cumulProba + localProba)) {
													
													dataLocalEffectif[ic]++; 
													affected = true;
												}else{
													
													cumulProba += localProba;
												}
											}
										}
									}
								}
							}
						}
						
						synchronized(outDataPucerons) {
							for (int dy = -mid; dy <= mid; dy += 1) {
								if(((y + dy) >= 0) && ((y + dy) < height())){
									for (int dx = -mid; dx <= mid; dx += 1) {
										if(((x + dx) >= 0) && ((x + dx) < width())){
												
											ic = ((dy+mid) * windowSize()) + (dx+mid);							
												
											lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
											ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
											if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
												lind = (ly*outEntete.width() + lx);
													
												outDataPucerons[lind] += dataLocalEffectif[ic];
												
												// reaffectation au point central des non-disperseurs
												if(dy == 0 && dx == 0) {
													outDataPucerons[lind] += nonCandidats;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				outDatas()[ind][2] = nb_total;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = surface;
				outDatas()[ind][5] = volume;
				
			} else{
				
				synchronized(outDataPucerons) {
				
					int lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x));
					int ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y));
					int lind = (ly*outEntete.width() + lx);
					outDataPucerons[lind] += nonCandidats;
				}
				
				outDatas()[ind][0] = 0; // filtre pas ok 
			}
		}
	}
	
	protected float[] getQualite(int x, int y, int mid){
		
		return getLocalData(3, x, y, mid);
	}

	protected float[] getLocalData(int covIndex, int x, int y, int mid){
		
		float[] data = new float[windowSize()*windowSize()];
		Arrays.fill(data, noDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						data[ic] = inDatas(covIndex)[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return data;
	}

	protected float calculateVolumeRepartition(float[] dataVolumeRepartition, float[] coeffs, float[] dataQualiteHabitat) {
		
		float volumeTotal = 0;
		
		for(int i=0; i<dataVolumeRepartition.length; i++) {
		
			float ponderation = coeffs[i];
			
			if(ponderation > 0) {
				
				float vl = ponderation * dataQualiteHabitat[i];
				
				volumeTotal += vl;
				dataVolumeRepartition[i] = vl;
			} else {
				dataVolumeRepartition[i] = noDataValue();
			}
		}
		
		return volumeTotal;
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}
	
	/*
	private void exportTab(String output, float[] tab, int x, int y){
		
		int mid = windowSize() / 2;

		double X, Y;
		X = CoordinateManager.getProjectedX(inEntete, x);
		Y = CoordinateManager.getProjectedY(inEntete, y);
			
		double minx = CoordinateManager.getProjectedX(inEntete, x-mid)-(inEntete.cellsize()/2);
		double maxx = CoordinateManager.getProjectedX(inEntete, x+mid)+(inEntete.cellsize()/2);
		double miny = CoordinateManager.getProjectedY(inEntete, y-mid)+(inEntete.cellsize()/2);
		double maxy = CoordinateManager.getProjectedY(inEntete, y+mid)-(inEntete.cellsize()/2);	

		EnteteRaster localEntete = new EnteteRaster(windowSize(), windowSize(), minx, maxx, miny, maxy, inEntete.cellsize(), inEntete.noDataValue());
		CoverageManager.write(output, tab, localEntete);
		
	}
	*/
}
