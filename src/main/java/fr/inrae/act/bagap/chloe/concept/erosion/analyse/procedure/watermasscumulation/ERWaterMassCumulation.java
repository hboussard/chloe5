package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermasscumulation;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERWaterMassCumulation extends ErosionProcedure {

	public ERWaterMassCumulation(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().elevation()).exists()){
			
			factory().parentFactory("data_initialization").create(manager()).run();
		}
		
		if(manager().force() || !new File(manager().infiltration()).exists()){
			
			factory().parentFactory("infiltration_generation").create(manager()).run();
		}

		if(manager().force() || !new File(manager().slopeIntensity()).exists()){
	
			factory().parentFactory("slope_detection").create(manager()).run();
		}

		if(manager().force() || !new File(manager().initialWaterMass()).exists()){
	
			factory().parentFactory("water_mass_initialization").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		System.out.println("cumul des masses d'eau");
		
		Erosion.waterMassCumulation(manager().cumulWaterMass(), manager().initialWaterMass(), manager().elevation(), manager().infiltration(), manager().slopeIntensity(), manager().displacement(), 201);
		
	}

}
