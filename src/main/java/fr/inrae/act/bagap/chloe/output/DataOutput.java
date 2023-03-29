package fr.inrae.act.bagap.chloe.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;

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
	public void postrun(Counting c, int x, int y, Map<Metric, Double> values) {
		// stockage de la valeur
		for(PixelWithID rp : pixels){
			if(rp.x() == x && rp.y() == y){
				internalDatas.put(rp, new Double(values.get(metric)).floatValue());
				break;
			}
		}
		//internalDatas.put(new Pixel(x, y), new Double(values.get(metric)).floatValue());
		System.out.println(x+" "+y+" : "+new Double(values.get(metric)).floatValue());
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
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
