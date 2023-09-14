package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement.HugeGBPDetectionTypeBoisement;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPCalculDistanceInfluenceBoisement extends GrainBocagerProcedure {

	public HugeGBPCalculDistanceInfluenceBoisement(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covTypeBoisement;
		if(!new File(manager().typeBoisement()).exists()){
			covTypeBoisement = new HugeGBPDetectionTypeBoisement(manager()).run();
		}else{
			covTypeBoisement = CoverageManager.getCoverage(manager().typeBoisement());
		}
		
		System.out.println("calcul des distances d'influences des boisements");
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(manager().hauteurBoisement());
		
		Coverage covDistanceInfluence = HugeGrainBocager.calculDistancesInfluences(manager().distanceInfluenceBoisement(), covHauteurBoisement, covTypeBoisement, manager().tile(), manager().modeFast());
		
		covHauteurBoisement.dispose();
		covTypeBoisement.dispose();
		
		return covDistanceInfluence;
	}

}