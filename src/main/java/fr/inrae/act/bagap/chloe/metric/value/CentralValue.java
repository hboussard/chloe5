package fr.inrae.act.bagap.chloe.metric.value;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class CentralValue extends Metric implements ValueMetric {

	public CentralValue() {
		super("Central");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.centralValue();
	}

}
