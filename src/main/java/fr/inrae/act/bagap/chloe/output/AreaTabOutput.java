package fr.inrae.act.bagap.chloe.output;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.raster.Coverage;

public class AreaTabOutput implements CountingObserver{

	private final float[] datas;
	
	private final Metric metric;
	
	private final Coverage areaCoverage;
	
	private final int width, height, noDataValue;
	
	private Map<Integer, Float> internalDatas;
	
	public AreaTabOutput(float[] datas, Coverage areaCoverage, Metric metric, int width, int height, int noDataValue){
		this.datas = datas;
		this.areaCoverage = areaCoverage;
		this.metric = metric;
		this.width = width;
		this.height = height;
		this.noDataValue = noDataValue;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		internalDatas = new HashMap<Integer, Float>();
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int x, int y, Map<Metric, Double> values) {
		// do nothing
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// stockage de la valeur
		internalDatas.put(id, new Double(values.get(metric)).floatValue());
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		Rectangle roi = new Rectangle(0, 0, width, height);
		float[] areaDatas = areaCoverage.getDatas(roi);
		
		int index = 0;
		for(float ad : areaDatas){
			if(ad == noDataValue || ad == 0){
				datas[index] = ad;
			}else{
				if(!internalDatas.containsKey((int) ad)){
					datas[index] = 0;
				}else{
					datas[index] = internalDatas.get((int) ad);
				}
			}
			
			index++;
		}
	}
	
}
