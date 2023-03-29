package fr.inrae.act.bagap.chloe.metric.couple;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.value.ValueMetric;
import fr.inrae.act.bagap.chloe.util.Couple;

public class AggregationClassIndex extends Metric implements ValueMetric, CoupleMetric {

	private int classMetric;
	
	public AggregationClassIndex(short cm){
		super("AI-class_"+cm);
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = -1;
		if(co.countCouples() > 0){
			int ai = (int) co.countValue(classMetric);
			int n=1, n2;
			for(; ; n++){
				n2 = (int) Math.pow(n, 2);
				if(n2 == ai){
					break;
				}else if(n2 > ai){
					n--;
					n2 = (int) Math.pow(n, 2);
					break;
				}
			}
			int m = ai - n2;
			int G;
			if(m == 0){
				G = 2*n*(n-1);
			}else if(m <= n){
				G = 2*n*(n-1) + 2*m -1;
			}else{
				G = 2*n*(n-1) + 2*m -2;
			}
			double g = co.countCouple(Couple.getCouple(classMetric, classMetric));
			if(G != 0){
				value = (g / G * 100.0)*(co.countValue(classMetric)/co.validValues());
			}else{
				value = 0;
			}
		}
	}
	
}