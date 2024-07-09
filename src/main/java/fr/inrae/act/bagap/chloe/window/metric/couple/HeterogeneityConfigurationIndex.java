package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class HeterogeneityConfigurationIndex extends Metric implements CoupleMetric {

	public HeterogeneityConfigurationIndex(){
		super("HET-config");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = 100.0*(co.countHeterogenousCouples() / co.validCouples()) / co.resolution();
		}
	}
	
}