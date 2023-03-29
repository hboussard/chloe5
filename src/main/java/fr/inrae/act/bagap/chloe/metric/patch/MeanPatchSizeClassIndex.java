package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class MeanPatchSizeClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public MeanPatchSizeClassIndex(short cm) {
		super("MPS-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getTotalSurface(classMetric) / co.getNbPatches(classMetric);
	}

}
