package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.Stats;

public class CubistModel {

	private Map<Integer, CubistUnitModel> committees;
	
	private final Stats stats = new Stats();
	
	public static final Map<String, Double> couvertMap = new HashMap<String, Double>();
	static{
		couvertMap.put("Ete", 1.0);
		couvertMap.put("Prairie", 2.0);
		couvertMap.put("Printemps", 3.0);
		couvertMap.put("Hiver", 4.0);
	}
	
	public CubistModel(){
		committees = new TreeMap<Integer, CubistUnitModel>();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(); 
		sb.append("Cubist model : number of unit models = "+size()+"\n");
		
		for(Entry<Integer, CubistUnitModel> e : committees.entrySet()){
			sb.append("model "+e.getKey()+" : ");
			sb.append(e.getValue().toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public void addCubistUnitModel(int id, CubistUnitModel cum){
		committees.put(id, cum);
	}
	
	public int size(){
		return committees.size();
	}
	
	public double predict(Map<String, Double> data){
		stats.reset();
		for(CubistUnitModel cum : committees.values()){
			stats.add(cum.predict(data));
		}
		stats.calculate();
		return stats.getAverage();
	}
	
}
