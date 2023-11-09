package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;

public class GBPRecuperationHauteurBoisementFactory extends GrainBocagerProcedureFactory {

	@Override
	public boolean check(GrainBocagerManager manager) {
		if(new File(manager.bocage()).exists()){
			return true;
		}else{
			System.out.println("input file for 'bocage' is missing");
			return false;
		}
		
		//boolean ok = true;
		/*if(manager.outputPath().equalsIgnoreCase("") && manager.outputFile().equalsIgnoreCase("") ){
			System.err.println("output folder 'output_path' and output file 'output_file' are both missing");
			ok = false;
		}*/
		/*
		if(manager.bocage().equalsIgnoreCase("")){
			System.err.println("input MNHC folder for 'bocage' is missing");
			ok = false;
		}*/
		//return ok;
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		return new GBPRecuperationHauteurBoisement(manager);
	}

}
