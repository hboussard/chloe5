package fr.inrae.act.bagap.chloe.metric.basic;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.couple.CoupleMetric;

public class ValidCoupleMetric extends Metric implements CoupleMetric {

	public ValidCoupleMetric() {
		super("NC-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.validCouples();
	}

}
