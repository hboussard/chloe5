package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class HugeGBPDetectionTypeBoisement extends GrainBocagerProcedure {

	public HugeGBPDetectionTypeBoisement(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().woodHeight()).exists()){
			
			factory().parentFactory().create(manager()).run();
			
		}
	}
	
	@Override
	public void doRun() {
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(manager().woodHeight());
		
		System.out.println("detection des types de boisements");
		
		Coverage covTypeBoisement = HugeGrainBocager.detectionTypeBoisement(manager().woodType(), manager().outputFolder()+"woodTypePhase1/", manager().outputFolder()+"woodDistance/", covHauteurBoisement, manager().tile(), manager().fastMode());
		
		covHauteurBoisement.dispose();
		
		covTypeBoisement.dispose();
	}

}
