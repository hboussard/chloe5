package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class HugeGBPClusterisationFonctionnalite extends GrainBocagerProcedure {

	public HugeGBPClusterisationFonctionnalite(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
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
		
		Coverage covSeuillageFonctionnel = HugeGrainBocager.runClassificationFonctionnelle(manager().functionalGrainBocager(), covGrainBocager, manager().threshold());
		
		System.out.println("clusterisation des zones fonctionnelles");
		
		Coverage covClusterFonctionnel = HugeGrainBocager.runClusterisationGrainFonctionnel(manager().functionalGrainBocagerClustering(), covSeuillageFonctionnel, covGrainBocager.getEntete().noDataValue());
		
		covSeuillageFonctionnel.dispose();
		
		covClusterFonctionnel.dispose();
		
	}

}
