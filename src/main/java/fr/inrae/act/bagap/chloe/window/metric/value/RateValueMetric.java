package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateValueMetric extends Metric implements ValueMetric {

	private short v;
	
	public RateValueMetric(short v) {
		super("pNV_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = 0;
		if(co.validValues() > 0){
			value = co.countValue(v) / co.validValues();
		}
	}

}
