package fr.inrae.act.bagap.chloe.distance.analysis.slope;

import java.util.ArrayList;
import java.util.List;

import fr.inrae.act.bagap.apiland.analysis.Analysis;

public class TabMassCumulRCMDistanceAnalysis extends Analysis {

	private static final float sqrt2 = (float) Math.sqrt(2);
	
	private static final float coeffReg = 314.0f;
	
	private float[] outDatas, inDatas, altitudeDatas, infiltrationDatas, slopeIntensityDatas, everDatas;
	
	private int[] codes;

	private float massInit;
	
	private int width, height;
	
	private float cellSize;
	
	private float localSurface;
	
	private int noDataValue;
	
	private boolean hasValue;
	
	private List<Integer>[] waits;
	
	public TabMassCumulRCMDistanceAnalysis(float[] outDatas, float[] altitudeDatas, float[] infiltrationDatas, float[] slopeIntensityDatas, int width, int height, float cellSize, int noDataValue, float massInit) {
		this(outDatas, null, altitudeDatas, infiltrationDatas, slopeIntensityDatas, width, height, cellSize, noDataValue, null, massInit);		
	}
	
	public TabMassCumulRCMDistanceAnalysis(float[] outDatas, float[] inDatas, float[] altitudeDatas, float[] infiltrationDatas, float[] slopeIntensityDatas, int width, int height, float cellSize, int noDataValue, int[] codes, float massInit) {
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.altitudeDatas = altitudeDatas;
		this.infiltrationDatas = infiltrationDatas;
		this.slopeIntensityDatas = slopeIntensityDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.localSurface = (float) Math.pow(cellSize, 2);
		this.noDataValue = noDataValue;
		this.codes = codes;
		this.massInit = massInit;
	}
	
