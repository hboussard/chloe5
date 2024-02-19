package fr.inrae.act.bagap.chloe.window.counting;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public interface CountingObserver {

	void init(Counting c, Set<Metric> metrics);
	
	void prerun(Counting c);
	
	//void postrun(Counting c, int i, int j, Map<Metric, Double> values);
	
	//void postrun(Counting c, int id, Map<Metric, Double> values);
	
	void postrun(Counting c, int i, int j, Set<Metric> metrics);
	
	void postrun(Counting c, Pixel pixel, Set<Metric> metrics);
	
	void postrun(Counting c, int id, Set<Metric> metrics);
	
	void close(Counting c, Set<Metric> metrics);
	
}
