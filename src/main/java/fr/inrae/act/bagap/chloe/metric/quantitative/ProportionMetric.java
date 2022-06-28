package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

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
