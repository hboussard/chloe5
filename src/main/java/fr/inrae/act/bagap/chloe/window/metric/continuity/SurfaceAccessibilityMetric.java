package fr.inrae.act.bagap.chloe.window.metric.continuity;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SurfaceAccessibilityMetric extends Metric implements ContinuityMetric {

	public SurfaceAccessibilityMetric(String name, String alias) {
		super(name, alias);
		//super("surface");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.surface();
	}
	
}
