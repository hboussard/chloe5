package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ErosionEmpriseMetric extends Metric implements ErosionMetric {

	public ErosionEmpriseMetric() {
		super("erosionemprise");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.erosionEmprise();
	}
	
}
