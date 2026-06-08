package fr.inrae.act.bagap.chloe.window.metric.dispersion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class DispersionPterostichusMelanariusMetric extends Metric implements DispersalMetric {

	public DispersionPterostichusMelanariusMetric() {
		super("dispersionPterostichusMelanarius");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.volume();
	}
	
}
