package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class LargestPatchClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public LargestPatchClassIndex(short cm) {
		super("LPI-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.maxSurface(classMetric);
		}
	}

}
