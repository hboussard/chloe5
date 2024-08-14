package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermassinitialization;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERWaterMassInitialization extends ErosionProcedure {

	public ERWaterMassInitialization(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {

		if(manager().force() || !new File(manager().os()).exists()){
			
			factory().parentFactory("data_initialization").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		System.out.println("initialisation des masses d'eau");
		
		Erosion.waterMassInitialization(manager().initialWaterMass(), manager().os(), manager().waterQuantity());
		
	}

}
