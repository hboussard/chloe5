package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager.GBPCalculGrainBocagerFactory;

public class GBPClusterisationFonctionnaliteFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(!manager.force() && new File(manager.grainBocager()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'grain_bocager' is missing");
			
			setParentFactory(new GBPCalculGrainBocagerFactory());
			
			return checkParent(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		if(manager.hugeMode()){
			
			return new HugeGBPClusterisationFonctionnalite(this, manager);
			
		}else{
			
			return new GBPClusterisationFonctionnalite(this, manager);
		}
	}

}
