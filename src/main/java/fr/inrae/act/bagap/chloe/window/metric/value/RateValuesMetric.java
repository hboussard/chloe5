package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateValuesMetric extends Metric implements ValueMetric {

	public RateValuesMetric() {
		super("pN-values");
	}
	
	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			
			//System.out.println(co.countValues()+" "+co.validValues());
			
			value = co.countValues()/co.validValues();
		}
	}

}