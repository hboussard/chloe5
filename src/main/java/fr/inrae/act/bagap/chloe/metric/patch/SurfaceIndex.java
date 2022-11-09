package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class SurfaceIndex extends Metric implements PatchMetric {

	public SurfaceIndex() {
		super("AREA");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getTotalSurface();
	}

}