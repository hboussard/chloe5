package fr.inrae.act.bagap.chloe.metric.quantitative;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class BocageMetric extends Metric implements QuantitativeMetric {

	public BocageMetric() {
		super("bocage");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sum();
		if(value == 0){
			return;
		}
		value /= co.validValues();
		
		if(value > 0.75){
			value = 5; // 5 fois la hauteur des arbres en forets
		}else if(value < 0.20){
			value = 1; // 1 fois la hauteur des arbres isoles
		}else{
			value = 10; // 10 fois la hauteur des arbres en haie 
		}
	}
	
}
