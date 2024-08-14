package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure;

public abstract class ErosionProcedure {

	private ErosionProcedureFactory factory;
	
	private ErosionManager manager;
	
	public ErosionProcedure(ErosionProcedureFactory factory, ErosionManager manager){
		this.factory = factory;
		this.manager = manager;
	}
	
	protected ErosionProcedureFactory factory(){
		return factory;
	}
	
	protected ErosionManager manager(){
		return manager;
	}
	
	public abstract void doInit();
	
	public abstract void doRun();
	
	public void run() {
		doInit();
		doRun();
	}
}
