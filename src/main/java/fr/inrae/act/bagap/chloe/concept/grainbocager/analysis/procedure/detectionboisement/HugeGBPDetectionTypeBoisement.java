package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPDetectionTypeBoisement extends GrainBocagerProcedure {

	public HugeGBPDetectionTypeBoisement(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covHauteurBoisement = null;
		if(!new File(manager().hauteurBoisement()).exists()){
			//covHauteurBoisement = new HugeGBPRecuperationHauteurBoisement(manager()).run();
		}else{
			covHauteurBoisement = CoverageManager.getCoverage(manager().hauteurBoisement());
		}
		
		System.out.println("détection des types de boisements");
		
		if(!manager().typeBoisement().equalsIgnoreCase("")){
			// TODO
		}
		
		Coverage covTypeBoisement = HugeGrainBocager.detectionTypeBoisement(manager().typeBoisement(), manager().outputPath()+"TypeBoisementPhase1/", manager().outputPath()+"DistanceBoisement/", covHauteurBoisement, manager().tile(), manager().modeFast());
		
		covHauteurBoisement.dispose();
		
		return covTypeBoisement;
	}

}
