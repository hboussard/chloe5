package fr.inrae.act.bagap.chloe.distance.analysis.functional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class TabRCMDistanceAnalysis2 extends Analysis {

	//private int indexG = 0;
	
	private float[] outDatas, inDatas, frictionDatas;
	
	private int[] codes;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private int noDataValue;
	
	private boolean hasValue;
	
	private Map<Float, Set<Pixel>> waits;

	public TabRCMDistanceAnalysis2(float[] outDatas, float[] inDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, int[] codes) {
		this(outDatas, inDatas, frictionDatas, width, height, cellSize, noDataValue, codes, noDataValue);
	}
	
	public TabRCMDistanceAnalysis2(float[] outDatas, float[] inDatas, float[] frictionDatas, int width, int height, float cellSize, int noDataValue, int[] codes, int threshold) {
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.frictionDatas = frictionDatas;
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
					}
				}else{
					outDatas[yt * width + xt] = noDataValue; // nodata_value -> to be not computed
				}
				
			}
		}
		inDatas = null;
		
		// afin de limiter le nombre de calculs de diffusion, ne diffuser qu'à partir des bords d'habitats
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
		//System.out.println(indexG);
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
		Entry<Float, Set<Pixel>> entry = iteEntry.next(); // c des pixels à diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // récupération de la valeur de diffusion 
			
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
		//System.out.println("diffusion au point "+p+" "+dd+" "+(++indexG));
		//++indexG;
		//if(indexG%1000000==0){System.out.println(indexG);};
		
		if (threshold > dd) { 
			double vd = outDatas[p.x()+p.y()*width];  // valeur au point de diffusion
			if (vd != noDataValue) {
				double fd = frictionDatas[p.x()+p.y()*width]; // friction au point de diffusion
				
				Pixel np;
				float v, d;
				Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
						if (v != noDataValue) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = (float) d;
								
								if(v != -2){
									float value = (float) (Math.floor(v * 100.0)/100.0);
									waits.get(value).remove(np);
								}
								setPixelAndValue(np, (float) d);
							}
						}
					}
				}
				ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point diagonal
						if (v != noDataValue) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize * Math.sqrt(2) / 2) * fd + (cellSize * Math.sqrt(2) / 2) * fc); // distance au point diagonal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = (float) d;
								
								if(v != -2){
									float value = (float) (Math.floor(v * 100.0)/100.0);
									waits.get(value).remove(np);
								}
								setPixelAndValue(np, (float) d);
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
	}

}
