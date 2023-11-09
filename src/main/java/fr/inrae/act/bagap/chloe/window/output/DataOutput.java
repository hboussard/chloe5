package fr.inrae.act.bagap.chloe.window.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;

public class DataOutput implements CountingObserver{
	
	private Set<PixelWithID> pixels;
	
	private Map<PixelWithID, Float> internalDatas;
	
	private final Metric metric;
	
	public DataOutput(String metric, Set<PixelWithID> pixels){
		this(MetricManager.get(metric), pixels);
	}
	
	public DataOutput(Metric metric, Set<PixelWithID> pixels){
		this.pixels = pixels;
		this.internalDatas = new HashMap<PixelWithID, Float>();
		this.metric = metric;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void prerun(Counting c) {
		
	}

	@Override
	public void postrun(Counting c, int x, int y, Set<Metric> metrics) {
		// stockage de la valeur
		for(PixelWithID rp : pixels){
			if(rp.x() == x && rp.y() == y){
				internalDatas.put(rp, Float.parseFloat(Util.format(metric.value())));
				break;
			}
		}
		//internalDatas.put(new Pixel(x, y), new Double(values.get(metric)).floatValue());
		//System.out.println(x+" "+y+" : "+new Double(values.get(metric)).floatValue());
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		// do nothing
	}
	
	public Map<PixelWithID, Float> getDatas(){
		return internalDatas;
	}
	
}
