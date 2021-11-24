package fr.inrae.act.bagap.chloe.counting;

import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.metric.Metric;

public interface CountingObserver {

	void init(Counting c, Set<Metric> metrics);
	
	void prerun(Counting c);
	
	void postrun(Counting c, int i, int j, Map<Metric, Double> values);
	
	void postrun(Counting c, int id, Map<Metric, Double> values);
	
	void close(Counting c, Set<Metric> metrics);
	
}
