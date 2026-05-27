package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class CountInterfaceMetric extends Metric implements CoupleMetric {

	private short v;
	
	public CountInterfaceMetric(short v) {
		super("Interface_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countInterface(v);
		}
	}
	
}
