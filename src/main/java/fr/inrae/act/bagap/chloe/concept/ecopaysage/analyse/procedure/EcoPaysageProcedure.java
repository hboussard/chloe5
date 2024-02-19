package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure;

public abstract class EcoPaysageProcedure {

	private EcoPaysageManager manager;
	
	public EcoPaysageProcedure(EcoPaysageManager manager){
		this.manager = manager;
	}
	
	protected EcoPaysageManager manager(){
		return manager;
	}
	
	public abstract void doInit();
	
	public abstract void doRun();
	
	public void run() {
		doInit();
		doRun();
	}
	
}
