package fr.inrae.act.bagap.chloe.window.metric.grainbocager;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.QuantitativeMetric;

public class GrainBocagerBocageMetric extends Metric implements QuantitativeMetric {

	public GrainBocagerBocageMetric() {
		super("GBBocage");
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.sum();
		if(value == 0){
			return;
		}
		value /= co.validValues();
		
		if(value > 0.75){ // version 2025
		//if(value > 0.65){ // version 2026
			
			value = 5; // 5 fois la hauteur des arbres en forets
			
		}else if(value < 0.20){ // version 2025
		//}else if(value < 0.15){ // version 2026
			
			value = 1; // 1 fois la hauteur des arbres isoles
			
		}else{
			
			value = 10; // 10 fois la hauteur des arbres en haie 
		}
	}
	
}
