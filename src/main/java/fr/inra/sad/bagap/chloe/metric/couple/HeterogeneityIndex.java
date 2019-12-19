package fr.inra.sad.bagap.chloe.metric.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class HeterogeneityIndex extends Metric {

	public HeterogeneityIndex() {
		super(VariableManager.get("HET"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			float p; 
			for(float c : co.couples()){
				p = co.countCouple(c) / co.validCouples();
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
