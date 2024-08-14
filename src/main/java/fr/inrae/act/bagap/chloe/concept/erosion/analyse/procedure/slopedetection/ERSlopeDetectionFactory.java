package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;

public class ERSlopeDetectionFactory extends ErosionProcedureFactory {

	public ERSlopeDetectionFactory() {
		super("slope_detection");
	}

	@Override
	public boolean check(ErosionManager manager) {

		if(!manager.force() && new File(manager.elevation()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'elevation' is missing");
			
			addParentFactory(new ERDataInitializationFactory());
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERSlopeDetection(this, manager);
	}
}
