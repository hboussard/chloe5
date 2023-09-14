package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite.HugeGBPClusterisationFonctionnalite;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPCalculEnjeuxGlobaux extends GrainBocagerProcedure {

	public HugeGBPCalculEnjeuxGlobaux(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		/*
		Coverage covClusterFonctionnel;
		if(!new File(manager().clusterGrainBocagerFonctionnel()).exists()){
			covClusterFonctionnel = new HugeGBPClusterisationFonctionnalite(manager()).run();
		}else{
			covClusterFonctionnel = CoverageManager.getCoverage(manager().clusterGrainBocagerFonctionnel());
		}
		
		System.out.println("calcul des zones de fragmentation de grain bocager fonctionnel à "+manager().enjeuxCellSize()+"m en utilisant une fenêtre de "+manager().enjeuxWindowRadius()+"m");
		
		Coverage covFragmentationGrainBocagerFonctionnel = HugeGrainBocager.runSHDIClusterGrainBocagerFonctionnel(manager().zoneFragmentationGrainBocagerFonctionnel(), covClusterFonctionnel, manager().entete(), manager().enjeuxWindowRadius(), manager().enjeuxCellSize(), manager().tile(), manager().modeFast());
		
		covClusterFonctionnel.dispose();
		
		covFragmentationGrainBocagerFonctionnel.dispose();
		*/
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(manager().grainBocagerFonctionnel());
		
		System.out.println("calcul des proportions de grain bocager fonctionnel dans une fenêtre de "+manager().enjeuxWindowRadius()+"m avec une resolution de "+manager().enjeuxCellSize()+"m");
		
		Coverage covProportionGrainBocagerFonctionnel = HugeGrainBocager.runProportionGrainBocagerFonctionnel(manager().proportionGrainBocagerFonctionnel(), covGrainBocagerFonctionnel, manager().entete(), manager().enjeuxWindowRadius(), manager().enjeuxCellSize(), manager().tile(), manager().modeFast());
		
		covGrainBocagerFonctionnel.dispose();
		
		return covProportionGrainBocagerFonctionnel;
		
	}

}
