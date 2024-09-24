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
		
		File fWoodHeight = new File(manager.woodHeight());
		File fWoodType = new File(manager.woodType());
		
		if(!manager.force() 
				&& ((fWoodHeight.isFile() && fWoodHeight.exists()) || (fWoodHeight.isDirectory() && fWoodHeight.list().length > 0))
				&& ((fWoodType.isFile() && fWoodType.exists()) || (fWoodType.isDirectory() && fWoodType.list().length > 0))){
			
			return true;
			
		}else{
			
			if(((fWoodType.isFile() && fWoodType.exists()) || (fWoodType.isDirectory() && fWoodType.list().length > 0))) {
				
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

