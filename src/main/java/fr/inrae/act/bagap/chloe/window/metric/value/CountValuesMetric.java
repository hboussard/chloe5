package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class CountValuesMetric extends Metric implements ValueMetric {

	public CountValuesMetric(String name, String alias) {
		super(name, alias);
		//super("N-values");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.countValues();
	}

}
