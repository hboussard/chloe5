package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite.GBPClusterisationFonctionnaliteFactory;

public class GBPCalculEnjeuxGlobauxFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(!manager.force() && new File(manager.functionalGrainBocagerClustering()).exists()){
			
			return true;
			
		}else{
			
			System.out.println("WARNING : input file for 'functional_grain_bocager_clustering' is missing");
			
			setParentFactory(new GBPClusterisationFonctionnaliteFactory());
			
			return checkParent(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		if(manager.hugeMode()){
			
			return new HugeGBPCalculEnjeuxGlobaux(this, manager);
			
		}else{
			
			return new GBPCalculEnjeuxGlobaux(this, manager);
		}
	}

}
