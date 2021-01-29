package fr.inrae.act.bagap.chloe.metric.couple;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.util.Couple;

public class HeterogeneityFragmentationIndex extends Metric implements CoupleMetric {

	public HeterogeneityFragmentationIndex() {
		super("HET-frag");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			double p; 
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
