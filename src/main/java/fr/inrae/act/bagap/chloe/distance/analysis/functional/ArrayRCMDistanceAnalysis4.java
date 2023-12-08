package fr.inrae.act.bagap.chloe.distance.analysis.functional;

import java.util.ArrayList;
import java.util.List;
import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ArrayRCMDistanceAnalysis4 extends Analysis {

	//private int indexG = 0;
	
	private static final float sqrt2 = (float) Math.sqrt(2);
	
	private static final float coeffReg = 314.0f;
	
	private float[] outDatas, frictionDatas, everDatas;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private List<Integer>[] waits;
	
	private boolean hasValue;

	public ArrayRCMDistanceAnalysis4(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, double threshold) {
		this.outDatas = outDatas;
		this.frictionDatas = frictionDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doInit() {

		everDatas = new float[outDatas.length];
		
		waits = new ArrayList[(int) ((threshold * coeffReg)/cellSize)];
		waits[0] = new ArrayList<Integer>();
		
		hasValue = false;
		for(int ind=0; ind<frictionDatas.length; ind++){
			if(frictionDatas[ind] == noDataValue){
				outDatas[ind] = noDataValue;
			}else if(ind == ((width*width)-1)/2){ // pixel central source de diffusion
				hasValue = true;
				outDatas[ind] = 0;
				waits[0].add(ind);
			}else{
				outDatas[ind] = -2; // to be computed
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
			//System.out.println("nombre de diffusions = "+indexG);
			
		}
		
		setResult(outDatas);
	}
	
	private void diffusionPaquet(int dd, List<Integer> wait){
		for(int p : wait){
			diffusion(p, dd/coeffReg);
		}
	}

	private void setPixelAndValue(int pixel, float dist) {
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
				
				int x = p%width;
				int y = p/width;
				
				// en haut à gauche
				np = p - width - 1;
				if(x > 0 && y > 0 && everDatas[np] != 1){
				//if(np >= 0 && x > 0 && y > 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d * cellSize < v) { // MAJ ?
							outDatas[np] = d * cellSize;
							setPixelAndValue(np, d);
						}
					}
				}
				// en haut
				np = p - width;
				if(y > 0 && everDatas[np] != 1){
				//if(np >= 0 && y > 0 && everDatas[np] != 1){
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
				// en haut à droite
				np = p - width + 1;
				if(x < (width-1) && y > 0 && everDatas[np] != 1){
				//if(np >= 0 && x < (width-1) && y > 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d * cellSize < v) { // MAJ ?
							outDatas[np] = d * cellSize;
							setPixelAndValue(np, d);
						}
					}
				}
				// à gauche
				np = p - 1;
				if(x > 0 && everDatas[np] != 1){
				//if(np >= 0 && x > 0 && everDatas[np] != 1){
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
				// à droite
				np = p + 1;
				if(x < (width-1) && everDatas[np] != 1){
				//if(np < width*height && x < (width-1) && everDatas[np] != 1){
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
				// en bas à gauche
				np = p + width - 1;
				if(x > 0 && y < (height-1) && everDatas[np] != 1){
				//if(np < width*height && x > 0 && y < (height-1) && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (sqrt2 / 2) * fd + (sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d * cellSize < v) { // MAJ ?
							outDatas[np] = d * cellSize;
							setPixelAndValue(np, d);
						}
					}
				}
				// en bas
				np = p + width;
				if(y < (height-1) && everDatas[np] != 1){
				//if(np < width*height && y < (height-1) && everDatas[np] != 1){
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
				// en bas à droite
				np = p + width + 1;
				if(x < (width-1) && y < (height-1) && everDatas[np] != 1){
				//if(np < width*height && x < (width-1) && y < (height-1) && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
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


	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
	}

}
