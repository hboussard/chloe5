package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class DegatErosionEmpriseMetric extends Metric implements DegatErosionMetric {

	public DegatErosionEmpriseMetric() {
		super("degat-erosion-emprise");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.degatErosionEmprise();
	}
	
}
