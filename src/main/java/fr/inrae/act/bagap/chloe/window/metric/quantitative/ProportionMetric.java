package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ProportionMetric extends Metric implements QuantitativeMetric {

	public ProportionMetric() {
		super("prop");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sum();
		if(value == 0){
			return;
		}
		value /= co.validValues();
	}
	
}
