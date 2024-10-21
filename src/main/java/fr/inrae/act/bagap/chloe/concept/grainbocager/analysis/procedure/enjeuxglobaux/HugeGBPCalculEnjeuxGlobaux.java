package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class HugeGBPCalculEnjeuxGlobaux extends GrainBocagerProcedure {

	public HugeGBPCalculEnjeuxGlobaux(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
	
		if(manager().force() || !new File(manager().functionalGrainBocagerClustering()).exists()){
			
			factory().parentFactory().create(manager()).run();
			
		}	
	}
	
	@Override
	public void doRun() {

		Coverage covClusterFonctionnel = CoverageManager.getCoverage(manager().functionalGrainBocagerClustering());
		
		System.out.println("calcul des zones de fragmentation de grain bocager fonctionnel a "+manager().issuesCellSize()+"m en utilisant une fenetre de "+manager().issuesWindowRadius()+"m");
		
		Coverage covFragmentationGrainBocagerFonctionnel = HugeGrainBocager.runSHDIClusterGrainBocagerFonctionnel(manager().functionalGrainBocagerFragmentation(), covClusterFonctionnel, manager().entete(), manager().issuesWindowRadius(), manager().issuesCellSize(), manager().tile(), manager().fastMode());
		
		covClusterFonctionnel.dispose();
		
		covFragmentationGrainBocagerFonctionnel.dispose();
		
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(manager().functionalGrainBocager());
		
		System.out.println("calcul des proportions de grain bocager fonctionnel dans une fenetre de "+manager().issuesWindowRadius()+"m avec une resolution de "+manager().issuesCellSize()+"m");
		
		Coverage covProportionGrainBocagerFonctionnel = HugeGrainBocager.runProportionGrainBocagerFonctionnel(manager().functionalGrainBocagerProportion(), covGrainBocagerFonctionnel, manager().entete(), manager().issuesWindowRadius(), manager().issuesCellSize(), manager().tile(), manager().fastMode());
		
		covGrainBocagerFonctionnel.dispose();
		
		covProportionGrainBocagerFonctionnel.dispose();
		
	}

}
