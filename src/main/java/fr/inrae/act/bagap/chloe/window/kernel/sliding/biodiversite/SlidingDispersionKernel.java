package fr.inrae.act.bagap.chloe.window.kernel.sliding.biodiversite;

import java.lang.reflect.Array;
import java.util.Arrays;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
/**
 * inDatas :
 * 1 : candidats
 * 2 : rugosite
 * 3 : qualite d'habitat
 * 4 : capacite d'accueil
 */
public class SlidingDispersionKernel extends SlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster inEntete, outEntete;
	
	private String outputEffectifs;
	
	private float[] dataEffectifs;
	
	private float dMax;
	
	public SlidingDispersionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete, EnteteRaster outEntete, String outputEffectifs, float dMax) {
		super(windowSize, displacement, null, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.dMax = dMax;
		
		if(outputEffectifs != null) {
			
			this.dataEffectifs = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataEffectifs, 0);
			this.outputEffectifs = outputEffectifs;
		}
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
		
			//System.out.println("a");
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			float candidats = inDatas()[(y * width()) + x];
			
			if(!hasFilter() || filterValue((int) candidats)){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = candidats; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
			
				float[] dataRugosite = generateRugosite(x, y, mid);
				
				float[] dataQualiteHabitat = generateQualiteHabitat(x, y, mid);
				
				float[] dataCapaciteAccueil = generateCapaciteAccueil(x, y, mid);
				
				float[] dataDistance = calculateDistance(dataRugosite);
				
				float[] dataVolumeDeplacement = new float[windowSize()*windowSize()];
				float volumeTotal = calculateVolumeDeplacement(dataVolumeDeplacement, dataDistance, dataQualiteHabitat, dataCapaciteAccueil);
				//float volumeTotal = calculateSurfaceAccessibilite(dataVolumeDeplacement, dataDistance, dataQualiteHabitat, dataCapaciteAccueil);
				
				
				int ic, lind;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
				float effectifLocal;
				float vl;
				int lx, ly; 
				
				if(volumeTotal > 0) {
					
					int mind = ((windowSize()*windowSize())-1)/2;
					
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);							
									vl = dataVolumeDeplacement[ic];
									if(vl != noDataValue()) {
										
										nb_total += 1;
										surface += localSurface;
										volume += volumeTotal;
										
										effectifLocal = candidats * vl / volumeTotal;
										
										/*
										if(ic == mind && vl == 0) {
											System.out.println(effectifLocal+" "+vl+" "+volumeTotal);
										}
										*/
										
										lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
										ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
										if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
											lind = (ly*outEntete.width() + lx);
											if(outputEffectifs != null) {
												synchronized(dataEffectifs) {
													dataEffectifs[lind] += effectifLocal;
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
					
				outDatas()[ind][0] = 0; // filtre pas ok 
				
				/*
				final int mid = windowSize() / 2;
				int lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + mid));
				int ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + mid));
				if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
					int lind = (ly*outEntete.width() + lx);
					dataEffectifs[lind] = noDataValue(); 
				}
				*/
			}
		}
	}
	
	protected float[] generateCandidat(int x, int y, int mid){
		
		return getLocalData(1, x, y, mid);
	}
	 
	protected float[] generateRugosite(int x, int y, int mid){
		 
		return getLocalData(2, x, y, mid);
	}
	
	protected float[] generateQualiteHabitat(int x, int y, int mid){
		
		return getLocalData(3, x, y, mid);
	}
	
	protected float[] generateCapaciteAccueil(int x, int y, int mid){
		
		return getLocalData(4, x, y, mid);
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
	
	protected float[] calculateDistance(float[] rugosite) {
		
		float[] distance = new float[windowSize()*windowSize()];
		
		TabRCMDistanceAnalysis rcm = new TabRCMDistanceAnalysis(distance, rugosite, windowSize(), windowSize(), (float) cellSize, noDataValue(), dMax);
		rcm.allRun();
		/*
		int ind = ((windowSize()*windowSize())-1)/2;
		if(distance[ind] != 0) {
			System.out.println(distance[ind]+" "+rugosite[ind]);	
		}
		*/
		
		return distance;
	}

	protected float calculateVolumeDeplacement(float[] dataVolumeDeplacement, float[] dataDistance, float[] dataQualiteHabitat, float[] dataCapaciteAccueil) {
		
		float volumeTotal = 0;
		
		int ind = ((windowSize()*windowSize())-1)/2;
		/*
		
		if(dataDistance[ind] == 0) {
			System.out.println(dataDistance[ind]);	
		}*/
		
		for(int i=0; i<dataVolumeDeplacement.length; i++) {
			float vd = dataDistance[i];
			if(vd >=0 && vd <= dMax) {
				float vl = localSurface * (dMax-vd) * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				//float vl = localSurface * (dMax-vd) * (float) Math.pow(dataQualiteHabitat[i], 2) * dataCapaciteAccueil[i];
				//float vl = localSurface * (dMax-vd) * ((dataQualiteHabitat[i] + dataCapaciteAccueil[i])/2);
				//float vl = localSurface * (dMax-vd) * ((9*dataQualiteHabitat[i] + dataCapaciteAccueil[i])/10);
				volumeTotal += vl;
				dataVolumeDeplacement[i] = vl;
			} else {
				dataVolumeDeplacement[i] = noDataValue();
			}
			/*
			if(i == ind && dataVolumeDeplacement[i] == 0) {
				System.out.println(dataVolumeDeplacement[ind]+" "+dataDistance[ind]+" "+dataQualiteHabitat[ind]+" "+dataCapaciteAccueil[ind]);
			}
			*/
		}
		
		return volumeTotal;
	}
	
	protected float calculateSurfaceAccessibilite(float[] dataVolumeDeplacement, float[] dataDistance, float[] dataQualiteHabitat, float[] dataCapaciteAccueil) {
		
		float volumeTotal = 0;
		
		int ind = ((windowSize()*windowSize())-1)/2;
		/*
		
		if(dataDistance[ind] == 0) {
			System.out.println(dataDistance[ind]);	
		}*/
		
		for(int i=0; i<dataVolumeDeplacement.length; i++) {
			float vd = dataDistance[i];
			if(vd >=0 && vd <= dMax) {
			//if(vd >=0 && vd >= dMax-5 && vd <= dMax) {
				float vl = 1 * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				//float vl = 1 * ((dataQualiteHabitat[i] + dataCapaciteAccueil[i])/2);
				//float vl = 1 * ((9*dataQualiteHabitat[i] + dataCapaciteAccueil[i])/10);
				volumeTotal += vl;
				dataVolumeDeplacement[i] = vl;
			} else {
				dataVolumeDeplacement[i] = noDataValue();
			}
			/*
			if(i == ind && dataVolumeDeplacement[i] == 0) {
				System.out.println(dataVolumeDeplacement[ind]+" "+dataDistance[ind]+" "+dataQualiteHabitat[ind]+" "+dataCapaciteAccueil[ind]);
			}
			*/
		}
		
		return volumeTotal;
	}
	
	@Override
	public void dispose(){
		if(outputEffectifs != null) {
			
			for(int i=0; i<outEntete.width()*outEntete.height(); i++) {
				if(inDatas(1)[i] == noDataValue()) {
					dataEffectifs[i] = noDataValue();
				}
			}			
			
			CoverageManager.write(outputEffectifs, dataEffectifs, outEntete);
		}
		
		dataEffectifs = null;
		
		super.dispose();
	}
}
