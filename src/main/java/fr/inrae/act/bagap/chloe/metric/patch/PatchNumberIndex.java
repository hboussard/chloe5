package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class PatchNumberIndex extends Metric implements PatchMetric {

	public PatchNumberIndex() {
		super("NP");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getNbPatches();
	}

}