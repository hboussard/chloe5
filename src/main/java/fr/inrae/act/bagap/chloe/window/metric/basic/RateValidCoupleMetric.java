package fr.inrae.act.bagap.chloe.window.metric.basic;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.couple.CoupleMetric;

public class RateValidCoupleMetric extends Metric implements CoupleMetric {

	public RateValidCoupleMetric() {
		super("pNC-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = (double)co.validCouples()/co.theoreticalCoupleSize();
	}

}
