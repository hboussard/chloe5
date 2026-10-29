package fr.inrae.act.bagap.chloe.window.metric.basic;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.couple.CoupleMetric;

public class ValidCoupleMetric extends Metric implements CoupleMetric {

	public ValidCoupleMetric(String name, String alias) {
		super(name, alias);
		//super("NC-valid");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.validCouples();
	}

}
