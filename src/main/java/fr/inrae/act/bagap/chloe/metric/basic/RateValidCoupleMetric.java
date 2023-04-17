package fr.inrae.act.bagap.chloe.metric.basic;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.couple.CoupleMetric;

public class RateValidCoupleMetric extends Metric implements CoupleMetric {

	public RateValidCoupleMetric() {
		super("pNC-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = (double)co.validCouples()/co.theoreticalCoupleSize();
	}

}
