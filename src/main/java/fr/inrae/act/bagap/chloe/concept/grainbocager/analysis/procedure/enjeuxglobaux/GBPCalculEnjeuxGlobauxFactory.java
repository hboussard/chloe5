package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite.GBPClusterisationFonctionnaliteFactory;

public class GBPCalculEnjeuxGlobauxFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		if(new File(manager.clusterGrainBocagerFonctionnel()).exists()){
			return true;
		}else{
			System.out.println("input file for 'cluster_grain_bocager_fonctionnel' is missing");
			return new GBPClusterisationFonctionnaliteFactory().check(manager);
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		return new GBPCalculEnjeuxGlobaux(manager);
	}

}
