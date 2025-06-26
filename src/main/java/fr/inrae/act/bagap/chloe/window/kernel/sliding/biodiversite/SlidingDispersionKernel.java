package fr.inrae.act.bagap.chloe.window.kernel.sliding.biodiversite;

import java.lang.reflect.Array;
import java.util.Arrays;

import fr.inrae.act.bagap.apiland.analysis.distance.DistanceFunction;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import tec.uom.se.AbstractSystemOfUnits;
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
	
	private DistanceFunction function;
	
	public SlidingDispersionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete, EnteteRaster outEntete, String outputEffectifs, DistanceFunction function, float dMax) {
		super(windowSize, displacement, null, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.dMax = dMax;
		this.function = function;
		
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
			
			if((!hasFilter() || filterValue((int) candidats)) && candidats > 0){ // gestion des filtres
				
				//System.out.println("pass "+candidats);
				
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
				
				float[] dataPonderation = calculatePonderation(x, y, dataDistance);
				
				float[] dataVolumeRepartition = new float[windowSize()*windowSize()];
				float volumeTotal = calculateVolumeRepartition(dataVolumeRepartition/*, dataDistance*/, dataQualiteHabitat, dataCapaciteAccueil, dataPonderation);
				
				//if((x==500 || x==600 || x==610) && y==500) {
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/rugosite_locale_"+x+"_"+y+".tif", dataRugosite, x, y);	
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/qualite_locale_"+x+"_"+y+".tif", dataQualiteHabitat, x, y);	
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/capacite_locale_"+x+"_"+y+".tif", dataCapaciteAccueil, x, y);	
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/distance_locale_"+x+"_"+y+".tif", dataDistance, x, y);	
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/ponderation_locale_"+x+"_"+y+".tif", dataPonderation, x, y);	
					//exportTab("C:/Data/projet/bannwart/data/dispersion_locale/disperion_locale_"+x+"_"+y+".tif", dataVolumeRepartition, x, y);	
				//}
				
				int ic, lind;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
				float effectifLocal;
				float[] effectifsLocaux = new float[windowSize()*windowSize()];
				float vl;
				int lx, ly; 
				
				if(volumeTotal > 0) {
					
					//int mind = ((windowSize()*windowSize())-1)/2;
					
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
										
										effectifLocal = candidats * vl / volumeTotal;
										
										effectifsLocaux[ic] = effectifLocal;
										
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
				
				if((x==500 || x==570 || x==600 || x==610) && y==500) {
					exportTab("C:/Data/projet/bannwart/data/dispersion_locale/disperion_locale_"+x+"_"+y+".tif", dataVolumeRepartition, x, y);
					exportTab("C:/Data/projet/bannwart/data/dispersion_locale/repartition_effectifs_locaux_"+x+"_"+y+".tif", effectifsLocaux, x, y);	
				}
				
				outDatas()[ind][2] = nb_total;
				outDatas()[ind][3] = nb_nodata;
				outDatas()[ind][4] = surface;
				outDatas()[ind][5] = volume;
				
			} else{
					
				outDatas()[ind][0] = 0; // filtre pas ok 
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
	
		return distance;
	}

	protected float[] calculatePonderation(int x, int y, float[] distance) {
		
		float[] ponderation = new float[windowSize()*windowSize()];
		
		for(int ind=0; ind<distance.length; ind++){
			float dist = distance[ind];
			if(dist >= 0 && dist <= dMax){
				if(function == null){
					ponderation[ind] = 1;
				}else{
					//ponderation[ind] = (float) function.interprete(dist);
					ponderation[ind] = (float) Math.exp(-1*Math.pow(dist, 2)/Math.pow(dMax/2, 2));
				}
			}else{
				ponderation[ind] = 0;
			}
		}
		
		return ponderation;
	}
	
	protected float calculateVolumeRepartition(float[] dataVolumeRepartition/*, float[] dataDistance*/, float[] dataQualiteHabitat, float[] dataCapaciteAccueil, float[] dataPonderation) {
		
		float volumeTotal = 0;
		
		for(int i=0; i<dataVolumeRepartition.length; i++) {
			
			//float d = dataDistance[i];
			float ponderation = dataPonderation[i];
			
			if(ponderation > 0) {
				
				float vl = ponderation * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				//float vl = (dMax-d) * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				
				//float vl = (dMax-d) * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				//float vl = localSurface * (dMax-vd) * (float) Math.pow(dataQualiteHabitat[i], 2) * dataCapaciteAccueil[i];
				//float vl = localSurface * (dMax-vd) * ((dataQualiteHabitat[i] + dataCapaciteAccueil[i])/2);
				//float vl = localSurface * (dMax-vd) * ((9*dataQualiteHabitat[i] + dataCapaciteAccueil[i])/10);
				
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
		
		//System.out.println(output);
		//System.out.println(localEntete);
		
		CoverageManager.write(output, tab, localEntete);
		
	}
	
}
