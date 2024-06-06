package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ShannonDiversityIndex2 extends Metric implements ValueMetric {

	public ShannonDiversityIndex2() {
		super("SHDI2");
	}

	@Override
	protected void doCalculate(Counting co) {
		
		if(co.validValues() > 0){
			value = 0;
			
			double p; 
			for(int v : co.values()){
					
				if(co.countValue(v) > 0 ) {
					p = co.countValue(v) / co.countValues();
					if(p > 1){
						p = 1;
					}
					value += p*Math.log(p);
				}	
			}
			if(value > 0 ) {
				value = 0;
			}
			if(value != 0){
				value *= -1;
				
				if(co.countValues() > 0) {
					value *= (co.countValues()/co.validValues());	
				}
			}
			//System.out.println(value);
		}
	}

}
