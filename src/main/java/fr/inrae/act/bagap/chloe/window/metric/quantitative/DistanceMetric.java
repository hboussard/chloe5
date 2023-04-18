package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class DistanceMetric extends Metric implements QuantitativeMetric {

	public DistanceMetric() {
		super("distance");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sum();
	}
	
}
