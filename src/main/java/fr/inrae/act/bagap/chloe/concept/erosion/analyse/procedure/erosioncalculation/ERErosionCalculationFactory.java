package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erosioncalculation;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilityintensitygeneration.ERErodibilityIntensityGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.infiltrationgeneration.ERInfiltrationGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection.ERSlopeDetectionFactory;

public class ERErosionCalculationFactory extends ErosionProcedureFactory {

	public ERErosionCalculationFactory() {
		super("erosion_calculation");
	}

	@Override
	public boolean check(ErosionManager manager) {

		if(!manager.force() 
				&& new File(manager.elevation()).exists()
				&& new File(manager.infiltration()).exists()
				&& new File(manager.slopeIntensity()).exists()
				&& new File(manager.erodibilityIntensity()).exists()){
			
			return true;
			
		}else{
			
			if(manager.force() || !new File(manager.elevation()).exists()) {
				System.out.println("WARNING : input file for 'elevation' is missing");
				
				addParentFactory(new ERDataInitializationFactory());
			}
			
			if(manager.force() || !new File(manager.infiltration()).exists()) {
				System.out.println("WARNING : input file for 'infiltration' is missing");
				
				addParentFactory(new ERInfiltrationGenerationFactory());
			}
			
			if(manager.force() || !new File(manager.slopeIntensity()).exists()) {
				System.out.println("WARNING : input file for 'slope_intensity' is missing");
				
				addParentFactory(new ERSlopeDetectionFactory());
			}
			
			if(manager.force() || !new File(manager.erodibilityIntensity()).exists()) {
				System.out.println("WARNING : input file for 'erodibility_intensity' is missing");
				
				addParentFactory(new ERErodibilityIntensityGenerationFactory());
			}
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERErosionCalculation(this, manager);
	}
	
}