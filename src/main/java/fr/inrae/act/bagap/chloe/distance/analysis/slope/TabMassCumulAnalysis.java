package fr.inrae.act.bagap.chloe.distance.analysis.slope;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class TabMassCumulAnalysis extends Analysis {

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
	
	public TabMassCumulAnalysis(float[] outDatas, float[] altitudeDatas, float[] infiltrationDatas, float[] slopeIntensityDatas, int width, int height, float cellSize, int noDataValue, float massInit) {
		this(outDatas, null, altitudeDatas, infiltrationDatas, slopeIntensityDatas, width, height, cellSize, noDataValue, null, massInit);		
	}
	
	public TabMassCumulAnalysis(float[] outDatas, float[] inDatas, float[] altitudeDatas, float[] infiltrationDatas, float[] slopeIntensityDatas, int width, int height, float cellSize, int noDataValue, int[] codes, float massInit) {
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
		waits = new ArrayList[((int) (massInit * coeffReg))+1];
		waits[(int) (massInit*coeffReg)] = new ArrayList<Integer>();
		
		hasValue = false;
		
		if(inDatas == null){
			for(int ind=0; ind<infiltrationDatas.length; ind++){
				if(infiltrationDatas[ind] == noDataValue){
					outDatas[ind] = noDataValue;
				}else if(ind == ((width*width)-1)/2){ // pixel central source de diffusion
					hasValue = true;
					outDatas[ind] = massInit;
					waits[(int) (massInit*coeffReg)].add(ind);
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
							outDatas[yt * width + xt] = massInit; // inside the object -> distance=0
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
								setPixelAndValue(yt * width + xt, massInit);
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
			for(int m=(int)(massInit * coeffReg); m>0; m--){
				//System.out.println(d+" "+((int) ((threshold * coeffReg)/cellSize)));
				wait = waits[m];
				if(wait != null){
					waits[m] = null;
					diffusionPaquet(m, wait);
					m++;
				}
			}
			
		}
		
		setResult(outDatas);
	}

	private void diffusionPaquet(int masseLocale, List<Integer> wait){
		for(int p : wait){
			diffusion(p, masseLocale/coeffReg);
		}
	}

	public void setPixelAndValue(int pixel, float masse) {
		if(masse > 0 && masse <= (int) massInit){
			//System.out.println(masse);
			if (waits[(int) (masse*coeffReg)] == null) {
				waits[(int) (masse*coeffReg)] = new ArrayList<Integer>();
			}
			waits[(int) (masse*coeffReg)].add(pixel);
		}
	}
	
	private void diffusion(int p, float masseLocale) {
		
		//System.out.println("diffusion de puis "+p);
		if (everDatas[p] != 1 && (masseLocale > 0)) {
			
			everDatas[p] = 1; // marquage de diffusion du pixel
			
			if (outDatas[p] != noDataValue) {
				
				float alt0 = altitudeDatas[p]; // altitude au point de diffusion
				float inf0 = infiltrationDatas[p]; // infiltration au point de diffusion
				float friction0 = friction(inf0); // friction au point de diffusion
				
				float masseLocale0 = masseLocale - (friction0 * localSurface); // masse apres infiltration locale
				
				float sIntMax = getSlopeIntensity(slopeIntensityDatas[p]); // intensite de pente maximal
				
				int np;
				int x = p%width;
				int y = p/width;
				
				// en haut a gauche (1)
				np = p - width - 1;
				if(x > 0 && y > 0 && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, sqrt2);
				}
				
				// en haut (2)
				np = p - width;
				if(y > 0 && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, 1);
				}
				
				// en haut a droite (3)
				np = p - width + 1;
				if(x < (width-1) && y > 0 && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, sqrt2);
				}
				
				// a gauche (4)
				np = p - 1;
				if(x > 0 && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, 1);
				}
				
				// a droite (5)
				np = p + 1;
				if(x < (width-1) && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, 1);
				}
				
				// en bas a gauche (6)
				np = p + width - 1;
				if(x > 0 && y < (height-1) && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, sqrt2);
				}
				
				// en bas (7)
				np = p + width;
				if(y < (height-1) && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, 1);
				}
				
				// en bas a droite (8)
				np = p + width + 1;
				if(x < (width-1) && y < (height-1) && everDatas[np] != 1){
					
					doMassTransfer(np, alt0, masseLocale0, sIntMax, sqrt2);
				}
			}
		}
	}
	
	private void doMassTransfer1(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			if(nalt <= alt) {
				
				float masseLocale1;
				
				if(sIntMax == 0) {
					masseLocale1 = masseLocale0;
				}else {
					float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
					float coeffRepartition = sIntLocal / sIntMax;
					
					masseLocale1 = masseLocale0 * coeffRepartition;
				}
				
				if (m == -2 || masseLocale1 > m) { // MAJ ?
					outDatas[np] = masseLocale1;
					setPixelAndValue(np, masseLocale1);
				}
			}
		}
	}
	
	private void doMassTransfer2(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
		
			float nalt = altitudeDatas[np]; 
			float masseLocale1;
				
			if(sIntMax < 0) {
				masseLocale1 = 0;
			}else {
				float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
				float coeffRepartition = (1 + sIntLocal) / (1 + sIntMax);
				
				masseLocale1 = masseLocale0 * coeffRepartition;
			}
			
			if (m == -2 || masseLocale1 > m) { // MAJ ?
				outDatas[np] = masseLocale1;
				setPixelAndValue(np, masseLocale1);
			}
		}
	}
	
	private void doMassTransfer3(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			if(nalt <= alt) {
				
				float masseLocale1;
				
				if(sIntMax == 0) {
					masseLocale1 = masseLocale0;
				}else {
					float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
					float coeffRepartition = (1 + sIntLocal) / (1 + sIntMax);
					
					masseLocale1 = masseLocale0 * coeffRepartition;
				}
				
				if (m == -2 || masseLocale1 > m) { // MAJ ?
					outDatas[np] = masseLocale1;
					setPixelAndValue(np, masseLocale1);
				}
			}
		}
	}
	
	private void doMassTransfer4(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			if(nalt <= alt) {
				
				float masseLocale1 = 0;
				
				if(sIntMax == 0) {
					masseLocale1 = masseLocale0;
				}else {
					float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
					
					if(sIntMax - sIntLocal < 0.1) {
						masseLocale1 = masseLocale0;
					}
				}
				
				if (m == -2 || masseLocale1 > m) { // MAJ ?
					outDatas[np] = masseLocale1;
					setPixelAndValue(np, masseLocale1);
				}
			}
		}
	}
	
	private void doMassTransfer5(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			float masseLocale1 = 0;
				
			float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
				
			if(sIntMax - sIntLocal < 0.1) {
				
				masseLocale1 = masseLocale0;
			}
				
			if (m == -2 || masseLocale1 > m) { // MAJ ?
				outDatas[np] = masseLocale1;
				setPixelAndValue(np, masseLocale1);
			}
		}
	}
	
	private void doMassTransfer(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			float masseLocale1 = 0;
				
			float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
				
			if(sIntMax - sIntLocal < 0.1) {
				
				float coeffRepartition = (1 + sIntLocal) / (1 + sIntMax);
				//System.out.println(coeffRepartition);
				masseLocale1 = masseLocale0 * coeffRepartition;
			}
				
			if (m == -2 || masseLocale1 > m) { // MAJ ?
				outDatas[np] = masseLocale1;
				setPixelAndValue(np, masseLocale1);
			}
		}
	}
	
	private void doMassTransfer7(int np, float alt, float masseLocale0, float sIntMax, float ld) {
		
		float m = outDatas[np]; // valeur au point
		if (m != noDataValue) {
			float nalt = altitudeDatas[np]; 
			
			float masseLocale1 = 0;
				
			float sIntLocal = getSlopeIntensity(alt, nalt, ld * cellSize);
				
			if(sIntMax - sIntLocal < 0.05) {
				
				float coeffRepartition = (1 + sIntLocal) / (1 + sIntMax);
				//System.out.println(coeffRepartition);
				masseLocale1 = masseLocale0 * coeffRepartition;
			}
				
			if (m == -2 || masseLocale1 > m) { // MAJ ?
				outDatas[np] = masseLocale1;
				setPixelAndValue(np, masseLocale1);
			}
		}
	}
	
	private float friction(float infiltration) {
		
		//float friction = 1 + (float) (9*Math.pow(infiltration, 10));
		float friction = 1 + (float) (9*Math.pow(infiltration, 5));
		//float friction = 1 + (9*infiltration);
		//float friction = (float) (10*Math.pow(infiltration, 10));
		//float friction = 10*infiltration;
		//float friction = 1 + (float) (Math.pow(infiltration, 10));
		return friction;
	}
	
	private static float getSlopeIntensity(float alt, float nalt, float cote_adjacent) {
		
		if(alt == nalt) {
			return 0;
		}
		
		float cote_oppose = alt - nalt;
		float tangente = cote_oppose/cote_adjacent;
		double arctangente = Math.atan(tangente);
		double angle = Math.toDegrees(arctangente);
		
		return getSlopeIntensity((float) ((90 - angle)%180.0));
	}
	
	private static float getSlopeIntensity(float angle) {
		if(angle <= 45){
			return  1;
		}else if(angle >= 135){
			return  -1;
		}else{
			return (float) ((90-angle)/45.0);
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
