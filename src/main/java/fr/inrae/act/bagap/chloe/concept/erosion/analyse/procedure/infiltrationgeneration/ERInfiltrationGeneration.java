package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.infiltrationgeneration;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERInfiltrationGeneration extends ErosionProcedure {

	public ERInfiltrationGeneration(ErosionProcedureFactory factory, ErosionManager manager) {
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
	
		System.out.println("generation de la carte d'infiltration");
		
		Erosion.infiltrationGeneration(manager().infiltration(), manager().os(), manager().infiltrationMap());
		
	}
		

}
