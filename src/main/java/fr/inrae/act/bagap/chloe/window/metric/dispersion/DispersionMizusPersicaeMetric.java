package fr.inrae.act.bagap.chloe.window.metric.dispersion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class DispersionMizusPersicaeMetric extends Metric implements DispersalMetric {

	public DispersionMizusPersicaeMetric() {
		super("dispersionMizusPersicae");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.volume();
	}
	
}
