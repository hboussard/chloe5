package fr.inrae.act.bagap.chloe.distance.analysis.slope;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import fr.inrae.act.bagap.apiland.analysis.Analysis;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.apiland.raster.Raster;

public class TabInverseAltitudeRCMDistanceAnalysis2 extends Analysis {

	private float[] outDatas, inDatas, frictionDatas, altitudeDatas;
	
	private int[] codes;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private boolean hasValue;
	
	private Map<Float, Set<Pixel>> waits;

	public TabInverseAltitudeRCMDistanceAnalysis2(float[] outDatas, float[] inDatas, float[] frictionDatas, float[] altitudeDatas, int width, int height, float cellSize, int noDataValue, int[] codes) {
		this(outDatas, inDatas, frictionDatas, altitudeDatas, width, height, cellSize, noDataValue, codes, noDataValue);
	}
	
	public TabInverseAltitudeRCMDistanceAnalysis2(float[] outDatas, float[] inDatas, float[] frictionDatas, float[] altitudeDatas, int width, int height, float cellSize, int noDataValue, int[] codes, int threshold) {
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.frictionDatas = frictionDatas;
		this.altitudeDatas = altitudeDatas;
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
		this.waits = new TreeMap<Float, Set<Pixel>>();
	}
	
	public boolean hasValue(){
		return hasValue;
	}
	
	@Override
	protected void doInit() {
		hasValue = false;
		boolean ok;
		float v;
		for (int yt = 0; yt < height; yt++) {
			for (int xt = 0; xt < width; xt++) {
				v = inDatas[yt*width+xt];
				ok = false;
				if (v != Raster.getNoDataValue()) {
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
					}
				}else{
					outDatas[yt * width + xt] = noDataValue; // nodata_value -> to be not computed
				}
				
			}
		}
		inDatas = null;
		
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
						setPixelAndValue(new Pixel(xt, yt), 0.0f);
					}
				}
				
			}
		}
	}

	@Override
	public void doRun() {

		// diffusion
		
		while (waits.size() > 0) {
			diffusionPaquet();
			
		}
		
		setResult(outDatas);
	}

	public void setPixelAndValue(Pixel pixel, float value) {
		value = (float) (Math.floor(value * 100.0)/100.0);
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	private void diffusionPaquet(){
		
		Iterator<Entry<Float, Set<Pixel>>> iteEntry = waits.entrySet().iterator();
		Entry<Float, Set<Pixel>> entry = iteEntry.next(); // r�cup�ration des pixels � diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // r�cup�ration de la valeur de diffusion 
			
			Iterator<Pixel> itePixel = entry.getValue().iterator();
			Pixel p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				itePixel.remove();
				
				diffusion(p, dd);
			}
		}
	}
	
	/*
	 * 1 2 3
	 * 4 0 5
	 * 6 7 8
	 */
	private int getDirection(Pixel p, Pixel np){
		if(p.x() == np.x()-1){
			if(p.y() == np.y()-1){
				return 8;
			}else if(p.y() == np.y()){
				return 5;
			}else if(p.y() == np.y()+1){
				return 3;
			}
		}else if(p.x() == np.x()){
			if(p.y() == np.y()-1){
				return 7;
			}else if(p.y() == np.y()+1){
				return 2;
			}
		}else if(p.x() == np.x()+1){
			if(p.y() == np.y()-1){
				return 6;
			}else if(p.y() == np.y()){
				return 4;
			}else if(p.y() == np.y()+1){
				return 1;
			}
		}
		// impossible --> p = np
		throw new IllegalArgumentException();
	}
	
	private void diffusion(Pixel p, double dd) {
		if (threshold > dd) { 
			double vd = outDatas[p.x()+p.y()*width];  // valeur au point de diffusion
			if (vd != Raster.getNoDataValue()) {
				double fd = frictionDatas[p.x()+p.y()*width]; // friction au point de diffusion
				double alt = altitudeDatas[p.x()+p.y()*width]; // altitude au point de diffusion
				Pixel np;
				float v, d;
				Iterator<Pixel> ite = null;
				//ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
				
				while (ite.hasNext()) {
					np = ite.next();
					
					double nalt = altitudeDatas[np.x()+np.y()*width];
					//System.out.println(p+" "+np);
					//System.out.println(dir+" "+ndir);
					if(alt <= nalt){
						//System.out.println("pass");
						if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
							v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
							if (v != Raster.getNoDataValue()) {
								float fc = frictionDatas[np.x()+np.y()*width];
								d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
								if (v == -2 || d < v) { // MAJ ?
									outDatas[np.x()+np.y()*width] = (float) d;
									
									//if(v != -2){
									//	waits.get(v).remove(np);
									//}
									setPixelAndValue(np, (float) d);
								}
							}
						}
					}
				}
				
				
				//ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					double nalt = altitudeDatas[np.x()+np.y()*width];
					//System.out.println(p+" "+np);
					//System.out.println(dir+" "+ndir);
					if(alt <= nalt){
						//System.out.println("pass");
						if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
							v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
							if (v != Raster.getNoDataValue()) {
								float fc = frictionDatas[np.x()+np.y()*width];
								d = (float) (dd + (cellSize * Math.sqrt(2) / 2) * fd + (cellSize * Math.sqrt(2) / 2) * fc); // distance au point diagonal
								if (v == -2 || d < v) { // MAJ ?
									outDatas[np.x()+np.y()*width] = (float) d;
									
									//if(v != -2){
									//	waits.get(v).remove(np);
									//}
									setPixelAndValue(np, (float) d);
								}
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
		altitudeDatas = null;
	}

}
