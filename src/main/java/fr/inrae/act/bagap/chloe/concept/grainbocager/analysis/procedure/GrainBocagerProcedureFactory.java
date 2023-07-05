package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

public abstract class GrainBocagerProcedureFactory {

	public abstract boolean check(GrainBocagerManager manager);
	
	public abstract GrainBocagerProcedure create(GrainBocagerManager manager);
	
}
