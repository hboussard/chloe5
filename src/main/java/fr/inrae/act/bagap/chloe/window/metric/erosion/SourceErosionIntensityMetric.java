package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SourceErosionIntensityMetric extends Metric implements SourceErosionMetric {

	public SourceErosionIntensityMetric() {
		super("source-erosion-intensity");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sourceErosionIntensity();
	}
	
}
