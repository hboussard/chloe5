package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;
import java.io.IOException;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPCalculDistanceInfluenceBoisement extends GrainBocagerProcedure {

	public GBPCalculDistanceInfluenceBoisement(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
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
		
		Coverage covDistanceInfluence = GrainBocager.calculDistancesInfluences(covHauteurBoisement, covTypeBoisement, manager().fastMode());
		
		covHauteurBoisement.dispose();
		covTypeBoisement.dispose();
		
		CoverageManager.write(manager().influenceDistance(), covDistanceInfluence.getData(), covDistanceInfluence.getEntete());
			
		covDistanceInfluence.dispose();
		
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_distance_influence_boisement.qml"), Tool.deleteExtension(manager().influenceDistance())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}