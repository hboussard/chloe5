package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EffectiveMeshSizeMetric extends Metric implements PatchMetric {
	
	public EffectiveMeshSizeMetric(){
		super("EMS");
	}
	
	@Override
	public void doCalculate(Counting co) {
		/*
		if(co.countValues() > 0){
			double sa = 0;
			for(int vv : co.values()){
				sa += Math.pow(co.countValue(vv), 2);
			}
			value = sa / co.validValues();
		}else{
			value = -1;
		}
		*/
		if(co.validValues() > 0){
			value = (double) co.totalSurfaceCarre()/(co.validValues()*Math.pow(co.resolution(), 2)/10000.0);
		}else{
			value = 0;
		}
	}
	
}