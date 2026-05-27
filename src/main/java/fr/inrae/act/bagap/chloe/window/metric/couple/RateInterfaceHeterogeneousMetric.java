package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateInterfaceHeterogeneousMetric extends Metric implements CoupleMetric {

	private short v;
	
	public RateInterfaceHeterogeneousMetric(short v) {
		super("pInterfaceHete_"+v);
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			if(co.countHeterogenousCouples() > 0) {
				
				value = co.countInterface(v) / co.countHeterogenousCouples();
			
			}else {
				
				value = 0;
			}
		}
	}
	
}
