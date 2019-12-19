package fr.inra.sad.bagap.chloe.metric.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class ShannonDiversityIndex extends Metric {

	public ShannonDiversityIndex() {
		super(VariableManager.get("SHDI"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			float p; 
			for(short v : co.values()){
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
