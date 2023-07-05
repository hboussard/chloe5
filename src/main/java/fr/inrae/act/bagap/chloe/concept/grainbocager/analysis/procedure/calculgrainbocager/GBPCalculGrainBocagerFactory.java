package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence.GBPCalculDistanceInfluenceBoisementFactory;

public class GBPCalculGrainBocagerFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(new File(manager.distanceInfluenceBoisement()).exists()){
			return true;
		}else{
			System.out.println("input file for 'distance_influence_boisement' is missing");
			return new GBPCalculDistanceInfluenceBoisementFactory().check(manager);
		}
		
		//boolean ok = true;
		/*if(manager.outputPath().equalsIgnoreCase("") && manager.outputFile().equalsIgnoreCase("") ){
			System.err.println("output folder 'output_path' and output file 'output_file' are both missing");
			ok = false;
		}*/
		/*
		if(manager.grainBocager().equalsIgnoreCase("")){
			System.err.println("output file 'grain_bocager' is missing");
			ok = false;
		}*/
		/*
		if(!new File(manager.distanceInfluenceBoisement()).exists()){
			System.err.println("input distance of influence of woody elements for 'distance_influence_boisement' is missing");
			ok = false;
		}*/
		/*
		if(manager.bocage().equalsIgnoreCase("")){
			System.err.println("input MNHC folder for 'bocage' is missing");
			ok = false;
		}
		if(manager.typeBoisement().equalsIgnoreCase("")){
			System.err.println("input type of woody elements for 'type_boisement' is missing");
			ok = false;
		}*/
		//return ok;
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		return new GBPCalculGrainBocager(manager);
	}

}
