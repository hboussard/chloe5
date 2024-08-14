package fr.inrae.act.bagap.chloe.concept.occupationsol.analyse.procedure;

import fr.inrae.act.bagap.chloe.concept.occupationsol.analyse.OccupationSolManager;

public abstract class OccupationSolProcedure {

	private OccupationSolManager manager;
	
	public OccupationSolProcedure(OccupationSolManager manager){
		this.manager = manager;
	}
	
	protected OccupationSolManager manager(){
		return manager;
	}
	
	public abstract void doInit();
	
	public abstract void doRun();
	
	public void run() {
		doInit();
		doRun();
	}
	
}