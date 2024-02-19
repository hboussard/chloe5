package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure;

public abstract class EcoPaysageProcedureFactory {

	public abstract boolean check(EcoPaysageManager manager);
	
	public abstract EcoPaysageProcedure create(EcoPaysageManager manager);
	
}
