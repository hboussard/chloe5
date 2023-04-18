package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MeanDistanceMetric extends Metric implements QuantitativeMetric {

	public MeanDistanceMetric() {
		super("MD");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){	
			value = co.average() / 100.0;
		}
	}
	
}
