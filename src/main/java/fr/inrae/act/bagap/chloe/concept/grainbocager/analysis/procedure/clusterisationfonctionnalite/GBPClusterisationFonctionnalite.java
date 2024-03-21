package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPClusterisationFonctionnalite extends GrainBocagerProcedure {

	public GBPClusterisationFonctionnalite(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().grainBocager()).exists()){
			
			factory().parentFactory().create(manager()).run();
			
		}
	}
	
	@Override
	public void doRun() {

		Coverage covGrainBocager = CoverageManager.getCoverage(manager().grainBocager());
		
		System.out.println("seuillage des zones fonctionnelles en utilisant le seuil "+manager().threshold());
		
		Coverage covSeuillageFonctionnel = GrainBocager.runClassificationFonctionnelle(covGrainBocager, manager().threshold());
		
		covGrainBocager.dispose();
		
		CoverageManager.write(manager().functionalGrainBocager(), covSeuillageFonctionnel.getData(), covSeuillageFonctionnel.getEntete());
			
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().functionalGrainBocager())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("clusterisation des zones fonctionnelles");
		
		Coverage covClusterFonctionnel = GrainBocager.runClusterisationGrainFonctionnel(covSeuillageFonctionnel);
		
		covSeuillageFonctionnel.dispose();
		
		CoverageManager.write(manager().functionalGrainBocagerClustering(), covClusterFonctionnel.getData(), covClusterFonctionnel.getEntete());
			
		covClusterFonctionnel.dispose();
		
	}

}
