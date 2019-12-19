package fr.inra.sad.bagap.chloe;

public interface MetricObserver {

	void notify(Metric m, String metric, float value);
	
	void close();
	
}
