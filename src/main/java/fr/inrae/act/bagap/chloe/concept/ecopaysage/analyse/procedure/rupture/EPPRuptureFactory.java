package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.rupture;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedureFactory;

public class EPPRuptureFactory extends EcoPaysageProcedureFactory {

	@Override
	public boolean check(EcoPaysageManager manager) {
		// TODO
		return true;
	}

	@Override
	public EcoPaysageProcedure create(EcoPaysageManager manager) {
		return new EPPRupture(manager);
	}
	
}
