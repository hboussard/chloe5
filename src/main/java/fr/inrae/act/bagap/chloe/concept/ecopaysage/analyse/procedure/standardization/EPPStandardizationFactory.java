package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedureFactory;

public class EPPStandardizationFactory extends EcoPaysageProcedureFactory {

	@Override
	public boolean check(EcoPaysageManager manager) {
		// TODO
		return true;
	}

	@Override
	public EcoPaysageProcedure create(EcoPaysageManager manager) {
		return new EPPStandardization(manager);
	}
	
}
