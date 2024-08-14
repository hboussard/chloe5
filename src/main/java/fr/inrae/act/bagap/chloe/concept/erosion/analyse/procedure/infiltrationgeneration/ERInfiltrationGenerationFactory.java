package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.infiltrationgeneration;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;

public class ERInfiltrationGenerationFactory extends ErosionProcedureFactory {

	public ERInfiltrationGenerationFactory() {
		super("infiltration_generation");
	}

	@Override
	public boolean check(ErosionManager manager) {

		if(!manager.force() && new File(manager.os()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'os' is missing");
			
			addParentFactory(new ERDataInitializationFactory());
			
			return checkParents(manager);
		}
	}

	@Override
	public ErosionProcedure create(ErosionManager manager) {
		return new ERInfiltrationGeneration(this, manager);
	}
	
}