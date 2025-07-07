package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CubistCouvertCondition extends CubistVarCondition {

	private Set<Double> couverts;
	
	public CubistCouvertCondition(String... couverts){
		this.couverts = new HashSet<Double>();
		for(String c : couverts){
			this.couverts.add(CubistModel.couvertMap.get(c));
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Couvert in ");
		for(Double dc : couverts){
			for(Entry<String, Double> e : CubistModel.couvertMap.entrySet()){
				if(e.getValue() == dc){
					sb.append(e.getKey()+" ");
				}
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public boolean isValid(Map<String, Double> data){
		return couverts.contains(data.get("Couvert"));
	}
}
