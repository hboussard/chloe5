package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

public abstract class GrainBocagerProcedureFactory {

	private GrainBocagerProcedureFactory parentFactory;
	
	protected void setParentFactory(GrainBocagerProcedureFactory parentFactory) {
		this.parentFactory = parentFactory;
	}
	
	public GrainBocagerProcedureFactory parentFactory() {
		return parentFactory;
	}
	
	protected boolean checkParent(GrainBocagerManager manager) {
		return parentFactory.check(manager);
	}
	
	public abstract boolean check(GrainBocagerManager manager);
	
	public abstract GrainBocagerProcedure create(GrainBocagerManager manager);
	
}
