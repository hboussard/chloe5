package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur.GBPRecuperationHauteurBoisementFactory;

public class GBPDetectionTypeBoisementFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		
		if(new File(manager.hauteurBoisement()).exists()){
			return true;
		}else{
			System.out.println("input file for 'hauteur_boisement' is missing");
			return new GBPRecuperationHauteurBoisementFactory().check(manager);
		}
		
		
		//boolean ok = true;
		/*if(manager.outputPath().equalsIgnoreCase("") && manager.outputFile().equalsIgnoreCase("") ){
			System.err.println("output folder 'output_path' and output file 'output_file' are both missing");
			ok = false;
		}*/
		/*
		if(manager.typeBoisement().equalsIgnoreCase("")){
			System.err.println("output file 'type_boisement' is missing");
			ok = false;
		}
		
		if(manager.bocage().equalsIgnoreCase("")){
			System.err.println("input MNHC folder for 'bocage' is missing");
			ok = false;
		}*/
		//return ok;
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		if(manager.tile() == null){
			return new GBPDetectionTypeBoisement(manager);
		}else{
			return new HugeGBPDetectionTypeBoisement(manager);
		}
	}

}
