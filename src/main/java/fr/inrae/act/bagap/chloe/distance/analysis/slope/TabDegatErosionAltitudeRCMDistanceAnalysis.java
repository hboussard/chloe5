package fr.inrae.act.bagap.chloe.distance.analysis.slope;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class TabDegatErosionAltitudeRCMDistanceAnalysis extends Analysis {

	private static final float sqrt2 = (float) Math.sqrt(2);
	
	private static final float coeffReg = 314.0f;
	
	private float[] outDatas, inDatas, infiltrationDatas, altitudeDatas, everDatas;
	
	private int[] codes;

	private float threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private boolean hasValue;
	
	private List<Integer>[] waits;
	
	public TabDegatErosionAltitudeRCMDistanceAnalysis(float[] outDatas, float[] altitudeDatas, float[] infiltrationDatas, int width, int height, float cellSize, int noDataValue, float threshold) {
		this(outDatas, null, altitudeDatas, infiltrationDatas, width, height, cellSize, noDataValue, null, threshold);		
	}
	
	public TabDegatErosionAltitudeRCMDistanceAnalysis(float[] outDatas, float[] inDatas, float[] altitudeDatas, float[] infiltrationDatas, int width, int height, float cellSize, int noDataValue, int[] codes, float threshold) {
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.infiltrationDatas = infiltrationDatas;
		this.altitudeDatas = altitudeDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.codes = codes;
		this.threshold = threshold;
	}
	
	public boolean hasValue(){
		return hasValue;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doInit() {
		
		everDatas = new float[outDatas.length];
		waits = new ArrayList[(int) ((threshold * coeffReg)/cellSize)];
		//System.out.println(threshold);
		//System.out.println((int) ((threshold * coeffReg)/cellSize));
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

	@Override
	public void doRun() {
		if(hasValue){
			
			List<Integer> wait;
			// diffusion
			for(int d=0; d<((int) ((threshold * coeffReg)/cellSize)); d++){
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

	/*
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
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	*/
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
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private float friction(float slopeIntensity, float infiltration) {
		float friction = 2 + 9*infiltration - slopeIntensity;
		if(friction >= 9) {
			friction *= 10;
		}
		return friction;
	}
	/*
	private float getSlopeIntensity(float alt, float nalt, float dist) {
		
		if(alt == nalt) {
			return 0;
		}
		
		float tangente = dist/(alt - nalt);
		float v =  (float) ((90 + (90 + Math.toDegrees(Math.atan(tangente))))%180.0);
		
		if(v <= 45){
			return  1;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	*/
	private void diffusionPaquet(int dd, List<Integer> wait){
		for(int p : wait){
			diffusion(p, dd/coeffReg);
		}
	}

	public void setPixelAndValue(int pixel, float dist) {
		if(dist < (int) (threshold/cellSize)){
			if (waits[(int) (dist*coeffReg)] == null) {
				waits[(int) (dist*coeffReg)] = new ArrayList<Integer>();
			}
			waits[(int) (dist*coeffReg)].add(pixel);
		}
	}
	
	private void diffusion(int p, double dd) {
		//++indexG;
		//System.out.println("diffusion de puis "+p);
		if (everDatas[p] != 1 && ((int) threshold/cellSize) > dd) {
			
			everDatas[p] = 1; // marquage de diffusion du pixel
			
			if (outDatas[p] != noDataValue) {
				float alt = altitudeDatas[p]; // altitude au point de diffusion
				int np;
				float v, infc, d;
				float nalt;
				float sInt;
				float friction;
				
				int x = p%width;
				int y = p/width;
				
				// en haut a gauche
				np = p - width - 1;
				if(x > 0 && y > 0 && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, sqrt2 * cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + sqrt2 * friction); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en haut
				np = p - width;
				if(y > 0 && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + friction); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en haut a droite
				np = p - width + 1;
				if(x < (width-1) && y > 0 && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, sqrt2 * cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + sqrt2 * friction); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// a gauche
				np = p - 1;
				if(x > 0 && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + friction); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// a droite
				np = p + 1;
				if(x < (width-1) && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + friction); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en bas a gauche
				np = p + width - 1;
				if(x > 0 && y < (height-1) && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, sqrt2 * cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + sqrt2 * friction); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}	
				}
				
				// en bas
				np = p + width;
				if(y < (height-1) && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + friction); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en bas a droite
				np = p + width + 1;
				if(x < (width-1) && y < (height-1) && everDatas[np] != 1){
					nalt = altitudeDatas[np]; 
					if(alt <= nalt){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							infc = infiltrationDatas[np];
							sInt = getSlopeIntensity(alt, nalt, sqrt2 * cellSize);
							friction = friction(sInt, infc);
							d = (float) (dd + sqrt2 * friction); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
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
		everDatas = null;
	}

}
