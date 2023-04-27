package fr.inrae.act.bagap.chloe.window.metric.grainbocager;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.QuantitativeMetric;

public class GrainBocagerMeanDistanceMetric extends Metric implements QuantitativeMetric {

	public GrainBocagerMeanDistanceMetric() {
		super("MD");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){	
			value = co.average() / 100.0;
		}
	}
	
}
