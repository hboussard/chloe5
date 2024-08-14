package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erosioncalculation;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERErosionCalculation extends ErosionProcedure {

	public ERErosionCalculation(ErosionProcedureFactory factory, ErosionManager manager) {
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
		
		if(manager().force() || !new File(manager().erodibilityIntensity()).exists()){
			
			factory().parentFactory("erodibility_intensity_generation").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
	
		System.out.println("calcul des sources et des depots d'erosion");
		
		Erosion.erosionCalculation(manager().sourceErosionIntensity(), manager().depositionErosionIntensity(), manager().elevation(), manager().infiltration(), manager().slopeIntensity(), manager().erodibilityIntensity(), manager().displacement(), 201);
	}

}
