package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager.HugeGBPCalculGrainBocager;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPClusterisationFonctionnalite extends GrainBocagerProcedure {

	public HugeGBPClusterisationFonctionnalite(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {

		Coverage covGrainBocager;
		if(!new File(manager().grainBocager()).exists()){
			covGrainBocager = new HugeGBPCalculGrainBocager(manager()).run();
		}else{
			covGrainBocager = CoverageManager.getCoverage(manager().grainBocager());
		}
		
		System.out.println("seuillage des zones fonctionnelles en utilisant le seuil "+manager().seuil());
		
		Coverage covSeuillageFonctionnel = HugeGrainBocager.runClassificationFonctionnelle(manager().grainBocagerFonctionnel(), covGrainBocager, manager().seuil());
		
		System.out.println("clusterisation des zones fonctionnelles");
		
		Coverage covClusterFonctionnel = HugeGrainBocager.runClusterisationGrainFonctionnel(manager().clusterGrainBocagerFonctionnel(), covSeuillageFonctionnel, covGrainBocager.getEntete().noDataValue());
		
		covSeuillageFonctionnel.dispose();
		
		return covClusterFonctionnel;
		
	}

}
