package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EffectiveMeshSizeClassMetric extends Metric implements ValueMetric {
	
	private int classMetric;
	
	public EffectiveMeshSizeClassMetric(short cm){
		super("EMS-class_"+cm);
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = Math.pow(co.countValue(classMetric), 2) / co.validValues();
		}else{
			value = -1;
		}
	}
	
}