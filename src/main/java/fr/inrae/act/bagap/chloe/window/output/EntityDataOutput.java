package fr.inrae.act.bagap.chloe.window.output;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EntityDataOutput implements CountingObserver{

	private Map<Integer, Map<String, Double>> entityData;
	
	public EntityDataOutput(Map<Integer, Map<String, Double>> entityData) {
		this.entityData = entityData;
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
	public void postrun(Counting c, int x, int y, Set<Metric> metrics) {
		// do nothing
	}
	
	@Override
	public void postrun(Counting c, Pixel p, Set<Metric> metrics) {
		// do nothing;
	}

	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		entityData.put(id, new TreeMap<String, Double>());
		for(Metric m : metrics){
			entityData.get(id).put(m.getName(), m.value());
		}
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing
	}

}
