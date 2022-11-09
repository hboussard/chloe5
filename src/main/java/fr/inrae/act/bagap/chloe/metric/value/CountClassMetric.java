package fr.inrae.act.bagap.chloe.metric.value;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class CountClassMetric extends Metric implements ValueMetric {

	public CountClassMetric() {
		super("Nclass");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.countClass();
		}
	}

}
