package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;
import java.io.IOException;

import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class GBPDetectionTypeBoisement extends GrainBocagerProcedure {

	public GBPDetectionTypeBoisement(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
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
		
		Coverage covTypeBoisement = GrainBocager.detectionTypeBoisement(manager().woodHeight(), manager().fastMode());
		
		covHauteurBoisement.dispose();
		
		CoverageManager.write(manager().woodType(), covTypeBoisement.getData(), covTypeBoisement.getEntete());
			
		covTypeBoisement.dispose();
		
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_type_boisement.qml"), Tool.deleteExtension(manager().woodType())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}