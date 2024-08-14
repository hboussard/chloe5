package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermassinitialization;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;

public class ERWaterMassInitializationFactory extends ErosionProcedureFactory {

	public ERWaterMassInitializationFactory() {
		super("water_mass_initialization");
	}

	@Override
	public boolean check(ErosionManager manager) {

		if(!manager.force() && new File(manager.os()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'os' is missing");
			
			addParentFactory(new ERDataInitializationFactory());
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERWaterMassInitialization(this, manager);
	}
}
