package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement.GBPDetectionTypeBoisementFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur.GBPRecuperationHauteurBoisementFactory;

public class GBPCalculDistanceInfluenceBoisementFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(!manager.force() && new File(manager.woodHeight()).exists() && new File(manager.woodType()).exists()){
			
			return true;
			
		}else{
			
			if(new File(manager.woodType()).exists()) {
				
				System.out.println("WARNING : input file for 'wood_height' is missing");
				
				setParentFactory(new GBPRecuperationHauteurBoisementFactory());
				
				return checkParent(manager);
				
			}else {
				
				System.out.println("WARNING : input file for 'wood_type' is missing");
				
				setParentFactory(new GBPDetectionTypeBoisementFactory());
				
				return checkParent(manager);
			}
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		if(manager.hugeMode()){
			
			return new HugeGBPCalculDistanceInfluenceBoisement(this, manager);
			
		}else{
			
			return new GBPCalculDistanceInfluenceBoisement(this, manager);
		}
	}

}

