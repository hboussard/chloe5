package fr.inrae.act.bagap.chloe.window.kernel.sliding.erosion;

import java.util.Arrays;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabMassCumulAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SlidingMassCumulKernel extends SlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster inEntete, outEntete;
	
	private int massInit;
	
	private String outputDegatIntensity/*, outputDepotIntensity*/;
	
	private float[] dataDegatIntensity/*, dataDepotIntensity*/;
	
	public SlidingMassCumulKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete,  EnteteRaster outEntete, String outputDegatIntensity/*, String outputDepotIntensity*/){	
		super(windowSize, displacement, null, noDataValue, unfilters);
		
		this.inEntete = inEntete;
		this.outEntete = outEntete;
		this.cellSize = inEntete.cellsize();
		localSurface = (float) Math.pow(cellSize, 2);
		//this.massInit = dMax;
		if(outputDegatIntensity != null) {
			//System.out.println(entete.width()+" "+entete.height()+" "+entete.width()*entete.height());
			this.dataDegatIntensity = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataDegatIntensity, 0);
			this.outputDegatIntensity = outputDegatIntensity;
		}
		/*if(outputDepotIntensity != null) {
			this.dataDepotIntensity = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataDepotIntensity, 0);
			this.outputDepotIntensity = outputDepotIntensity;
		}*/
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			//System.out.println("a");
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			massInit = (int) inDatas()[(y * width()) + x];
			
			if(!hasFilter() || filterValue(massInit)){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = massInit; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				
				float[] dataAltitude = generateAltitude(x, y, mid);
				
				float[] dataInfiltration = generateInfiltration(x, y, mid);
				
				float[] dataSlopeIntensity = generateSlopeIntensity(x, y, mid);
				
				float[] dataMassCumul = calculateMassCumul(dataAltitude, dataInfiltration, dataSlopeIntensity, massInit);
				
				//float[] dataMassInit = generateMassInit(x, y, mid);
				
				generateCoeff(dataMassCumul, massInit);
				/*
				if(massInit > 0 && massInit != 500) {
					exportTab("C:/Data/projet/coterra/essai_petit_magdelaine/data/erosion35/erosion/erosion_"+massInit+"_"+x+"_"+y+".tif", dataMassCumul, x, y);
					//exportTab("C:/Data/projet/coterra/essai_petit_magdelaine/data/erosion33/filters/coeff_"+x+"_"+y+".tif", coeff(), x, y);
				}*/
				//exportTab("C:/Data/projet/coterra/essai_magdelaine/data/erosion/degats/degat_erosion_"+x+"_"+y+".tif", dataErosion, x, y);
				
				
				//System.out.println("2");
				int ic;
				int v;
				int lind;
				float coeff;
				float dm;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
				float inflocal;
				for (int dy = -mid; dy <= mid; dy += 1) {
					if(((y + dy) >= 0) && ((y + dy) < height())){
						for (int dx = -mid; dx <= mid; dx += 1) {
							if(((x + dx) >= 0) && ((x + dx) < width())){
								ic = ((dy+mid) * windowSize()) + (dx+mid);
								coeff = coeff()[ic];
								if(coeff > 0){
									v = (int) inDatas()[((y + dy) * width()) + (x + dx)];
									nb_total += coeff;
									if(v == noDataValue()){
										nb_nodata += coeff;
									}else{
										surface += coeff*localSurface;
										dm = dataMassCumul[ic];
										volume += dm;
										
										//if(((y + dy) * width())%displacement() == 0 &&  + (x + dx)%displacement() == 0) {
										//lind = ((y + dy)/displacement())*outEntete.width() + ((x + dx)/displacement());
										
										int lx = CoordinateManager.getLocalX(outEntete, CoordinateManager.getProjectedX(inEntete, x + dx));
										int ly = CoordinateManager.getLocalY(outEntete, CoordinateManager.getProjectedY(inEntete, y + dy));
										lind = (ly*outEntete.width() + lx);
										if(outputDegatIntensity != null) {
											synchronized(dataDegatIntensity) {
												dataDegatIntensity[lind] += dm;
											}
										}/*
										if(outputDepotIntensity != null) {
											inflocal = friction(dataInfiltration[ic])*localSurface;
											synchronized(dataDepotIntensity) {
												dataDepotIntensity[lind] += inflocal;
											}
										}*/
										//}
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
			}
		}
	}
	
	private float friction(float infiltration) {
		float friction = 1 + (float) (9*Math.pow(infiltration, 5));
		return friction;
	}
	
	protected float[] generateMassInit(int x, int y, int mid){
		
		return getLocalData(1, x, y, mid);
	}
	
	protected float[] generateAltitude(int x, int y, int mid){
		
		return getLocalData(2, x, y, mid);
	}
	
	protected float[] generateInfiltration(int x, int y, int mid){
		
		return getLocalData(3, x, y, mid);
	}
	
	protected float[] generateSlopeIntensity(int x, int y, int mid){
		
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
	
	protected float[] calculateMassCumul(float[] dataAltitude, float[] dataInfiltration, float[] dataSlopeIntensity, int massInit) {
		
		float[] dataMassCumul = new float[windowSize()*windowSize()];
		//TabMassCumulRCMDistanceAnalysis rcm = new TabMassCumulRCMDistanceAnalysis(dataMassCumul, dataAltitude, dataInfiltration, dataSlopeIntensity, windowSize(), windowSize(), cellSize, noDataValue(), massInit);
		//System.out.println(massInit);
		TabMassCumulAnalysis rcm = new TabMassCumulAnalysis(dataMassCumul, dataAltitude, dataInfiltration, dataSlopeIntensity, windowSize(), windowSize(), cellSize, noDataValue(), massInit);
		rcm.allRun();
		
		return dataMassCumul;
	}

	protected void generateCoeff(float[] dataMassCumul, float massInit) {
		float[] coeff = new float[windowSize()*windowSize()];
		for(int ind=0; ind<dataMassCumul.length; ind++){
			float massCumul = dataMassCumul[ind];
			if(massCumul >= 0 && massCumul <= massInit){
				coeff[ind] = 1;
			}else{
				coeff[ind] = 0;
			}
		}
		
		setCoeff(coeff);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		
		if(outputDegatIntensity != null) {
			CoverageManager.write(outputDegatIntensity, dataDegatIntensity, outEntete);
		}
		/*
		if(outputDepotIntensity != null) {
			CoverageManager.write(outputDepotIntensity, dataDepotIntensity, outEntete);
		}
		*/
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
