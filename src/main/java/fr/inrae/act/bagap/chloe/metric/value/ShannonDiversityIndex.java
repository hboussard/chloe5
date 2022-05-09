package fr.inrae.act.bagap.chloe.metric.value;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class ShannonDiversityIndex extends Metric implements ValueMetric {

	public ShannonDiversityIndex() {
		super("SHDI");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			double p; 
			for(int v : co.values()){
				//System.out.println(v+" "+co.countValue(v));
				p = co.countValue(v) / co.validValues();
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
			//System.out.println(value);
		}
	}

}
