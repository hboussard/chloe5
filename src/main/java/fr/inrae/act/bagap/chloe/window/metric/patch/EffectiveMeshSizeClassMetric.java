package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.value.ValueMetric;

public class EffectiveMeshSizeClassMetric extends Metric implements ValueMetric {
	
	private int classMetric;
	
	public EffectiveMeshSizeClassMetric(short cm){
		super("EMS-class_"+cm);
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		/*
		if(co.countValues() > 0){
			value = Math.pow(co.countValue(classMetric), 2) / co.validValues();
		}else{
			value = -1;
		}
		*/
		if(co.validValues() > 0){
			value = (double) co.totalSurfaceCarre(classMetric)/(co.validValues()*Math.pow(co.resolution(), 2)/10000.0);
		}else{
			value = 0;
		}
	}
	
}