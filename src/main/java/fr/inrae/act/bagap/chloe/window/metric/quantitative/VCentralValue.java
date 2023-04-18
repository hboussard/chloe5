package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class VCentralValue extends Metric implements QuantitativeMetric {

	public VCentralValue() {
		super("vCentral");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.centralValue();
	}

}
