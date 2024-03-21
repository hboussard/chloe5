package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur.GBPRecuperationHauteurBoisementFactory;

public class GBPDetectionTypeBoisementFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(!manager.force() && new File(manager.woodHeight()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'wood_height' is missing");
			
			setParentFactory(new GBPRecuperationHauteurBoisementFactory());
			
			return checkParent(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		if(manager.hugeMode()){
		
			return new HugeGBPDetectionTypeBoisement(this, manager);
			
		}else{
			
			return new GBPDetectionTypeBoisement(this, manager);
		}
	}

}
