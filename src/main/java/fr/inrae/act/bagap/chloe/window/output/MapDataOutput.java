package fr.inrae.act.bagap.chloe.window.output;

import java.util.Set;
import java.util.Map;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MapDataOutput implements CountingObserver{
	
	private Map<String, Double> mapData;
	
	public MapDataOutput(Map<String, Double> mapData){
		this.mapData = mapData;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, Pixel p, Set<Metric> metrics) {
		// do nothing;
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		
		for(Metric m : metrics){
			mapData.put(m.getName(), m.value());
		}
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing;
	}
	
}
