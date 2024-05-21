package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MassCumulMetric extends Metric implements DegatErosionMetric {

	public MassCumulMetric() {
		super("mass-cumul");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.massCumul();
	}
	
}