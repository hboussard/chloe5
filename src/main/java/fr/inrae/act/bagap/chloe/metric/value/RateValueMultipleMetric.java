package fr.inrae.act.bagap.chloe.metric.value;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class RateValueMultipleMetric extends Metric implements ValueMetric {

	private short[] v;
	
	public RateValueMultipleMetric(short[] v) {
		super();
		StringBuffer name = new StringBuffer("pNVm_");
		for(short vv : v){
			name.append(vv);
			name.append('&');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = 0;
			for(short vv : v){
				value += co.countValue(vv);
			}
			if(value != 0){
				value /= co.validValues();
			}
		}
	}

}