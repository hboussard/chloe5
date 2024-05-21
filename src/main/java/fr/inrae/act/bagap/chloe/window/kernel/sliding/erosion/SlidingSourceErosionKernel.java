package fr.inrae.act.bagap.chloe.window.kernel.sliding.erosion;

import java.util.Arrays;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabSourceErosionAltitudeRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SlidingSourceErosionKernel extends SlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster inEntete, outEntete;
	
	private boolean interpolate;
	
	private String outputDegatIntensity, outputDepotIntensity;
	
	private float[] dataDegatIntensity, dataDepotIntensity;
	
	public SlidingSourceErosionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, EnteteRaster inEntete,  EnteteRaster outEntete, boolean interpolate, String outputDegatIntensity, String outputDepotIntensity){	
		super(windowSize, displacement, null, noDataValue, unfilters);
		this.inEntete = inEntete;
		this.cellSize = inEntete.cellsize();
		this.outEntete = outEntete;
		localSurface = (float) Math.pow(this.cellSize, 2);
		this.interpolate = interpolate;
		//System.out.println(entete);
		if(outputDegatIntensity != null) {
			this.dataDegatIntensity = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataDegatIntensity, 0);
			this.outputDegatIntensity = outputDegatIntensity;
		}
		if(outputDepotIntensity != null) {
			this.dataDepotIntensity = new float[outEntete.width()*outEntete.height()];
			Arrays.fill(dataDepotIntensity, 0);
			this.outputDepotIntensity = outputDepotIntensity;
		}
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			//System.out.println("a");
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			int versement = (int ) inDatas()[(y * width()) + x];
			
			if(!hasFilter() || filterValue(versement)){ // gestion des filtres
			
				outDatas()[ind][0] = 1; // filtre ok 
					
				outDatas()[ind][1] = versement; // affectation de la valeur du pixel central
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				
				float[] dataAltitude = generateAltitude(x, y, mid);
				
				float[] dataInfiltration = generateInfiltration(x, y, mid);
				
				float[] dataSlopeIntensity = generateSlopeIntensity(x, y, mid);
				
				float[] dataErosion = calculateErosion(dataAltitude, dataInfiltration, dataSlopeIntensity, versement);
				
				generateCoeff(dataErosion, versement);
				/*
				if(versement > 0) {
					//if(x >= 800 && x < 1000 && y >= 300 && y < 500) {
						//System.out.println("versement = "+versement);
						exportTab("C:/Data/projet/coterra/essai_petit_magdelaine/data/erosion/filters/erosion_"+x+"_"+y+"_"+versement+".tif", dataErosion, x, y);
						exportTab("C:/Data/projet/coterra/essai_petit_magdelaine/data/erosion/filters/coeff_"+x+"_"+y+".tif", coeff(), x, y);
					//}
				}*/
				
				//System.out.println("2");
				int ic;
				int v;
				float coeff;
				float nb_total = 0;
				float nb_nodata = 0;
				float surface = 0;
				float volume = 0;
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
										volume += coeff/**localSurface*/*(versement-dataErosion[ic]);
										if(((y + dy) * width())%displacement() == 0 &&  + (x + dx)%displacement() == 0) {
											int lind = ((y + dy)/displacement())*outEntete.width() + ((x + dx)/displacement());
											float de = dataErosion[ic];
											if(outputDegatIntensity != null) {
												synchronized(dataDegatIntensity) {
													dataDegatIntensity[lind] += versement-de;
												}
											}
											if(outputDepotIntensity != null) {
												synchronized(dataDepotIntensity) {
													dataDepotIntensity[lind] += de;
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
			}
		}
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
	
	protected float[] calculateErosion(float[] dataAltitude, float[] dataInfiltration, float[] dataSlopeIntensity, float versement) {
		
		float[] dataErosion = new float[windowSize()*windowSize()];
		if(versement == 0) {
			//System.out.println("versement = 0");
			Arrays.fill(dataErosion, versement+1);
		}else {
			//System.out.println("versement = "+((int) versement));
			TabSourceErosionAltitudeRCMDistanceAnalysis rcm = new TabSourceErosionAltitudeRCMDistanceAnalysis(dataErosion, dataAltitude, dataInfiltration, dataSlopeIntensity, windowSize(), windowSize(), cellSize, noDataValue(), (int) versement);
			//TabSourceErosionPenteRCMDistanceAnalysis rcm = new TabSourceErosionPenteRCMDistanceAnalysis(dataErosion, dataAltitude, dataInfiltration, windowSize(), windowSize(), cellSize, noDataValue(), (int) versement);
			rcm.allRun();
		}
		
		return dataErosion;
	}

	protected void generateCoeff(float[] dataErosion, float versement) {
		float[] coeff = new float[windowSize()*windowSize()];
		float dist;
		for(int ind=0; ind<dataErosion.length; ind++){
			dist = dataErosion[ind];
			if(dist >= 0 && dist <= versement){
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
			if(displacement() > 1 && !interpolate) {
				
				CoverageManager.write(outputDegatIntensity, dataDegatIntensity, outEntete);
				
			}else if(displacement() > 1 && interpolate) {
				
				CoverageManager.write(outputDegatIntensity, Util.extend(dataDegatIntensity, outEntete, inEntete, displacement()), inEntete);
				
			}else {
				
				CoverageManager.write(outputDegatIntensity, dataDegatIntensity, inEntete);
			}
		}
		if(outputDepotIntensity != null) {
			if(displacement() > 1 && !interpolate) {
				
				CoverageManager.write(outputDepotIntensity, dataDepotIntensity, outEntete);
				
			}else if(displacement() > 1 && interpolate) {
				
				CoverageManager.write(outputDepotIntensity, Util.extend(dataDepotIntensity, outEntete, inEntete, displacement()), inEntete);
				
			}else {
				
				CoverageManager.write(outputDepotIntensity, dataDepotIntensity, inEntete);
			}
		}
	}

	private void exportTab(String output, float[] tab, int x, int y){
		
		int mid = windowSize() / 2;

		/*
		double X, Y;
		X = CoordinateManager.getProjectedX(entete, x);
		Y = CoordinateManager.getProjectedY(entete, y);
		 */
			
		double minx = CoordinateManager.getProjectedX(inEntete, x-mid)-(inEntete.cellsize()/2);
		double maxx = CoordinateManager.getProjectedX(inEntete, x+mid)+(inEntete.cellsize()/2);
		double miny = CoordinateManager.getProjectedY(inEntete, y-mid)+(inEntete.cellsize()/2);
		double maxy = CoordinateManager.getProjectedY(inEntete, y+mid)-(inEntete.cellsize()/2);	

		EnteteRaster localEntete = new EnteteRaster(windowSize(), windowSize(), minx, maxx, miny, maxy, inEntete.cellsize(), inEntete.noDataValue());
		CoverageManager.write(output, tab, localEntete);
	}
	
}
