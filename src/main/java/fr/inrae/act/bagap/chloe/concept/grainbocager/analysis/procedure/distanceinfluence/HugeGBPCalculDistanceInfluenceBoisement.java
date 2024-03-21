package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPCalculDistanceInfluenceBoisement extends GrainBocagerProcedure {

	public HugeGBPCalculDistanceInfluenceBoisement(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}
	
	@Override
	public void doInit() {
		
		if(manager().force() 
				|| !new File(manager().woodType()).exists()
				|| !new File(manager().woodHeight()).exists()){
			
			factory().parentFactory().create(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		Coverage covTypeBoisement = CoverageManager.getCoverage(manager().woodType());
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(manager().woodHeight());
		
		System.out.println("calcul des distances d'influences des boisements");
		
		Coverage covDistanceInfluence = HugeGrainBocager.calculDistancesInfluences(manager().influenceDistance(), covHauteurBoisement, covTypeBoisement, manager().tile(), manager().fastMode());
		
		covHauteurBoisement.dispose();
		covTypeBoisement.dispose();
		covDistanceInfluence.dispose();;
	}

}