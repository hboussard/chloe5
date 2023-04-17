package fr.inrae.act.bagap.chloe.metric.basic;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.value.ValueMetric;

public class RateValidValueMetric extends Metric implements ValueMetric {

	public RateValidValueMetric() {
		super("pN-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = (double) (co.validValues())/co.theoreticalSize();
	}

}
