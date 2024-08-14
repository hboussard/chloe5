package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERDataInitializationFactory extends ErosionProcedureFactory {

	public ERDataInitializationFactory() {
		super("data_initialization");
	}

	@Override
	public boolean check(ErosionManager manager) {
		// TODO
		return true;
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERDataInitialization(this, manager);
	}
	
}