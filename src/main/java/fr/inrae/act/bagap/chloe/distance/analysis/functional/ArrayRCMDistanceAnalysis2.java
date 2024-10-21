package fr.inrae.act.bagap.chloe.distance.analysis.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.inrae.act.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.apiland.raster.Raster;

public class ArrayRCMDistanceAnalysis2 extends Analysis {

	private int indexG = 0;
	
	private float[] outDatas, frictionDatas;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private Map<Float, List<Pixel>> waits;

	public ArrayRCMDistanceAnalysis2(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, Map<Float, List<Pixel>> waits) {
		this(outDatas, frictionDatas, width, height, cellSize, noDataValue, waits, Raster.getNoDataValue());
	}

	public ArrayRCMDistanceAnalysis2(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, Map<Float, List<Pixel>> waits, double threshold) {
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
		// TODO Auto-generated method stub	
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

	private void setPixelAndValue(Map<Float, List<Pixel>> waits, Pixel pixel, float value) {
		value = (float) (Math.floor(value * 100.0)/100.0);
		if (!waits.containsKey(value)) {
			waits.put(value, new ArrayList<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	private void diffusionPaquet(){
		
		Iterator<Entry<Float, List<Pixel>>> iteEntry = waits.entrySet().iterator();
		Entry<Float, List<Pixel>> entry = iteEntry.next(); // r�cup�ration des pixels � diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // r�cup�ration de la valeur de diffusion 
			
			Iterator<Pixel> itePixel = entry.getValue().iterator();
			Pixel p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				//itePixel.remove();
				
				diffusion(p, dd);
			}
		}
	}
	
	private void diffusion(Pixel p, double dd) {
		++indexG;
		if (threshold > dd) { 
			double vd = outDatas[p.x()+p.y()*width];  // valeur au point de diffusion
			if (vd != noDataValue) {
				double fd = frictionDatas[p.x()+p.y()*width]; // friction au point de diffusion
				
				Pixel np;
				float v, d;
				Iterator<Pixel> ite = null;
				//ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
						if (v != noDataValue) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = d;
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
				//ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
						if (v != noDataValue) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize * Math.sqrt(2) / 2) * fd + (cellSize * Math.sqrt(2) / 2) * fc); // distance au point diagonal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = d;
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
	}


	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
	}

}
