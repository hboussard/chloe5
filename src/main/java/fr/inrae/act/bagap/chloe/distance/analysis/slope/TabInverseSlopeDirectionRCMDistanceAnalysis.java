package fr.inrae.act.bagap.chloe.distance.analysis.slope;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.Analysis;

public class TabInverseSlopeDirectionRCMDistanceAnalysis extends Analysis {

	private static final float sqrt2 = (float) Math.sqrt(2);
	
	private static final float coeffReg = 314.0f;
	
	private float[] outDatas, inDatas, frictionDatas, slopeDirectionDatas, everDatas;
	
	private int[] codes;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private boolean hasValue;
	
	private List<Integer>[] waits;

	public TabInverseSlopeDirectionRCMDistanceAnalysis(float[] outDatas, float[] inDatas, float[] frictionDatas, float[] slopeDirectionDatas, int width, int height, float cellSize, int noDataValue, int[] codes) {
		this(outDatas, inDatas, frictionDatas, slopeDirectionDatas, width, height, cellSize, noDataValue, codes, noDataValue);
	}
	
	public TabInverseSlopeDirectionRCMDistanceAnalysis(float[] outDatas, float[] inDatas, float[] frictionDatas, float[] slopeDirectionDatas, int width, int height, float cellSize, int noDataValue, int[] codes, int threshold) {
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.frictionDatas = frictionDatas;
		this.slopeDirectionDatas = slopeDirectionDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.codes = codes;
		if(threshold == noDataValue){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}
	
	public boolean hasValue(){
		return hasValue;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doInit() {
		
		everDatas = new float[outDatas.length];
		waits = new ArrayList[(int) ((threshold * coeffReg)/cellSize)];
		waits[0] = new ArrayList<Integer>();
		
		hasValue = false;
		
		if(inDatas == null){
			for(int ind=0; ind<frictionDatas.length; ind++){
				if(frictionDatas[ind] == noDataValue){
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
			for(int d=0; d<threshold*coeffReg/cellSize; d++){
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
		if(dist < threshold/cellSize){
			if (waits[(int) (dist*coeffReg)] == null) {
				waits[(int) (dist*coeffReg)] = new ArrayList<Integer>();
			}
			waits[(int) (dist*coeffReg)].add(pixel);
		}
	}
	
	private void diffusion(int p, double dd) {
		//++indexG;
		
		if (everDatas[p] != 1 && threshold/cellSize > dd) {
			
			everDatas[p] = 1; // marquage de diffusion du pixel
			
			if (outDatas[p] != noDataValue) {
				float fd = frictionDatas[p]; // friction au point de diffusion
				int np;
				float v, fc, d;
				int dir, ndir;
				int x = p%width;
				int y = p/width;
				
				// en haut a gauche
				np = p - width - 1;
				dir = 1;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x > 0 && y > 0 && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en haut
				np = p - width;
				dir = 2;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(y > 0 && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (1.0 / 2) * fd + (1.0 / 2) * fc); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en haut a droite
				np = p - width + 1;
				dir = 3;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x < (width-1) && y > 0 && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// a gauche
				np = p - 1;
				dir = 4;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x > 0 && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (1.0 / 2) * fd + (1.0 / 2) * fc); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// a droite
				np = p + 1;
				dir = 5;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x < (width-1) && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (1.0 / 2) * fd + (1.0 / 2) * fc); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en bas a gauche
				np = p + width - 1;
				dir = 6;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x > 0 && y < (height-1) && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en bas
				np = p + width;
				dir = 7;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(y < (height-1) && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point cardinal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (1.0 / 2) * fd + (1.0 / 2) * fc); // distance au point cardinal
							if (v == -2 || d * cellSize < v) { // MAJ ?
								outDatas[np] = d * cellSize;
								setPixelAndValue(np, d);
							}
						}
					}
				}
				
				// en bas a droite
				np = p + width + 1;
				dir = 8;
				ndir = (int) slopeDirectionDatas[np];
				if(ndir == 0 || ndir+dir == 9){
					if(x < (width-1) && y < (height-1) && everDatas[np] != 1){
						v = outDatas[np]; // valeur au point diagonal
						if (v != noDataValue) {
							fc = frictionDatas[np];
							d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
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
		frictionDatas = null;
		slopeDirectionDatas = null;
	}

}
