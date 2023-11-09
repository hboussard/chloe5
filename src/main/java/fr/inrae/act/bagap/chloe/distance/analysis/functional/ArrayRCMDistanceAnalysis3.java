package fr.inrae.act.bagap.chloe.distance.analysis.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ArrayRCMDistanceAnalysis3 extends Analysis {

	private int indexG = 0;
	
	private  static final float sqrt2 = (float) Math.sqrt(2);
	
	private float[] outDatas, frictionDatas, everDatas;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private Map<Float, List<Integer>> waits;

	public ArrayRCMDistanceAnalysis3(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, Map<Float, List<Integer>> waits) {
		this(outDatas, frictionDatas, width, height, cellSize, noDataValue, waits, Raster.getNoDataValue());
	}

	public ArrayRCMDistanceAnalysis3(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, Map<Float, List<Integer>> waits, double threshold) {
		this.outDatas = outDatas;
		this.frictionDatas = frictionDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.waits = waits;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}
	
	@Override
	protected void doInit() {

		everDatas = new float[outDatas.length];
	}

	@Override
	public void doRun() {

		// diffusion
		
		while (waits.size() > 0) {
			diffusionPaquet();
			
		}
		//System.out.println("nombre de diffusions = "+indexG);
		setResult(outDatas);
	}

	private void setPixelAndValue(Map<Float, List<Integer>> waits, int pixel, float value) {
		value = (float) (Math.floor(value * 100.0)/100.0);
		if (!waits.containsKey(value)) {
			waits.put(value, new ArrayList<Integer>());
		}
		waits.get(value).add(pixel);
	}

	private void diffusionPaquet(){
		
		Iterator<Entry<Float, List<Integer>>> iteEntry = waits.entrySet().iterator();
		Entry<Float, List<Integer>> entry = iteEntry.next(); // récupération des pixels à diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // récupération de la valeur de diffusion 
			
			Iterator<Integer> itePixel = entry.getValue().iterator();
			int p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				//itePixel.remove();
				
				diffusion(p, dd);
			}
		}
	}
	
	private void diffusion(int p, double dd) {
		++indexG;
		
		if (everDatas[p] != 1 && threshold > dd) {
			
			everDatas[p] = 1;
			
			double vd = outDatas[p];  // valeur au point de diffusion
			if (vd != noDataValue) {
				float fd = frictionDatas[p]; // friction au point de diffusion
				int np;
				float v, fc, d;
				// en haut à gauche
				np = p - width - 1;
				if(np >= 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize * sqrt2 / 2) * fd + (cellSize * sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// en haut
				np = p - width;
				if(np >= 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// en haut à droite
				np = p - width + 1;
				if(np >= 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize * sqrt2 / 2) * fd + (cellSize * sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// à gauche
				np = p - 1;
				if(np >= 0 && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// à droite
				np = p + 1;
				if(np < width*height && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// en bas à gauche
				np = p + width - 1;
				if(np < width*height && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize * sqrt2 / 2) * fd + (cellSize * sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// en bas
				np = p + width;
				if(np < width*height && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
						}
					}
				}
				// en bas à droite
				np = p + width + 1;
				if(np < width*height && everDatas[np] != 1){
					v = outDatas[np]; // valeur au point cardinal
					if (v != noDataValue) {
						fc = frictionDatas[np];
						d = (float) (dd + (cellSize * sqrt2 / 2) * fd + (cellSize * sqrt2 / 2) * fc); // distance au point diagonal
						if (v == -2 || d < v) { // MAJ ?
							outDatas[np] = d;
							/*
							if(v != -2){
								float value = (float) (Math.floor(v * 100.0)/100.0);
								waits.get(value).remove(np);
							}
							*/
							setPixelAndValue(waits, np, d);
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
