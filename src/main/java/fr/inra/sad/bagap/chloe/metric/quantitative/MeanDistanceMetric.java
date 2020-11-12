package fr.inra.sad.bagap.chloe.metric.quantitative;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class MeanDistanceMetric extends Metric {

	public MeanDistanceMetric() {
		super(VariableManager.get("MD"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.average();
		}
	}
	

}
