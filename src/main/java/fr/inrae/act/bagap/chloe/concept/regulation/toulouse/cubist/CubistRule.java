package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;

public class CubistRule {

	private CubistCondition condition;
	
	private CubistLinearModel model;
	
	public CubistRule(CubistCondition condition, CubistLinearModel model){
		this.condition = condition;
		this.model = model;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append(condition.toString());
		sb.append(" --> ");
		sb.append(model.toString());
		
		return sb.toString();
	}
	
	public boolean isValid(Map<String, Double> data){
		return condition.isValid(data);
	}
	
	public double predict(Map<String, Double> data){
		return model.evaluate(data);
	}
	

}
