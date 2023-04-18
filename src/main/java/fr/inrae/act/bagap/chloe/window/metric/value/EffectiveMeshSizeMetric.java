package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EffectiveMeshSizeMetric extends Metric implements ValueMetric {
	
	public EffectiveMeshSizeMetric(){
		super("EMS");
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			double sa = 0;
			for(int vv : co.values()){
				sa += Math.pow(co.countValue(vv), 2);
			}
			value = sa / co.validValues();
		}else{
			value = -1;
		}
	}
	
}