package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class LargestPatchIndex extends Metric implements PatchMetric {

	public LargestPatchIndex(String name, String alias) {
		super(name, alias);
		//super("LPI");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.maxSurface();
		}
	}

}
