package fr.inrae.act.bagap.chloe.window.metric.slope;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SlopeIntensityMetric extends Metric implements SlopeMetric {

	public SlopeIntensityMetric() {
		super("slope-intensity");
	}

	@Override
	protected void doCalculate(Counting co) {
		/*if(co.validValues() > 0){
			value = co.slope();
		}*/
		value = co.slopeIntensity();
	}
	
}
