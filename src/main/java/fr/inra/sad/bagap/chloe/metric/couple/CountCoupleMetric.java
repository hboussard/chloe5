package fr.inra.sad.bagap.chloe.metric.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inra.sad.bagap.chloe.util.Couple;

public class CountCoupleMetric extends Metric {

	private float c;
	
	public CountCoupleMetric(float c) {
		super();
		short v1 = Couple.getOne(c);
		short v2 = Couple.getOther(c);
		setVariable(VariableManager.get("NC_"+v1+"-"+v2));
		this.c = c;
	}
	
	public CountCoupleMetric(short v1, short v2) {
		super();
		if(v1 < v2) {
			setVariable(VariableManager.get("NC_"+v1+"-"+v2));
		}else{
			setVariable(VariableManager.get("NC_"+v2+"-"+v1));
		}
		this.c = Couple.getCouple(v1, v2);
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validCouples() > 0){
			value = co.countCouple(c);
		}
	}
}
