package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class DistanceMetric extends Metric implements QuantitativeMetric {

	public DistanceMetric() {
		super("distance");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sum();
	}
	
}
