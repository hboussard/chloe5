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
		
			System.out.println("ERROR : input file for 'bocage' is missing");
			
			return false;
		}
	}

	@Override
	public GrainBocagerProcedure create(GrainBocagerManager manager) {
		
		return new GBPRecuperationHauteurBoisement(this, manager);
	}

}
