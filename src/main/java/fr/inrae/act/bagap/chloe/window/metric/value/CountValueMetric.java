package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class CountValueMetric extends Metric implements ValueMetric {

	private short v;
	
	public CountValueMetric(String name, String alias, short v) {
		super(name, alias);
		//super("NV_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.countValue(v);
		}
	}

}
