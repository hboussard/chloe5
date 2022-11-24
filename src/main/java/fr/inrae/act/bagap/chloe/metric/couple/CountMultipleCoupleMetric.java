package fr.inrae.act.bagap.chloe.metric.couple;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.util.Couple;

public class CountMultipleCoupleMetric extends Metric implements CoupleMetric {

	private float[] c;
	
	public CountMultipleCoupleMetric(short[] v1, short[] v2) {
		super();
		c = new float[v1.length * v2.length];
		int index = 0;
		StringBuffer name = new StringBuffer("NMC_");
		for(short vv1 : v1){
			for(short vv2 : v2){
				c[index++] = Couple.getCouple(vv1, vv2);
				if(vv1 < vv2) {
					name.append(vv1+"-"+vv2);
					name.append('&');
					
				}else{
					name.append(vv2+"-"+vv1);
					name.append('&');
				}
			}
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = 0;
			for(float cc : c){
				value += co.countCouple(cc);
			}
		}
	}
}
