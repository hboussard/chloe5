package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;

public class CubistVarCondition {

	private String var;
	
	private String sign;
	
	private double value;
	
	public CubistVarCondition(){}
	
	public CubistVarCondition(String var, String sign, double value){
		this.var = var;
		this.sign = sign;
		this.value = value;
	}
	
	@Override
	public String toString(){
		return var+" "+sign+" "+value;
	}
	
	public boolean isValid(Map<String, Double> data){
		switch(sign){
		case "<"  : return data.get(var) < value;
		case "<=" : return data.get(var) <= value;
		case "="  : return data.get(var) == value;
		case "==" : return data.get(var) == value;
		case ">"  : return data.get(var) > value;
		case ">=" : return data.get(var) >= value;
		default : throw new IllegalArgumentException();
		}
	}
}
