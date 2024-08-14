package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure;

import java.util.Map;
import java.util.HashMap;

public abstract class ErosionProcedureFactory {

	private String name;
	
	private Map<String, ErosionProcedureFactory> parentFactories;
	
	public ErosionProcedureFactory(String name) {
		this.name = name;
		parentFactories = new HashMap<String, ErosionProcedureFactory>();
	}
	
	public String getName() {
		return name;
	}
	
	protected void addParentFactory(ErosionProcedureFactory parentFactory) {
		this.parentFactories.put(parentFactory.getName(), parentFactory);
	}

	public ErosionProcedureFactory parentFactory(String name) {
		return parentFactories.get(name);
	}
	
	protected boolean checkParents(ErosionManager manager) {
		for(ErosionProcedureFactory parentFactory : parentFactories.values()) {
			if(!parentFactory.check(manager)) {
				return false;
			}
		}
		return true;
	}
	
	public abstract boolean check(ErosionManager manager);
	
	public abstract ErosionProcedure create(ErosionManager manager);
	
}
