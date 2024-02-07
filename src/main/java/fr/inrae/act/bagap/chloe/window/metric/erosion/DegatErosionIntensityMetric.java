package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class DegatErosionIntensityMetric extends Metric implements DegatErosionMetric {

	public DegatErosionIntensityMetric() {
		super("degaterosionintensity");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.degatErosionIntensity();
	}
	
}