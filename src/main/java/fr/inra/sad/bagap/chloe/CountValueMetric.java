package fr.inra.sad.bagap.chloe;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;

public class CountValueMetric extends Metric {

	private short v;
	
	public CountValueMetric(short v) {
		super(VariableManager.get("NV_"+v));
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.countValue(v);
		}
	}

}
