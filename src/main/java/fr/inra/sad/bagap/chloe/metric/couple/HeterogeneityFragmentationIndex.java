package fr.inra.sad.bagap.chloe.metric.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.util.Couple;

public class HeterogeneityFragmentationIndex extends Metric {

	public HeterogeneityFragmentationIndex() {
		super(VariableManager.get("HET-frag"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			float p; 
			for(float c : co.couples()){
				if(!Couple.isHomogeneous(c)) {
					p = co.countCouple(c) / co.validCouples();
					if(p != 0){
						value += p*Math.log(p);
					}
				}
			}
			if(value != 0){
				value *= -1;
			}
			//System.out.println(value);
		}
	}

}
