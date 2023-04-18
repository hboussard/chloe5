package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class CountCoupleMetric extends Metric implements CoupleMetric {

	private float c;
	
	public CountCoupleMetric(float c) {
		super("NC_"+Couple.getOne(c)+"-"+Couple.getOther(c));
		this.c = c;
	}
	
	public CountCoupleMetric(short v1, short v2) {
		super();
		if(v1 < v2) {
			this.setName("NC_"+v1+"-"+v2);
		}else{
			this.setName("NC_"+v2+"-"+v1);
		}
		this.c = Couple.getCouple(v1, v2);
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countCouple(c);
		}
	}
}
