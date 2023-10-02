package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement.GBPDetectionTypeBoisement;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPCalculDistanceInfluenceBoisement extends GrainBocagerProcedure {

	public GBPCalculDistanceInfluenceBoisement(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covTypeBoisement;
		if(manager().force() || !new File(manager().typeBoisement()).exists()){
			covTypeBoisement = new GBPDetectionTypeBoisement(manager()).run();
		}else{
			covTypeBoisement = CoverageManager.getCoverage(manager().typeBoisement());
		}
		
		System.out.println("calcul des distances d'influences des boisements");
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(manager().hauteurBoisement());
		
		Coverage covDistanceInfluence = GrainBocager.calculDistancesInfluences(covHauteurBoisement, covTypeBoisement, manager().modeFast());
		
		covHauteurBoisement.dispose();
		covTypeBoisement.dispose();
		
		if(!manager().distanceInfluenceBoisement().equalsIgnoreCase("")){
			CoverageManager.write(manager().distanceInfluenceBoisement(), covDistanceInfluence.getData(), covDistanceInfluence.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_distance_influence_boisement.qml"), Tool.deleteExtension(manager().distanceInfluenceBoisement())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}/*else{
			CoverageManager.write(manager().outputPath()+"distance_influence_boisement.tif", covDistanceInfluence.getDatas(), covDistanceInfluence.getEntete());
		}*/
		
		//covDistanceInfluence.dispose();
		return covDistanceInfluence;
	}

}