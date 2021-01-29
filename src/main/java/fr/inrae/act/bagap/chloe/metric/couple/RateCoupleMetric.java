package fr.inrae.act.bagap.chloe.metric.couple;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.util.Couple;

public class RateCoupleMetric extends Metric implements CoupleMetric {

	private float c;
	
	public RateCoupleMetric(float c) {
		super("pNC_"+Couple.getOne(c)+"-"+Couple.getOther(c));
		this.c = c;
	}
	
	public RateCoupleMetric(short v1, short v2) {
		super();
		if(v1 < v2) {
			this.setName("pNC_"+v1+"-"+v2);
		}else{
			this.setName("pNC_"+v2+"-"+v1);
		}
		this.c = Couple.getCouple(v1, v2);
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countCouple(c) / co.validCouples();
		}
	}
}
