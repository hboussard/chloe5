package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MinCoverMetric extends Metric implements ValueMetric {

	public MinCoverMetric() {
		super("minCover");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){

			value = -1;
			
			for(int v : co.values()){
				
				if(value == -1) {
					
					value = co.countValue(v);
					
				}else {
					
					value = Math.min(value, co.countValue(v));	
				}
			}
		}
		
		if(value == -1) {
			value = 0;
		}
	}

}
