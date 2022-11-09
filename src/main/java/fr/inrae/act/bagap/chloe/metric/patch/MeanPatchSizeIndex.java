package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class MeanPatchSizeIndex extends Metric implements PatchMetric {

	public MeanPatchSizeIndex() {
		super("MPS");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getTotalSurface() / co.getNbPatches();
	}

}