	public boolean hasValue(){
		return hasValue;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doInit() {
		
		everDatas = new float[outDatas.length];
		waits = new ArrayList[(int) ((massInit * coeffReg)/cellSize)];
		waits[0] = new ArrayList<Integer>();
		
		hasValue = false;
		
		if(inDatas == null){
			for(int ind=0; ind<infiltrationDatas.length; ind++){
				if(infiltrationDatas[ind] == noDataValue){
					outDatas[ind] = noDataValue;
				}else if(ind == ((width*width)-1)/2){ // pixel central source de diffusion
					hasValue = true;
					outDatas[ind] = 0;
					waits[0].add(ind);
				}else{
					outDatas[ind] = -2; // to be computed
					//outDatas[ind] = threshold; // to be computed
				}
			}
		}else{
			boolean ok;
			float v;
			for (int yt = 0; yt < height; yt++) {
				for (int xt = 0; xt < width; xt++) {
					v = inDatas[yt*width+xt];
					ok = false;
					if (v != noDataValue) {
						for (int c : codes) {
							if (c == v) {
								ok = true;
								hasValue = true;
								break;
							}
						}
						if (ok) {
							outDatas[yt * width + xt] = 0; // inside the object -> distance=0
						} else {
							outDatas[yt * width + xt] = -2; // outside the object -> to be computed
							//outDatas[yt * width + xt] = threshold; // to be computed
						}
					}else{
						outDatas[yt * width + xt] = noDataValue; // nodata_value -> to be not computed
					}
					
				}
			}
			inDatas = null;
			
			if(hasValue){
				// afin de limiter le nombre de calculs de diffusion, ne diffuser qu'� partir des bords d'habitats
				boolean maj;
				for (int yt = 0; yt < height; yt++) {
					for (int xt = 0; xt < width; xt++) {
						if (outDatas[yt * width + xt] == 0) {
							maj = true;
							if(xt == 0 || outDatas[(xt-1)+yt*width] == 0){
								if(xt == 0 || yt == 0 || outDatas[(xt-1)+(yt-1)*width] == 0){
									if(yt == 0 || outDatas[xt+(yt-1)*width] == 0){
										if(xt == (width-1) || yt == 0 || outDatas[(xt+1)+(yt-1)*width] == 0){
											if(xt == (width-1) || outDatas[(xt+1)+yt*width] == 0){
												if(xt == (width-1) || yt == (height-1) || outDatas[(xt+1)+(yt+1)*width] == 0){
													if(yt == (height-1) || outDatas[xt+(yt+1)*width] == 0){
														if(xt == 0 || yt == (height-1) || outDatas[(xt-1)+(yt+1)*width] == 0){
															// do nothing
															maj = false;
														}
													}
												}
											}
										}
									}
								}
							}
							if(maj){
								setPixelAndValue(yt * width + xt, 0.0f);
							}
						}
						
					}
				}
			}
		}
	}
	
	private static float getSlopeIntensity(float alt, float nalt, float cote_adjacent) {
		
		if(alt == nalt) {
			return 0;
		}
		
		float cote_oppose = alt - nalt;
		float tangente = cote_oppose/cote_adjacent;
		double arctangente = Math.atan(tangente);
		double angle = Math.toDegrees(arctangente);
		
		//System.out.println(cote_oppose+" "+cote_adjacent+" "+tangente+" "+arctangente+" "+angle);
		
		float v =  (float) ((90 - angle)%180.0);
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		//}else if(v >= 90){
			//return  0;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private static float getSlopeIntensity(float slopIntMax) {
		
		float v =  slopIntMax;
		
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		//}else if(v >= 90){
		//	return  0;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private float friction(float infiltration) {
		
		float friction = 1 + (float) (50*Math.pow(infiltration, 10));
		return friction;
	}

	@Override
	public void doRun() {
		if(hasValue){
			
			List<Integer> wait;
			// diffusion
			for(int d=0; d<((int) ((massInit * coeffReg)/cellSize)); d++){
				//System.out.println(d+" "+((int) ((threshold * coeffReg)/cellSize)));
				wait = waits[d];
				if(wait != null){
					waits[d] = null;
					diffusionPaquet(d, wait);
					d--;
				}
			}
			
		}
		//System.out.println("nombre de diffusions = "+indexG);
		setResult(outDatas);
	}

	
	
	private void diffusionPaquet(int dd, List<Integer> wait){
		for(int p : wait){
			diffusion(p, dd/coeffReg);
		}
	}

	public void setPixelAndValue(int pixel, float dist) {
		if(dist < (int) (massInit/cellSize)){
			if (waits[(int) (dist*coeffReg)] == null) {
				waits[(int) (dist*coeffReg)] = new ArrayList<Integer>();
			}
			waits[(int) (dist*coeffReg)].add(pixel);
		}
	}
	
	private void diffusion(int p, float dd) {
		
		//System.out.println("diffusion de puis "+p);
		if (everDatas[p] != 1 && ((int) massInit/cellSize) > dd) {
			
			everDatas[p] = 1; // marquage de diffusion du pixel
			
			if (outDatas[p] != noDataValue) {
				float alt = altitudeDatas[p]; // altitude au point de diffusion
				
				int np;
				int x = p%width;
				int y = p/width;
				
				// en haut a gauche
				np = p - width - 1;
				if(x > 0 && y > 0 && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, sqrt2);
				}
				
				// en haut
				np = p - width;
				if(y > 0 && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, 1);
				}
				
				// en haut a droite
				np = p - width + 1;
				if(x < (width-1) && y > 0 && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, sqrt2);
				}
				
				// a gauche
				np = p - 1;
				if(x > 0 && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, 1);
				}
				
				// a droite
				np = p + 1;
				if(x < (width-1) && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, 1);
				}
				
				// en bas a gauche
				np = p + width - 1;
				if(x > 0 && y < (height-1) && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, sqrt2);
				}
				
				// en bas
				np = p + width;
				if(y < (height-1) && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, 1);
				}
				
				// en bas a droite
				np = p + width + 1;
				if(x < (width-1) && y < (height-1) && everDatas[np] != 1){
					
					doPrefRoad(p, np, alt, dd, sqrt2);
				}
				
			}
		}
	}
	
	private void doPrefRoad(int p, int np, float alt, float dd, float ld) {
		
		float v = outDatas[np]; // valeur au point
		if (v != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			if(nalt >= alt) {
				
				float ninf = infiltrationDatas[np];
				float friction = friction(ninf);
				float sIntLocal = getSlopeIntensity(nalt, alt, ld * cellSize);
				float sIntMax = getSlopeIntensity(slopeIntensityDatas[np]); // intensite de pente maximal
				float d;
				d = (dd + ld * friction + ((1 - ((1+sIntLocal)/(1+sIntMax))) * ((massInit / cellSize) - dd))); // distance au point
				//d = (dd + ld * friction + ((1 - ((1+sIntLocal)/(1+sIntMax))) * ((massInit / localSurface) - dd))); // distance au point
				if (v == -2 || d * cellSize < v) { // MAJ ?
					outDatas[np] = d * cellSize;
					setPixelAndValue(np, d);
				}
			}
		}
	}

	@Override
	protected void doClose() {
		waits = null;
		inDatas = null;
		infiltrationDatas = null;
		altitudeDatas = null;
		slopeIntensityDatas = null;
		everDatas = null;
	}

}
