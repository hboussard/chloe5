package fr.inrae.act.bagap.chloe.window.metric.slope;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SlopeDirectionMetric extends Metric implements SlopeMetric {

	public SlopeDirectionMetric(String name, String alias) {
		super(name, alias);
		//super("slope-direction");
	}

	@Override
	protected void doCalculate(Counting co) {
		/*if(co.validValues() > 0){
			value = co.slope();
		}*/
		value = co.slopeDirection();
	}
	
}
