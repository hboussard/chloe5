package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPCalculEnjeuxGlobaux extends GrainBocagerProcedure {

	public GBPCalculEnjeuxGlobaux(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
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
		
		Coverage covFragmentationGrainBocagerFonctionnel = GrainBocager.runSHDIClusterGrainBocagerFonctionnel(covClusterFonctionnel, manager().issuesWindowRadius(), manager().issuesCellSize(), manager().fastMode());
		
		covClusterFonctionnel.dispose();
		
		CoverageManager.write(manager().functionalGrainBocagerFragmentation(), covFragmentationGrainBocagerFonctionnel.getData(), covFragmentationGrainBocagerFonctionnel.getEntete());
			
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_fragmentation_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().functionalGrainBocagerFragmentation())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		covFragmentationGrainBocagerFonctionnel.dispose();
		
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(manager().functionalGrainBocager());
		
		System.out.println("calcul des proportions de grain bocager fonctionnel dans une fenetre de "+manager().issuesWindowRadius()+"m avec une resolution de "+manager().issuesCellSize()+"m");
		
		Coverage covProportionGrainBocagerFonctionnel = GrainBocager.runProportionGrainBocagerFonctionnel(covGrainBocagerFonctionnel, manager().issuesWindowRadius(), manager().issuesCellSize(), manager().fastMode());
		
		covGrainBocagerFonctionnel.dispose();
		
		CoverageManager.write(manager().functionalGrainBocagerProportion(), covProportionGrainBocagerFonctionnel.getData(), covProportionGrainBocagerFonctionnel.getEntete());
			
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_proportion_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().functionalGrainBocagerProportion())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		covProportionGrainBocagerFonctionnel.dispose();
	}

}
