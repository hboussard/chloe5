package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class PatchNumberClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public PatchNumberClassIndex(short cm) {
		super("NP-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getNbPatches(classMetric);
	}

}