package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class PatchNumberClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public PatchNumberClassIndex(short cm) {
		super("NP-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.nbPatches(classMetric);
		}
	}

}