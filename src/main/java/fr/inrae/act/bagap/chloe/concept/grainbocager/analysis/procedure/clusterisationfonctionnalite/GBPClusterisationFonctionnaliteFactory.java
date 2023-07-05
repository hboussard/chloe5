package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager.GBPCalculGrainBocagerFactory;

public class GBPClusterisationFonctionnaliteFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		if(new File(manager.grainBocager()).exists()){
			return true;
		}else{
			System.out.println("input file for 'grain_bocager' is missing");
			return new GBPCalculGrainBocagerFactory().check(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		return new GBPClusterisationFonctionnalite(manager);
	}

}
