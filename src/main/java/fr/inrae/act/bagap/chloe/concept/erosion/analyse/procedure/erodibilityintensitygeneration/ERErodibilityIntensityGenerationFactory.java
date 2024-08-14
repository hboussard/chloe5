package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilityintensitygeneration;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilitygeneration.ERErodibilityGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection.ERSlopeDetectionFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermasscumulation.ERWaterMassCumulationFactory;

public class ERErodibilityIntensityGenerationFactory extends ErosionProcedureFactory {

	public ERErodibilityIntensityGenerationFactory() {
		super("erodibility_intensity_generation");
	}

	@Override
	public boolean check(ErosionManager manager) {

		if(!manager.force() 
				&& new File(manager.erodibility()).exists()
				&& new File(manager.normSlopeIntensity()).exists()
				&& new File(manager.cumulWaterMass()).exists()){
			
			return true;
			
		}else{
			
			if(manager.force() || !new File(manager.erodibility()).exists()) {
				System.out.println("WARNING : input file for 'erodibility' is missing");
				
				addParentFactory(new ERErodibilityGenerationFactory());
			}
			
			if(manager.force() || !new File(manager.normSlopeIntensity()).exists()) {
				System.out.println("WARNING : input file for 'norm_slope_intensity' is missing");
				
				addParentFactory(new ERSlopeDetectionFactory());
			}
			
			if(manager.force() || !new File(manager.cumulWaterMass()).exists()) {
				System.out.println("WARNING : input file for 'cumul_water_mass' is missing");
				
				addParentFactory(new ERWaterMassCumulationFactory());
			}
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERErodibilityIntensityGeneration(this, manager);
	}
	
}