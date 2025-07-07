package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;
import java.util.Set;

public class CubistCondition {

	private Set<CubistVarCondition> conditions;
	
	public CubistCondition(){}
	
	public CubistCondition(Set<CubistVarCondition> conditions){
		this.conditions = conditions; 
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("if(");
		
		int index = 1;
		for(CubistVarCondition condition : conditions){
			sb.append(condition.toString());
			if(++index <= size()){
				sb.append(" & ");
			}
		}
		
		sb.append(")");
		
		return sb.toString();
	}
	
	private int size(){
		return conditions.size();
	}
	
	public boolean isValid(Map<String, Double> data){
		for(CubistVarCondition condition : conditions){
			if(!condition.isValid(data)){
				return false;
			}
		}
		return true;
	}
	
}
