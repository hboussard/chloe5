package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

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
