package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MaximumMetric extends Metric implements QuantitativeMetric {

	public MaximumMetric() {
		super("maximum");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.maximum();
		}
	}
	
}
