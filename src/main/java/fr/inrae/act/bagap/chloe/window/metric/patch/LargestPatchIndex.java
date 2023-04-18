package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class LargestPatchIndex extends Metric implements PatchMetric {

	public LargestPatchIndex() {
		super("LPI");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getMaxSurface();
	}

}
