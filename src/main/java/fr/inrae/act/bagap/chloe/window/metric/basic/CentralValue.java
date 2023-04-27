package fr.inrae.act.bagap.chloe.window.metric.basic;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.value.ValueMetric;

public class CentralValue extends Metric implements ValueMetric {

	public CentralValue() {
		super("Central");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.centralValue();
	}

}
