package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;

public class CubistNACondition extends CubistCondition {

	@Override
	public boolean isValid(Map<String, Double> data){
		return true;
	}
	
	@Override
	public String toString(){
		return "NA";
	}
	
}
