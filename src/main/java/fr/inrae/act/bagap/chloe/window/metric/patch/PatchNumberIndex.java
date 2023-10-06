package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class PatchNumberIndex extends Metric implements PatchMetric {

	public PatchNumberIndex() {
		super("NP");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.nbPatches();
		}
	}

}