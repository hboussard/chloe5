package fr.inrae.act.bagap.chloe.window.kernel.sliding.biodiversite;

import java.util.Arrays;
import fr.inrae.act.bagap.apiland.analysis.distance.DistanceFunction;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;

public class SlidingRepartitionDispersionKernel extends SlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster inEntete, outEntete;
	
	/*
	private String outputEffectifs;
	
	private float[] dataEffectifs;
	*/
	//private String outputJeunes;
	
	//private String outputVieilles;
	
	//private String outputMeres;
	
	private float[] outDataJeunes;
	
	private float[] outDataVieilles;
	
	private float[] outDataMeres;
	
	private float dMax;
	
	private DistanceFunction function;
	
	public SlidingRepartitionDispersionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete, EnteteRaster outEntete, float[] outDataJeunes, float[] outDataVieilles, float[] outDataMeres, DistanceFunction function, float dMax) {
			
		super(windowSize, displacement, null, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.dMax = dMax;
		this.function = function;
		
		this.outDataJeunes = outDataJeunes;
		this.outDataVieilles = outDataVieilles;
		this.outDataMeres = outDataMeres;
	}
	
	/*
	public SlidingRepartitionDispersionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete, EnteteRaster outEntete, String outputJeunes, String outputVieilles, String outputMeres, DistanceFunction function, float dMax) {
		super(windowSize, displacement, null, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.dMax = dMax;
		this.function = function;
		
		
		//if(outputEffectifs != null) {
		//	
		//	this.dataEffectifs = new float[outEntete.width()*outEntete.height()];
		//	Arrays.fill(dataEffectifs, 0);
		//	this.outputEffectifs = outputEffectifs;
		//}
		
		if(outputJeunes != null) {
			
			this.dataJeunes = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataJeunes, 0);
			this.outputJeunes = outputJeunes;
		}
		if(outputVieilles != null) {
	
			this.dataVieilles = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataVieilles, 0);
			this.outputVieilles = outputVieilles;
		}

		if(outputMeres != null) {
	
			this.dataMeres = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataMeres, 0);
			this.outputMeres = outputMeres;
		}
	}*/
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
		
			//System.out.println("a");
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			int jeunes = (int) inDatas(1)[(y * width()) + x];
			
			int vieilles = (int) inDatas(2)[(y * width()) + x];
			
			int meres = (int) inDatas(3)[(y * width()) + x];
			
			int candidats = jeunes + vieilles + meres;
			
			if((!hasFilter() || filterValue((int) candidats)) && candidats > 0){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = candidats; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
			
				float[] dataRugosite = getRugosite(x, y, mid);
				
				//System.out.println(dataRugosite[mid + mid*windowSize()]+" "+dataRugosite[(((windowSize()*windowSize())-1)/2)]+" "+jeunes+" "+vieilles+" "+meres+" "+candidats);
				
				//exportTab("C:/Data/temp/test/rugo_"+x+"_"+y+".tif", dataRugosite, x, y);
				
				float[] dataQualiteHabitat = getQualite(x, y, mid);
				
				//exportTab("C:/Data/temp/test/qualite_"+x+"_"+y+".tif", dataQualiteHabitat, x, y);
				
				//float[] dataCapaciteAccueil = getCapaciteAccueil(x, y, mid);
				
				//System.out.println("pass "+candidats+" "+x+" "+y);
				float[] dataVolumeRepartition = new float[windowSize()*windowSize()];
				float volumeTotal = calculateVolumeRepartition(dataVolumeRepartition, dataQualiteHabitat, calculatePonderation(calculateDistance(dataRugosite)));
				
				int ic, lind;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
				float localProba;
				float effectifLocal;
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
										
										dataLocalProba[ic] = localProba;
									}
								}
							}
						}
					}
					
					float[] dataLocalEffectif = new float[windowSize()*windowSize()];
					
					// dispersion des jeunes
					if(jeunes > 0) {
						
						//dataLocalEffectif = new float[windowSize()*windowSize()];
						
						for(int c=1; c<=jeunes; c++) {
							
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
						
						
						synchronized(outDataJeunes) {
							for (int dy = -mid; dy <= mid; dy += 1) {
								if(((y + dy) >= 0) && ((y + dy) < height())){
									for (int dx = -mid; dx <= mid; dx += 1) {
										if(((x + dx) >= 0) && ((x + dx) < width())){
												
											ic = ((dy+mid) * windowSize()) + (dx+mid);							
												
											lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
											ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
											if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
												lind = (ly*outEntete.width() + lx);
													
												outDataJeunes[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}
					
					
					/*
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);							
									
									lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
									ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
									if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
										lind = (ly*outEntete.width() + lx);
										if(outputJeunes != null) {
											synchronized(dataJeunes) {
												dataJeunes[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}
					*/
					
					// dispersion des vieilles
					if(vieilles > 0) {
						
						Arrays.fill(dataLocalEffectif, 0);
						
						for(int c=1; c<=vieilles; c++) {
							
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
						
						synchronized(outDataVieilles) {
							for (int dy = -mid; dy <= mid; dy += 1) {
								if(((y + dy) >= 0) && ((y + dy) < height())){
									for (int dx = -mid; dx <= mid; dx += 1) {
										if(((x + dx) >= 0) && ((x + dx) < width())){
												
											ic = ((dy+mid) * windowSize()) + (dx+mid);							
												
											lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
											ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
											if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
												lind = (ly*outEntete.width() + lx);
													
												outDataVieilles[lind] += dataLocalEffectif[ic];	
											}
										}
									}
								}
							}
						}
					}
					
					
					
					/*
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);							
									
									lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
									ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
									if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
										lind = (ly*outEntete.width() + lx);
										if(outputVieilles != null) {
											synchronized(dataVieilles) {
												dataVieilles[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}
					*/
					
					// dispersion des meres
					if(meres > 0) {
						
						Arrays.fill(dataLocalEffectif, 0);
						
						for(int c=1; c<=meres; c++) {
							
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
						
						synchronized(outDataMeres) {
							for (int dy = -mid; dy <= mid; dy += 1) {
								if(((y + dy) >= 0) && ((y + dy) < height())){
									for (int dx = -mid; dx <= mid; dx += 1) {
										if(((x + dx) >= 0) && ((x + dx) < width())){
												
											ic = ((dy+mid) * windowSize()) + (dx+mid);							
												
											lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
											ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
											if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
												lind = (ly*outEntete.width() + lx);
													
												outDataMeres[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}
					
					
					/*
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);							
									
									lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
									ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
									if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
										lind = (ly*outEntete.width() + lx);
										if(outputMeres != null) {
											synchronized(dataMeres) {
												dataMeres[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}
					*/
					
					/*
					float[] dataLocalEffectif = new float[windowSize()*windowSize()];
					
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
					
					
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height())){
							for (int dx = -mid; dx <= mid; dx += 1) {
								if(((x + dx) >= 0) && ((x + dx) < width())){
									
									ic = ((dy+mid) * windowSize()) + (dx+mid);							
									
									lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
									ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
									if(lx >= 0 && lx < outEntete.width() && ly >= 0 && ly < outEntete.height()) {
										lind = (ly*outEntete.width() + lx);
										if(outputEffectifs != null) {
											synchronized(dataEffectifs) {
												dataEffectifs[lind] += dataLocalEffectif[ic];
											}
										}
									}
								}
							}
						}
					}*/
					
					/*
					if((x==100 && y==100) 
					|| (x==153 && y==70)
					|| (x==150 && y==150)
					|| (x==75 && y==150)
					|| (x==75 && y==200)
					|| (x==226 && y==100)) {
						
						String period = "apres_moisson";
						//String period = "sortie_hiver";
						//String period = "fin_saison_culturale";
						
						File f = new File("C:/Data/projet/bannwart/data/test12/"+period+"_repartition_effectifs_locaux_"+x+"_"+y+".tif");
						
						if(!f.exists()) {
							exportTab("C:/Data/projet/bannwart/data/test12/"+period+"_proba_locale_"+x+"_"+y+".tif", dataLocalProba, x, y);
							exportTab("C:/Data/projet/bannwart/data/test12/"+period+"_repartition_effectifs_locaux_"+x+"_"+y+".tif", dataLocalEffectif, x, y);	
						}
					}
					*/
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
	
	protected float[] getRugosite(int x, int y, int mid){
		 
		return getLocalData(4, x, y, mid);
	}
	
	protected float[] getQualite(int x, int y, int mid){
		
		return getLocalData(5, x, y, mid);
	}
	
	/*
	protected float[] getCapaciteAccueil(int x, int y, int mid){
		
		return getLocalData(6, x, y, mid);
	}
	*/	

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

	protected float[] calculatePonderation(float[] distance) {
		
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
		
		//setCoeff(ponderation);
		return ponderation;
	}
	
	protected float calculateVolumeRepartition(float[] dataVolumeRepartition/*, float[] dataDistance*/, float[] dataQualiteHabitat, /*float[] dataCapaciteAccueil,*/ float[] dataPonderation) {
		
		float volumeTotal = 0;
		
		for(int i=0; i<dataVolumeRepartition.length; i++) {
			
			//float d = dataDistance[i];
			float ponderation = dataPonderation[i];
			
			if(ponderation > 0) {
				
				//float vl = ponderation * dataQualiteHabitat[i] * dataCapaciteAccueil[i];
				//float vl = ponderation * (float) Math.pow(dataQualiteHabitat[i], 4);
				float vl = ponderation * (float) Math.pow(dataQualiteHabitat[i], 2);
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
		/*
		if(outputJeunes != null) {
			
			for(int i=0; i<outEntete.width()*outEntete.height(); i++) {
				if(inDatas(1)[i] == noDataValue()) {
					dataJeunes[i] = noDataValue();
				}
			}			
			
			CoverageManager.write(outputJeunes, dataJeunes, outEntete);
		}
		
		dataJeunes = null;
		
		if(outputVieilles != null) {
			
			for(int i=0; i<outEntete.width()*outEntete.height(); i++) {
				if(inDatas(1)[i] == noDataValue()) {
					dataVieilles[i] = noDataValue();
				}
			}			
			
			CoverageManager.write(outputVieilles, dataVieilles, outEntete);
		}
		
		dataVieilles = null;
		
		if(outputMeres != null) {
			
			for(int i=0; i<outEntete.width()*outEntete.height(); i++) {
				if(inDatas(1)[i] == noDataValue()) {
					dataMeres[i] = noDataValue();
				}
			}			
			
			CoverageManager.write(outputMeres, dataMeres, outEntete);
		}
		
		dataMeres = null;
		*/
		
		/*
		if(outputEffectifs != null) {
			
			for(int i=0; i<outEntete.width()*outEntete.height(); i++) {
				if(inDatas(1)[i] == noDataValue()) {
					dataEffectifs[i] = noDataValue();
				}
			}			
			
			CoverageManager.write(outputEffectifs, dataEffectifs, outEntete);
		}
		
		dataEffectifs = null;
		*/
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
		CoverageManager.write(output, tab, localEntete);
		
	}
	
}
