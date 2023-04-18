package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class CentralValue extends Metric implements ValueMetric {

	public CentralValue() {
		super("Central");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.centralValue();
	}

}
