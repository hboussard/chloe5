package fr.inrae.act.bagap.chloe.window.kernel.sliding.erosion;

import java.util.Arrays;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabErosionAltitudeRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabErosionPenteRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.AbstractTripleSlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SlidingErosionKernel extends AbstractTripleSlidingLandscapeMetricKernel {

	private float cellSize;
	
	private float localSurface; 
	
	private EnteteRaster entete;
	
	public SlidingErosionKernel(int windowSize, int displacement, int noDataValue, int[] unfilters, float cellSize, EnteteRaster entete){	
		super(windowSize, displacement, null, noDataValue, unfilters);
		this.cellSize = cellSize;
		localSurface = (float) Math.pow(cellSize, 2);
		this.entete = entete;
	}
	
	@Override
	protected void processPixel(int x, int y, int localY) {
		//System.out.println(x+" "+y+" "+localY);
		if((x-bufferROIXMin())%displacement() == 0 && (y-bufferROIYMin())%displacement() == 0){
			//System.out.println("a");
			int ind = ((((localY-bufferROIYMin())/displacement()))*((((width() - bufferROIXMin() - bufferROIXMax())-1)/displacement())+1) + (((x-bufferROIXMin())/displacement())));
			
			if(!hasFilter() || filterValue((int) inDatas()[(y * width()) + x])){ // gestion des filtres
				
				outDatas()[ind][0] = 1; // filtre ok 
				
				outDatas()[ind][1] = inDatas()[(y * width()) + x]; // affectation de la valeur du pixel central
				
				int versement = (int) outDatas()[ind][1]; 
				
				for(int i=2; i<outDatas()[0].length; i++){
					outDatas()[ind][i] = 0f;
				}
				
				final int mid = windowSize() / 2;
				
				float[] dataAltitude = generateAltitude(x, y, mid);
				
				float[] dataInfiltration = generateInfiltration(x, y, mid);
				
				float[] dataErosion = calculateErosion(dataAltitude, dataInfiltration, versement);
				
				generateCoeff(dataErosion, versement);
				
				if(versement > 0) {
					//if(x == 2000 && y == 1400) {
					//System.out.println("versement = "+versement);
					//exportTab("C:/Data/projet/coterra/essai_magdelaine/data/erosion/filters/erosion_"+x+"_"+y+"_"+versement+".tif", dataErosion, x, y);
					//exportTab("C:/Data/projet/coterra/essai_magdelaine/data/erosion/filters/coeff_"+x+"_"+y+".tif", coeff(), x, y);
				}
				
				
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
										volume += coeff*localSurface*(versement-dataErosion[ic]);
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
		
		float[] dataAltitude = new float[windowSize()*windowSize()];
		Arrays.fill(dataAltitude, noDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						dataAltitude[ic] = inDatas2()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return dataAltitude;
	}
	
	protected float[] generateInfiltration(int x, int y, int mid){
		
		float[] dataInfiltration = new float[windowSize()*windowSize()];
		Arrays.fill(dataInfiltration, noDataValue());
		int ic;
		for (int dy = -mid; dy <= mid; dy += 1) {
			if(((y + dy) >= 0) && ((y + dy) < height())){
				for (int dx = -mid; dx <= mid; dx += 1) {
					if(((x + dx) >= 0) && ((x + dx) < width())){
						ic = ((dy+mid) * windowSize()) + (dx+mid);
						dataInfiltration[ic] = inDatas3()[((y + dy) * width()) + (x + dx)];
					}
				}
			}
		}
		
		return dataInfiltration;
	}
	
	protected float[] calculateErosion(float[] dataAltitude, float[] dataInfiltration, float versement) {
		
		float[] dataErosion = new float[windowSize()*windowSize()];
		if(versement == 0) {
			//System.out.println("versement = 0");
			Arrays.fill(dataErosion, versement+1);
		}else {
			//System.out.println("versement = "+((int) versement));
			TabErosionAltitudeRCMDistanceAnalysis rcm = new TabErosionAltitudeRCMDistanceAnalysis(dataErosion, dataAltitude, dataInfiltration, windowSize(), windowSize(), cellSize, noDataValue(), (int) versement);
			//TabErosionPenteRCMDistanceAnalysis rcm = new TabErosionPenteRCMDistanceAnalysis(dataErosion, dataAltitude, dataInfiltration, windowSize(), windowSize(), cellSize, noDataValue(), (int) versement);
			rcm.allRun();
		}
		
		return dataErosion;
	}

	protected void generateCoeff(float[] dataErosion, float versement) {
		float[] coeff = new float[windowSize()*windowSize()];
		for(int ind=0; ind<dataErosion.length; ind++){
			float dist = dataErosion[ind];
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
	}

	private void exportTab(String output, float[] tab, int x, int y){
		
		int mid = windowSize() / 2;

		double X, Y;
		X = CoordinateManager.getProjectedX(entete, x);
		Y = CoordinateManager.getProjectedY(entete, y);
			
		double minx = CoordinateManager.getProjectedX(entete, x-mid)-(entete.cellsize()/2);
		double maxx = CoordinateManager.getProjectedX(entete, x+mid)+(entete.cellsize()/2);
		double miny = CoordinateManager.getProjectedY(entete, y-mid)+(entete.cellsize()/2);
		double maxy = CoordinateManager.getProjectedY(entete, y+mid)-(entete.cellsize()/2);	

		EnteteRaster localEntete = new EnteteRaster(windowSize(), windowSize(), minx, maxx, miny, maxy, entete.cellsize(), entete.noDataValue());
		CoverageManager.write(output, tab, localEntete);
		
	}
	
}
