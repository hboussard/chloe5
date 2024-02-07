package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ErosionIntensityMetric extends Metric implements ErosionMetric {

	public ErosionIntensityMetric() {
		super("erosionintensity");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.erosionIntensity();
	}
	
}
