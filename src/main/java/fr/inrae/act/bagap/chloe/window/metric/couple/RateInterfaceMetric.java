package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateInterfaceMetric extends Metric implements CoupleMetric {

	private short v;
	
	public RateInterfaceMetric(short v) {
		super("pInterface_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			
			if(co.countCouples() > 0) {
				
				value = co.countInterface(v) / co.countCouples();	
				
			}else {
				
				value = 0;
			}
		}
	}
	
}
