package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.value.ValueMetric;

public class AggregationIndexMultiple2 extends Metric implements ValueMetric, CoupleMetric {
	
	private short[] v;
	
	public AggregationIndexMultiple2(short[] v){
		super();
		StringBuffer name = new StringBuffer("AI2m_");
		for(short vv : v){
			name.append(vv);
			name.append('&');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			int ai = 0;
			for(short classMetric : v){
				ai += (int) co.countValue(classMetric);
			}
					
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
			
			double g = 0;
			for(short classMetric1 : v){
				for(short classMetric2 : v){
					if(classMetric1 <= classMetric2){
						g += co.countCouple(Couple.getCouple(classMetric1, classMetric2));	
					}
				}
			}
			
			if(G != 0){
				value += (g / G * 100.0)*(ai/co.validValues());
			}
		}else{
			value = Raster.getNoDataValue();
		}
	}
	
}