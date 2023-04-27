package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MajorityClassMetric extends Metric implements ValueMetric {

	public MajorityClassMetric() {
		super("Majority");
	}

	@Override
	protected void doCalculate(Counting co) {
		double n;
		double majN = -1;
		for(int v : co.values()){
			n = co.countValue(v);
			if(n != 0){
				if(majN == -1 || n > majN){
					value = v;
					majN = n;
				}
			}
		}
	}

}
