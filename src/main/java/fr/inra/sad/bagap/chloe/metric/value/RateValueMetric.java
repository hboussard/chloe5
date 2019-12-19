package fr.inra.sad.bagap.chloe.metric.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class RateValueMetric extends Metric {

	private short v;
	
	public RateValueMetric(short v) {
		super(VariableManager.get("pNV_"+v));
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = co.countValue(v) / co.validValues();
		}
	}

}
