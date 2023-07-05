package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement.GBPDetectionTypeBoisementFactory;

public class GBPCalculDistanceInfluenceBoisementFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(new File(manager.hauteurBoisement()).exists()
				&& new File(manager.typeBoisement()).exists()){
			return true;
		}else{
			System.out.println("input file for 'type_boisement' is missing");
			return new GBPDetectionTypeBoisementFactory().check(manager);
		}
		
		//boolean ok = true;
		/*if(manager.outputPath().equalsIgnoreCase("") && manager.outputFile().equalsIgnoreCase("") ){
			System.err.println("output folder 'output_path' and output file 'output_file' are both missing");
			ok = false;
		}*//*
		if(manager.distanceInfluenceBoisement().equalsIgnoreCase("") ){
			System.err.println("output file for 'distance_influence_boisement' is missing");
			ok = false;
		}
		if(manager.hauteurBoisement().equalsIgnoreCase("")){
			System.err.println("input file for 'hauteur_boisement' is missing");
			ok = false;
		}
		if(manager.typeBoisement().equalsIgnoreCase("")){
			System.err.println("input file for 'type_boisement' is missing");
			ok = false;
		}*/
		//return ok;
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		return new GBPCalculDistanceInfluenceBoisement(manager);
	}

}

