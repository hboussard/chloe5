package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilityintensitygeneration;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERErodibilityIntensityGeneration extends ErosionProcedure {

	public ERErodibilityIntensityGeneration(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().erodibility()).exists()){
			
			factory().parentFactory("erodibility_generation").create(manager()).run();
		}
		
		if(manager().force() || !new File(manager().normSlopeIntensity()).exists()){
			
			factory().parentFactory("slope_detection").create(manager()).run();
		}

		if(manager().force() || !new File(manager().cumulWaterMass()).exists()){
	
			factory().parentFactory("water_mass_cumulation").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
	
		System.out.println("generation de la carte de la carte d'intensite d'erodibilite");
		
		Erosion.erodibilityIntensityGeneration(manager().erodibilityIntensity(), manager().erodibility(), manager().cumulWaterMass(), manager().normSlopeIntensity(), manager().displacement());
		
	}

}
