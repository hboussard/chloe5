package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SimpsonDiversityIndex extends Metric implements ValueMetric {

	public SimpsonDiversityIndex() {
		super("SIDI");
	}

	@Override
	protected void doCalculate(Counting co) {
		
		if(co.countValues() > 0){
			value = 0;
			double p;
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				value += p*p;
			}
			value = 1 - value;
		}
	}

}
