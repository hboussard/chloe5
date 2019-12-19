package fr.inra.sad.bagap.chloe.output;

import fr.inra.sad.bagap.chloe.metric.Metric;

public interface MetricObserver {

	//void init();
	
	void notify(Metric m, String metric, float value);
	
	void close();
	
}
