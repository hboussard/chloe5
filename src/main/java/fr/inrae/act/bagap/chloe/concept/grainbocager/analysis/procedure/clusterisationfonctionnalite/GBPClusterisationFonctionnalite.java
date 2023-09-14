package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager.GBPCalculGrainBocager;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPClusterisationFonctionnalite extends GrainBocagerProcedure {

	public GBPClusterisationFonctionnalite(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {

		Coverage covGrainBocager;
		if(!new File(manager().grainBocager()).exists()){
			covGrainBocager = new GBPCalculGrainBocager(manager()).run();
		}else{
			covGrainBocager = CoverageManager.getCoverage(manager().grainBocager());
		}
		
		System.out.println("seuillage des zones fonctionnelles en utilisant le seuil "+manager().seuil());
		
		Coverage covSeuillageFonctionnel = GrainBocager.runClassificationFonctionnelle(covGrainBocager, manager().seuil());
		
		if(!manager().grainBocagerFonctionnel().equalsIgnoreCase("")){
			
			CoverageManager.write(manager().grainBocagerFonctionnel(), covSeuillageFonctionnel.getData(), covSeuillageFonctionnel.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().grainBocagerFonctionnel())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("clusterisation des zones fonctionnelles");
		
		Coverage covClusterFonctionnel = GrainBocager.runClusterisationGrainFonctionnel(covSeuillageFonctionnel);
		
		covSeuillageFonctionnel.dispose();
		
		if(!manager().clusterGrainBocagerFonctionnel().equalsIgnoreCase("")){
			
			CoverageManager.write(manager().clusterGrainBocagerFonctionnel(), covClusterFonctionnel.getData(), covClusterFonctionnel.getEntete());
			
		}
		
		return covClusterFonctionnel;
		
	}

}
