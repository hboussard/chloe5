package fr.inrae.act.bagap.chloe.metric.couple;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class CountHeterogeneousCouple extends Metric implements CoupleMetric {

	public CountHeterogeneousCouple(){
		super("NC-hete");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countHeterogenousCouples();
		}
	}
	
}