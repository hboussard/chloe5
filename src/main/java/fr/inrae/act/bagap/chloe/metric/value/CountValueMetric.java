package fr.inrae.act.bagap.chloe.metric.value;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class CountValueMetric extends Metric implements ValueMetric {

	private short v;
	
	public CountValueMetric(short v) {
		super("NV_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.countValue(v);
		}
	}

}
