package fr.inrae.act.bagap.chloe.metric;

public interface MetricObserver {

	void init(Metric m);
	
	void notify(Metric m, double value);
	
	void close(Metric m);
	
}
