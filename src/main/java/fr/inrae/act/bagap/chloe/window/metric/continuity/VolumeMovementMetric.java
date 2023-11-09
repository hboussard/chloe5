package fr.inrae.act.bagap.chloe.window.metric.continuity;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class VolumeMovementMetric extends Metric implements ContinuityMetric {

	public VolumeMovementMetric() {
		super("volume");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.volume();
	}
	
}
