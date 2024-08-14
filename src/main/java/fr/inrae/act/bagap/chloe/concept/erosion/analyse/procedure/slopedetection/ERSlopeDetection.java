package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERSlopeDetection extends ErosionProcedure {

	public ERSlopeDetection(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().elevation()).exists()){
			
			factory().parentFactory("data_initialization").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		System.out.println("detection des pentes");
		
		Erosion.slopeDetection(manager().slopeIntensity(), manager().elevation());
		
		System.out.println("normalisation des pentes");
		
		Erosion.normalizeSlopeIntensity(manager().normSlopeIntensity(), manager().slopeIntensity());
	}

}
