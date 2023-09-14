package fr.inrae.act.bagap.chloe.window.metric.basic;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateValidValueMetric extends Metric implements BasicMetric {

	public RateValidValueMetric() {
		super("pN-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		
		value = (double) (co.validValues())/co.theoreticalSize();
	}

}
