package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class MinimumMetric extends Metric implements QuantitativeMetric {

	public MinimumMetric() {
		super("minimum");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.minimum();
		}
	}
	
}
