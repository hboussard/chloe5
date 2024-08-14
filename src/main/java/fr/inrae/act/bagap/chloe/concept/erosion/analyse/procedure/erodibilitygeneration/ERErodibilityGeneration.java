package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilitygeneration;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERErodibilityGeneration extends ErosionProcedure {

	public ERErodibilityGeneration(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().territory()).exists()){
			
			factory().parentFactory("data_initialization").create(manager()).run();
		}
	}

	@Override
	public void doRun() {
	
		System.out.println("generation de la carte d'erodibilite");
		
		Erosion.erodibilityGeneration(manager().erodibility(), manager().os(), manager().erodibilityMap());
		
	}

}
