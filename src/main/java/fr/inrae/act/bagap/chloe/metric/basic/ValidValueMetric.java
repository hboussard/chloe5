package fr.inrae.act.bagap.chloe.metric.basic;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.value.ValueMetric;

public class ValidValueMetric extends Metric implements ValueMetric {

	public ValidValueMetric() {
		super("N-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.validValues();
	}
	
	

}
