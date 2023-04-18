package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ShannonEvennessIndex extends Metric implements ValueMetric {

	public ShannonEvennessIndex() {
		super("SHEI");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			double p; 
			for(int v : co.values()){
				p = co.countValue(v) / co.validValues();
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
			value /= Math.log(co.values().length);
		}
	}

}
