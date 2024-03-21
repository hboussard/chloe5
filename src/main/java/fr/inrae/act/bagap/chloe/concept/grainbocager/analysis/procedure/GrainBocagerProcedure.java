package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

public abstract class GrainBocagerProcedure {

	private GrainBocagerProcedureFactory factory;
	
	private GrainBocagerManager manager;
	
	public GrainBocagerProcedure(GrainBocagerProcedureFactory factory, GrainBocagerManager manager){
		this.factory = factory;
		this.manager = manager;
	}
	
	protected GrainBocagerProcedureFactory factory(){
		return factory;
	}
	
	protected GrainBocagerManager manager(){
		return manager;
	}
	
	public void run() {
		doInit();
		doRun();
	}
	
	public abstract void doInit();
	
	public abstract void doRun();
	
}
