package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class LargestPatchClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public LargestPatchClassIndex(short cm) {
		super("LPI-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getMaxSurface(classMetric);
	}

}
