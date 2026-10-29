package fr.inrae.act.bagap.chloe.window.metric.quantitative;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class AverageMetric extends Metric implements QuantitativeMetric {

	public AverageMetric(String name, String alias) {
		super(name, alias);
		//super("average");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.average();
		}
	}
	
}
