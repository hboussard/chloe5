package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class VCentralValue extends Metric implements QuantitativeMetric {

	public VCentralValue() {
		super("vCentral");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.centralValue();
	}

}
