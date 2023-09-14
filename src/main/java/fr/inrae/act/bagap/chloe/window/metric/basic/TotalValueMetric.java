package fr.inrae.act.bagap.chloe.window.metric.basic;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class TotalValueMetric extends Metric implements BasicMetric {

	public TotalValueMetric() {
		super("N-total");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.totalValues();
	}

}
