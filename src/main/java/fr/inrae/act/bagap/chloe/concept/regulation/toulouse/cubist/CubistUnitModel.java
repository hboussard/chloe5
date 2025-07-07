package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;
import java.util.Map.Entry;

import fr.inrae.act.bagap.apiland.analysis.Stats;

public class CubistUnitModel {

	private Map<Integer, CubistRule> rules;
	
	private final Stats stats = new Stats();
	
	public CubistUnitModel(Map<Integer, CubistRule> rules){
		this.rules = rules;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("number of rules = "+size()+"\n");
		
		for(Entry<Integer, CubistRule> e : rules.entrySet()){
			sb.append("rule "+e.getKey()+" : ");
			sb.append(e.getValue().toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public int size(){
		return rules.size();
	}
	
	public double predict(Map<String, Double> data){
		
		//System.out.println();
		stats.reset();
		for(CubistRule rc : rules.values()){
			//System.out.println(rc);
			if(rc.isValid(data)){
				//System.out.println("voui");
				stats.add(rc.predict(data));
			}
		}
				
		stats.calculate();
		return stats.getAverage();
	}
	
}
