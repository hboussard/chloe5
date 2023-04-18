package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateHeterogeneousCouple extends Metric implements CoupleMetric {

	public RateHeterogeneousCouple(){
		super("pNC-hete");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countHeterogenousCouples() / co.validCouples();
		}
	}
	
}
