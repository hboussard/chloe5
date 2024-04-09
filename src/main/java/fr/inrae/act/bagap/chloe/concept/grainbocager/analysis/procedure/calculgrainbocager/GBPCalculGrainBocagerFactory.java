package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence.GBPCalculDistanceInfluenceBoisementFactory;

public class GBPCalculGrainBocagerFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(!manager.force() && new File(manager.influenceDistance()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'influence_distance' is missing");
			
			setParentFactory(new GBPCalculDistanceInfluenceBoisementFactory());
			
			return checkParent(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		if(manager.hugeMode()){
			
			return new HugeGBPCalculGrainBocager(this, manager);
			
		}else{
			
			//return new HugeGBPCalculGrainBocager(this, manager);
			return new GBPCalculGrainBocager(this, manager);
		}
	}

}
