package fr.inrae.act.bagap.chloe.window.metric.grainbocager;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.QuantitativeMetric;

public class GrainBocagerDistanceMetric extends Metric implements QuantitativeMetric {

	public GrainBocagerDistanceMetric() {
		super("GBDistance");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.minimum();
	}
	
}
