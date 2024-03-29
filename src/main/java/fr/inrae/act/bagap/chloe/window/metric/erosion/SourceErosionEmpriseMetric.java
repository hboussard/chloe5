package fr.inrae.act.bagap.chloe.window.metric.erosion;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SourceErosionEmpriseMetric extends Metric implements SourceErosionMetric {

	public SourceErosionEmpriseMetric() {
		super("source-erosion-emprise");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sourceErosionEmprise();
	}
	
}
