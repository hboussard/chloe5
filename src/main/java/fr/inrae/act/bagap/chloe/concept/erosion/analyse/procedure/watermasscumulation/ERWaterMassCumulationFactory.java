package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermasscumulation;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.infiltrationgeneration.ERInfiltrationGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection.ERSlopeDetectionFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermassinitialization.ERWaterMassInitializationFactory;

public class ERWaterMassCumulationFactory extends ErosionProcedureFactory {

	public ERWaterMassCumulationFactory() {
		super("water_mass_cumulation");
	}

	@Override
	public boolean check(ErosionManager manager) {
		
		if(!manager.force() 
				&& new File(manager.elevation()).exists()
				&& new File(manager.infiltration()).exists()
				&& new File(manager.slopeIntensity()).exists()
				&& new File(manager.initialWaterMass()).exists()){
			
			return true;
			
		}else{
			
			if(manager.force() || !new File(manager.elevation()).exists()) {
				System.out.println("WARNING : input file for 'os' is missing");
				
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
			
			if(manager.force() || !new File(manager.initialWaterMass()).exists()) {
				System.out.println("WARNING : input file for 'initial water mass' is missing");
				
				addParentFactory(new ERWaterMassInitializationFactory());
			}
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERWaterMassCumulation(this, manager);
	}
}
