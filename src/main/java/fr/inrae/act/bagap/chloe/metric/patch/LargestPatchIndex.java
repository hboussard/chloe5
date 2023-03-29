package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class LargestPatchIndex extends Metric implements PatchMetric {

	public LargestPatchIndex() {
		super("LPI");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getMaxSurface();
	}

}
